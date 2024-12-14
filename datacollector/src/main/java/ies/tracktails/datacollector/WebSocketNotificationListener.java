package ies.tracktails.datacollector;

import ies.tracktails.animalsDataCore.observers.AnimalDataChangeListener;
import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotificationListener implements AnimalDataChangeListener {
    @Autowired
    private final KafkaTemplate<String, AnimalDataDTO> kafkaTemplate;
    private static final String TOPIC = "animal-data"; // Nome do tópico Kafka

    public WebSocketNotificationListener(KafkaTemplate<String, AnimalDataDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onAnimalDataChanged(AnimalDataDTO animalDataDTO) {
        String partitionKey = animalDataDTO.getAnimalId();
        kafkaTemplate.send(TOPIC, partitionKey, animalDataDTO);
    }
}
