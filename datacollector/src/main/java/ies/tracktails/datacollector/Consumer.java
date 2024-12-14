package ies.tracktails.datacollector;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import ies.tracktails.animalsDataCore.services.AnimalService;
import ies.tracktails.animalsDataCore.entities.Animal;

@Service
public class Consumer {

    @Autowired
    private AnimalDataService animalDataService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private AnimalMonitoringService animalMonitoringService;

    public AnimalDataDTO convertToAnimalDataDTO(DataDTO data) {
        AnimalDataDTO animalDataDTO = new AnimalDataDTO();

        Animal animal = animalService.getAnimalByDeviceId(Long.parseLong(data.getDeviceId()));

        if (animal == null) {
            return null;
        }

        animalDataDTO.setAnimalId(animal.getId().toString());

        data.getLatitude().ifPresent(latitude -> animalDataDTO.setLatitude(latitude));
        data.getLongitude().ifPresent(longitude -> animalDataDTO.setLongitude(longitude));
        data.getSpeed().ifPresent(speed -> animalDataDTO.setSpeed(speed));
        data.getBpm().ifPresent(heartRate -> animalDataDTO.setHeartRate(heartRate));
        data.getRespiratory_rate().ifPresent(breathRate -> animalDataDTO.setBreathRate(breathRate));
        data.getBatteryPercentage().ifPresent(batteryPercentage -> animalDataDTO.setBatteryPercentage(batteryPercentage));

        animalMonitoringService.check(animalDataDTO);

        return animalDataDTO;
    }

    @KafkaListener(topics = "animal_tracking_topic", groupId = "kafka-listener-group")
    public void listen(DataDTO data) {
        try {
            System.out.println("Received message: " + data.toString());
            AnimalDataDTO animalDataDTO = convertToAnimalDataDTO(data);
            animalDataService.writeAnimalData(animalDataDTO);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}