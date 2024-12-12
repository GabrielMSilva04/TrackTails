package ies.tracktails.actionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import ies.tracktails.actionservice.dtos.ActionDTO;

@Component
public class Producer {
    @Autowired
    private final KafkaTemplate<String, ActionDTO> kafkaTemplate;
    private static final String TOPIC = "action";

    public Producer(KafkaTemplate<String, ActionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAction(String deviceId, ActionDTO actionDTO) {
        kafkaTemplate.send(TOPIC, deviceId, actionDTO);
    }
}
