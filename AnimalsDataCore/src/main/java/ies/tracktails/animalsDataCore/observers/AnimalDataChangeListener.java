package ies.tracktails.animalsDataCore.observers;

import com.fasterxml.jackson.core.JsonProcessingException;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;

public interface AnimalDataChangeListener {
    void onAnimalDataChanged(AnimalDataDTO animalDataDTO);
}