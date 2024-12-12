package ies.tracktails.actionservice.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.tracktails.actionservice.services.ActionService;
import ies.tracktails.actionservice.Producer;
import ies.tracktails.actionservice.dtos.ActionDTO;
import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalsDataCore.services.AnimalService;

@Service
public class ActionServiceImplementation implements ActionService {
    private final List<String> validActionTypes = List.of("blink", "sound");
    private final Producer producer;
    private final AnimalService animalService;

    @Autowired
    public ActionServiceImplementation(Producer producer, AnimalService animalService) {
        this.producer = producer;
        this.animalService = animalService;
    }

    public void triggerAction(ActionDTO action) {
        Animal animal = animalService.getAnimal(action.getAnimalId());
        Long deviceId = animal.getDeviceId();
        producer.sendAction(deviceId.toString(), action);
    }

    public List<String> getValidActionTypes() {
        return validActionTypes;
    }

    public boolean isActionTypeValid(String actionType) {
        return validActionTypes.contains(actionType);
    }
}