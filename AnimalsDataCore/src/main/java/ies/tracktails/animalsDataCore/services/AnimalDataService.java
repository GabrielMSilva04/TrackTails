package ies.tracktails.animalsDataCore.services;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.observers.AnimalDataChangeListener;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ArrayList;

@Service
public class AnimalDataService {

    // Observer pattern
    private final CopyOnWriteArrayList<AnimalDataChangeListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(AnimalDataChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AnimalDataChangeListener listener) {
        listeners.remove(listener);
    }

    // InfluxDB client
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
        animalDataDTO.getBatteryPercentage()
                .ifPresent(batteryPercentage -> point.addField("batteryPercentage", batteryPercentage));
        animalDataDTO.getBlinking().ifPresent(blinking -> point.addField("blinking", blinking));
        animalDataDTO.getTimestamp().ifPresent(timestamp -> point.time(timestamp.toEpochMilli(), WritePrecision.MS));

        // Aditional tags
        for (Map.Entry<String, String> entry : animalDataDTO.getAdditionalTags().entrySet()) {
            point.addTag(entry.getKey(), entry.getValue());
        }

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);

            for (AnimalDataChangeListener listener : listeners) {
                listener.onAnimalDataChanged(animalDataDTO);
            }
        }
    }

    // Read Operations
    // 1. Latest value of a field
    public AnimalDataDTO getLatestValue(String animalId, String field) {
        // Query
        String query = "from(bucket: \"" + bucket + "\")\n" +
                "  |> range(start: 0)\n" +
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
                AnimalDataDTO animalDataDTO = new AnimalDataDTO(animalId);
                Object rawValue = record.getValueByKey("_value");
                String value = null;

                if (rawValue instanceof Number) {
                    value = String.valueOf(((Number) rawValue).doubleValue());
                } else if (rawValue instanceof Boolean) {
                    value = String.valueOf(rawValue);
                } else if (rawValue != null) {
                    value = rawValue.toString(); // Fallback para outros tipos que você não esperava.
                }

                if (value != null) {
                    animalDataDTO.addField(field, value);
                }
                animalDataDTO.setTimestamp(record.getTime());
            }
        }
        return null;
    }

    // 2. Latest values of all fields
    public AnimalDataDTO getLatestValues(String animalId) {
        // Query
        String query = "from(bucket: \"" + bucket + "\")\n" +
                "  |> range(start: 0)\n" +
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
                        Object rawValue = record.getValueByKey("_value");
                        String value = null;

                        if (rawValue instanceof Number) {
                            value = String.valueOf(((Number) rawValue).doubleValue());
                        } else if (rawValue instanceof Boolean) {
                            value = String.valueOf(rawValue);
                        } else if (rawValue != null) {
                            value = rawValue.toString(); // Fallback para outros tipos que você não esperava.
                        }

                        if (value != null) {
                            animalDataDTO.addField(field, value);
                        }
                        animalDataDTO.setTimestamp(record.getTime());
                    }
                }
            }
            return animalDataDTO;
        }
        return null;
    }

    // 3. One fields in a given time range (one value for each timeslot)
    public List<AnimalDataDTO> getRangeValues(String animalId, String field, String start, String end,
            String timeWindow, String aggregateFunc) {
        // Query
        String query = "from(bucket: \"" + bucket + "\")\n" +
                "  |> range(start: " + start + ", stop: " + end + ")\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"animal_data\")\n" +
                "  |> filter(fn: (r) => r[\"animalId\"] == \"" + animalId + "\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"" + field + "\")\n" +
                "  |> group(columns: [\"_measurement\", \"_field\"], mode: \"by\")\n" +
                "  |> aggregateWindow(every: " + timeWindow + ", fn: " + aggregateFunc + ", createEmpty: false)\n" +
                "  |> yield(name: \"" + aggregateFunc + "\")";

        System.out.println("Query: " + query);
        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(query);
        debugTables(tables);

        List<AnimalDataDTO> result = new ArrayList<>();

        if (!tables.isEmpty()) {
            for (FluxTable table : tables) {
                List<FluxRecord> records = table.getRecords();
                if (!records.isEmpty()) {
                    for (FluxRecord record : records) {
                        AnimalDataDTO animalDataDTO = new AnimalDataDTO(animalId);

                        Object rawValue = record.getValueByKey("_value");
                        String value = null;

                        System.out.println("Raw value: " + rawValue);
                        if (rawValue instanceof Number) {
                            value = String.valueOf(((Number) rawValue).doubleValue());
                        } else if (rawValue instanceof Boolean) {
                            value = String.valueOf(rawValue);
                        } else if (rawValue != null) {
                            value = rawValue.toString(); // Fallback para outros tipos que você não esperava.
                        }

                        System.out.println("Value: " + value);

                        if (value != null) {
                            animalDataDTO.addField(field, value);
                        }
                        animalDataDTO.setTimestamp(record.getTime());

                        System.out.println("Adding animalDataDTO: " + animalDataDTO);
                        result.add(animalDataDTO);
                    }
                }
            }
            return result;
        }
        return null;
    }

    public void calculateAndStoreSleepDuration(String animalId) {
        // Query para calcular a duração do sono no dia atual
        String query = "from(bucket: \"" + bucket + "\")\n" +
                "  |> range(start: -1d)\n" + // Últimas 24 horas
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"animal_data\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"speed\")\n" +
                "  |> filter(fn: (r) => r[\"_value\"] < 0.1)\n" +
                "  |> filter(fn: (r) => r[\"animalId\"] == \"" + animalId + "\")\n" +
                "  |> stateDuration(fn: (r) => true, column: \"duration\", unit: 1m)\n" +
                "  |> keep(columns: [\"_time\", \"duration\"])";

        System.out.println("Query: " + query);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query);
        debugTables(tables);

        long totalDuration = 0;

        if (!tables.isEmpty()) {
            FluxTable table = tables.get(tables.size() - 1);
            List<FluxRecord> records = table.getRecords();
            FluxRecord record = records.get(records.size() - 1);
            Object duration = record.getValueByKey("duration");
            if (duration instanceof Number) {
                totalDuration = ((Number) duration).longValue();
            }
        }

        storeDailySleepDuration(animalId, totalDuration);
    }

    private void storeDailySleepDuration(String animalId, long totalDuration) {
        Point point = Point.measurement("daily_sleep_duration")
                .addTag("animalId", animalId)
                .addField("totalSleepDurationMinutes", totalDuration)
                .time(System.currentTimeMillis(), WritePrecision.MS); // Define o timestamp

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
            System.out.println("Daily sleep duration stored successfully for animalId: " + animalId);
        }
    }

    public long getStoredSleepDuration(String animalId) {
        String query = "from(bucket: \"" + bucket + "\")\n" +
                "  |> range(start: -1d)\n" + // Últimas 24 horas
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"daily_sleep_duration\")\n" +
                "  |> filter(fn: (r) => r[\"animalId\"] == \"" + animalId + "\")\n" +
                "  |> keep(columns: [\"_field\", \"_value\"])\n" +
                "  |> last()";

        System.out.println("Query: " + query);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query);

        if (!tables.isEmpty()) {
            List<FluxRecord> records = tables.get(0).getRecords();
            if (!records.isEmpty()) {
                Object duration = records.get(0).getValueByKey("_value");
                if (duration instanceof Number) {
                    return ((Number) duration).longValue();
                }
            }
        }

        return 0;
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
