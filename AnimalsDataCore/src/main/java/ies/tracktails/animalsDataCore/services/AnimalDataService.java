package ies.tracktails.animalsDataCore.services;

import ies.tracktails.animalsDataCore.configurations.InfluxDBConfig;
import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.List;

@Service
public class AnimalDataService {
    
    private final InfluxDBClient influxDBClient;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Autowired
    public AnimalDataService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    // Write Operation
    public void writeAnimalData(AnimalDataDTO animalDataDTO) {
        Point point = Point.measurement("animal_data")
                .addTag("animalId", animalDataDTO.getAnimalId()) // Tag que identifica o animal
                .time(System.currentTimeMillis(), WritePrecision.MS); // Define a timestamp

        animalDataDTO.getWeight().ifPresent(weight -> point.addField("weight", weight));
        animalDataDTO.getHeight().ifPresent(height -> point.addField("height", height));
        animalDataDTO.getLatitude().ifPresent(latitude -> point.addField("latitude", latitude));
        animalDataDTO.getLongitude().ifPresent(longitude -> point.addField("longitude", longitude));
        animalDataDTO.getSpeed().ifPresent(speed -> point.addField("speed", speed));
        animalDataDTO.getHeartRate().ifPresent(heartRate -> point.addField("heartRate", heartRate));
        animalDataDTO.getBreathRate().ifPresent(breathRate -> point.addField("breathRate", breathRate));

        // Aditional tags
        for (Map.Entry<String, String> entry : animalDataDTO.getAdditionalTags().entrySet()) {
            point.addTag(entry.getKey(), entry.getValue());
        }

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }

    // Read Operations
    //  1. Latest value of a field
    public Double getLatestValue(String animalId, String field) {
        // Query
        String query = "from(bucket: \"" + bucket + "\")\n" +
               "  |> range(start: 0)\n" + // Isso considera todos os dados, sem limite de tempo
               "  |> filter(fn: (r) => r[\"_measurement\"] == \"animal_data\")\n" +
               "  |> filter(fn: (r) => r[\"_field\"] == \"" + field + "\")\n" +
               "  |> filter(fn: (r) => r[\"animalId\"] == \"" + animalId + "\")\n" +
               "  |> group(columns: [\"_measurement\", \"_field\"], mode: \"by\")\n" +
               "  |> last()";
        
        
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query);
        debugTables(tables);
        
        if (!tables.isEmpty()) {
            List<FluxRecord> records = tables.get(0).getRecords();
            if (!records.isEmpty()) {
                FluxRecord record = records.get(0);
                return record.getValueByKey("_value") != null ? 
                       ((Number) record.getValueByKey("_value")).doubleValue() : null;
            }
        }
        return null;
    }

    // 2. Latest values of all fields
    public AnimalDataDTO getLatestValues(String animalId) {
        // Query
        String query = "from(bucket: \"" + bucket + "\")\n" +
               "  |> range(start: 0)\n" + // Isso considera todos os dados, sem limite de tempo
               "  |> filter(fn: (r) => r[\"_measurement\"] == \"animal_data\")\n" +
               "  |> filter(fn: (r) => r[\"animalId\"] == \"" + animalId + "\")\n" +
               "  |> group(columns: [\"_measurement\", \"_field\"], mode: \"by\")\n" +
               "  |> last()";
        
        System.out.println("Query: " + query);
        
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query);
        debugTables(tables);

        if (!tables.isEmpty()) {
            AnimalDataDTO animalDataDTO = new AnimalDataDTO(animalId);
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                if (!records.isEmpty()) {
                    for (FluxRecord record : records) {
                        String field = record.getValueByKey("_field").toString();
                        Double value = record.getValueByKey("_value") != null ? 
                                    ((Number) record.getValueByKey("_value")).doubleValue() : null;
                        switch (field) {
                            case "weight":
                                animalDataDTO.setWeight(value);
                                break;
                            case "height":
                                animalDataDTO.setHeight(value);
                                break;
                            case "latitude":
                                animalDataDTO.setLatitude(value);
                                break;
                            case "longitude":
                                animalDataDTO.setLongitude(value);
                                break;
                            case "speed":
                                animalDataDTO.setSpeed(value);
                                break;
                            case "heartRate":
                                animalDataDTO.setHeartRate(value);
                                break;
                            case "breathRate":
                                animalDataDTO.setBreathRate(value);
                                break;
                        }
                    }
                }
            }
            return animalDataDTO;
        }
        return null;
    }

    public void debugTables(List<FluxTable> tables) {
        for (FluxTable table : tables) {
            System.out.printf("\n\nTable: %s\n", table);
            for (FluxRecord record : table.getRecords()) {
                System.out.printf("\nRecord: %s\n", record);
                for (Map.Entry<String, Object> entry : record.getValues().entrySet()) {
                    System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void close() {
        influxDBClient.close();
    }
}
