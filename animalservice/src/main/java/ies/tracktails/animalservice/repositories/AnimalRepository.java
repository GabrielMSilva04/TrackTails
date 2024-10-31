package ies.tracktails.animalservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ies.tracktails.animalservice.entities.Animal;

public interface AnimalRepository extends JpaRepository <Animal, Long> {
    Animal findByName(String name);
}