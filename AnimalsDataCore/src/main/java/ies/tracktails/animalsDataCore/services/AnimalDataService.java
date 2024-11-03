package ies.tracktails.animalsDataCore.services;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnimalDataService {

    private final InfluxDBClient influxDBClient;

    @Autowired
    public AnimalDataService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public void writeAnimalData(AnimalDataDTO animalDataDTO) {
        // Cria um ponto de dados a partir do DTO AnimalData
        Point point = Point.measurement("animal_data")
                .addTag("animalId", animalDataDTO.getAnimalId()) // Tag que identifica o animal
                .time(System.currentTimeMillis(), WritePrecision.MS); // Define a timestamp

        // Adiciona os campos se os valores estiverem presentes
        animalDataDTO.getWeight().ifPresent(weight -> point.addField("weight", weight));
        animalDataDTO.getHeight().ifPresent(height -> point.addField("height", height));
        animalDataDTO.getLatitude().ifPresent(latitude -> point.addField("latitude", latitude));
        animalDataDTO.getLongitude().ifPresent(longitude -> point.addField("longitude", longitude));
        animalDataDTO.getSpeed().ifPresent(speed -> point.addField("speed", speed));
        animalDataDTO.getHeartRate().ifPresent(heartRate -> point.addField("heartRate", heartRate));
        animalDataDTO.getBreathRate().ifPresent(breathRate -> point.addField("breathRate", breathRate));

        // Adiciona as tags adicionais
        for (Map.Entry<String, String> entry : animalDataDTO.getAdditionalTags().entrySet()) {
            point.addTag(entry.getKey(), entry.getValue());
        }

        // Escreve o ponto no InfluxDB
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }

    public void close() {
        // Fecha a conexão com o InfluxDB quando o serviço não for mais necessário
        influxDBClient.close();
    }
}
