package ies.tracktails.animalsDataCore.repositories;

import ies.tracktails.animalsDataCore.entities.Fence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FenceRepository extends JpaRepository<Fence, Long> {
    Optional<Fence> findByAnimalId(Long animalId);

    void deleteByAnimalId(Long animalId);
}