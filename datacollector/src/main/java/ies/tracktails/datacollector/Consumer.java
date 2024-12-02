package ies.tracktails.datacollector;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Consumer {

    @Autowired
    private AnimalDataService animalDataService;

    @KafkaListener(topics = "animal_tracking_topic", groupId = "kafka-listener-group")
    public void listen(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            DataDTO animalData = mapper.readValue(message, DataDTO.class);
            animalDataService.writeAnimalData(animalData);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}



