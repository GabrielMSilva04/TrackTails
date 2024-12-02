package ies.tracktails.animalservice.services;

import ies.tracktails.animalservice.dtos.FenceDTO;

public interface FenceService {
    void addOrUpdateFence(FenceDTO fenceDTO);
    FenceDTO getFenceByAnimalId(Long animalId);
    void deleteFence(Long animalId);
}
