package ies.tracktails.animalservice.services.impl;

import ies.tracktails.animalservice.services.FenceService;
import ies.tracktails.animalservice.entities.Fence;
import ies.tracktails.animalservice.dtos.FenceDTO;
import ies.tracktails.animalservice.repositories.FenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FenceServiceImpl implements FenceService {
    private final FenceRepository fenceRepository;

    @Autowired
    public FenceServiceImpl(FenceRepository fenceRepository) {
        this.fenceRepository = fenceRepository;
    }

    // Add or update fence
    public void addOrUpdateFence(FenceDTO fenceDTO) {
        Fence fence = new Fence(fenceDTO);
        fenceRepository.save(fence);
    }

    // Get fence by animal ID
    public FenceDTO getFenceByAnimalId(Long animalId) {
        Fence fence = fenceRepository.findByAnimalId(animalId);
        return fence != null ? new FenceDTO(fence) : null;
    }

    // Delete fence by animal ID
    public void deleteFence(Long animalId) {
        Fence fence = fenceRepository.findByAnimalId(animalId);
        if (fence != null) {
            fenceRepository.delete(fence);
        }
    }
}
