package ies.tracktails.animalsDataCore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ies.tracktails.animalsDataCore.entities.Animal;

@Repository
public interface AnimalRepository extends JpaRepository <Animal, Long> {
    Animal findByName(String name);
}