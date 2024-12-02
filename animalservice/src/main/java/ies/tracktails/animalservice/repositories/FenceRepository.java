package ies.tracktails.animalservice.repositories;

import ies.tracktails.animalservice.entities.Fence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FenceRepository extends JpaRepository<Fence, Long> {
    Fence findByAnimalId(Long animalId);
}