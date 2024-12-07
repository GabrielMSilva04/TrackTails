package ies.tracktails.animalsDataCore.services;

import ies.tracktails.animalsDataCore.dtos.FenceDTO;

public interface FenceService {
    void addOrUpdateFence(FenceDTO fenceDTO);
    FenceDTO getFenceByAnimalId(Long animalId);
    void deleteFence(Long animalId);
}
