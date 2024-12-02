package ies.tracktails.datacollector;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalDataService;

@Service
public class Consumer {

    @Autowired
    private AnimalDataService animalDataService;

    public AnimalDataDTO convertToAnimalDataDTO(DataDTO data) {
        AnimalDataDTO animalDataDTO = new AnimalDataDTO();
        animalDataDTO.setAnimalId(data.getDeviceId());


        data.getLatitude().ifPresent(latitude -> animalDataDTO.setLatitude(latitude));
        data.getLongitude().ifPresent(longitude -> animalDataDTO.setLongitude(longitude));
        data.getSpeed().ifPresent(speed -> animalDataDTO.setSpeed(speed));
        data.getBpm().ifPresent(heartRate -> animalDataDTO.setHeartRate(heartRate));
        data.getRespiratory_rate().ifPresent(breathRate -> animalDataDTO.setBreathRate(breathRate));

        return animalDataDTO;
    }

    @KafkaListener(topics = "animal_tracking_topic", groupId = "kafka-listener-group")
    public void listen(DataDTO data) {
        try {
            System.out.println(data.getDeviceId());
            System.out.println("Received message: " + data.toString());
            AnimalDataDTO animalDataDTO = convertToAnimalDataDTO(data);
            animalDataService.writeAnimalData(animalDataDTO);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}




