package ies.tracktails.animalsDataCore.observers;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;

public interface AnimalDataChangeListener {
    void onAnimalDataChanged(AnimalDataDTO animalDataDTO);
}