package ies.tracktails.animalservice;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnimalDataKafkaConsumer {

    private final AnimalDataWebSocketHandler webSocketHandler;

    public AnimalDataKafkaConsumer(AnimalDataWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @KafkaListener(
        topics = "animal-data",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(AnimalDataDTO animalData) {
        try {
            String animalId = animalData.getAnimalId();
            // Transmite os dados via WebSocket
            webSocketHandler.broadcast(animalId, animalData.toString());
        } catch (Exception e) {
            // Log e tratamento de erros
            e.printStackTrace();
        }
    }
}
