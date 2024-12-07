package ies.tracktails.animalsDataCore.services.impl;

import ies.tracktails.animalsDataCore.services.FenceService;
import ies.tracktails.animalsDataCore.entities.Fence;
import ies.tracktails.animalsDataCore.dtos.FenceDTO;
import ies.tracktails.animalsDataCore.repositories.FenceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class FenceServiceImpl implements FenceService {
    private final FenceRepository fenceRepository;

    @Autowired
    public FenceServiceImpl(FenceRepository fenceRepository) {
        this.fenceRepository = fenceRepository;
    }

    // Add or update fence
    @Override
    public void addOrUpdateFence(FenceDTO fenceDTO) {
        Fence fence = fenceRepository.findByAnimalId(fenceDTO.getAnimalId())
                .orElseGet(() -> {
                    Fence newFence = new Fence();
                    newFence.setAnimalId(fenceDTO.getAnimalId());
                    newFence.setCreatedAt(Instant.now());
                    return newFence;
                });

        // Update common properties
        fence.setUpdatedAt(Instant.now());
        fence.setPoint1Latitude(fenceDTO.getPoint1Latitude());
        fence.setPoint1Longitude(fenceDTO.getPoint1Longitude());
        fence.setPoint2Latitude(fenceDTO.getPoint2Latitude());
        fence.setPoint2Longitude(fenceDTO.getPoint2Longitude());
        fence.setPoint3Latitude(fenceDTO.getPoint3Latitude());
        fence.setPoint3Longitude(fenceDTO.getPoint3Longitude());
        fence.setPoint4Latitude(fenceDTO.getPoint4Latitude());
        fence.setPoint4Longitude(fenceDTO.getPoint4Longitude());

        fenceRepository.save(fence);
    }

    // Get fence by animal ID
    @Override
    public FenceDTO getFenceByAnimalId(Long animalId) {
        Fence fence = fenceRepository.findByAnimalId(animalId).orElse(null);

        if (fence == null) {
            return null;
        }

        FenceDTO fenceDTO = new FenceDTO(fence.getAnimalId());
        fenceDTO.setPoint1Latitude(fence.getPoint1Latitude());
        fenceDTO.setPoint1Longitude(fence.getPoint1Longitude());
        fenceDTO.setPoint2Latitude(fence.getPoint2Latitude());
        fenceDTO.setPoint2Longitude(fence.getPoint2Longitude());
        fenceDTO.setPoint3Latitude(fence.getPoint3Latitude());
        fenceDTO.setPoint3Longitude(fence.getPoint3Longitude());
        fenceDTO.setPoint4Latitude(fence.getPoint4Latitude());
        fenceDTO.setPoint4Longitude(fence.getPoint4Longitude());

        return fenceDTO;
    }

    // Delete fence by animal ID
    @Override
    public void deleteFence(Long animalId) {
        Fence fence = fenceRepository.findByAnimalId(animalId).orElse(null);

        if (fence == null) {
            throw new EntityNotFoundException("Fence not found for animalId: " + animalId);
        }

        fenceRepository.delete(fence);
    }
}
