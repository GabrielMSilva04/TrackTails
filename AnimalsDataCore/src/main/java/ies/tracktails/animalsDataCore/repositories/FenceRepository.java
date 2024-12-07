package ies.tracktails.animalsDataCore.repositories;

import ies.tracktails.animalsDataCore.entities.Fence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FenceRepository extends JpaRepository<Fence, Long> {
    Fence findByAnimalId(Long animalId);
}