package ies.tracktails.animalsDataCore.repositories;

import ies.tracktails.animalsDataCore.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository <Animal, Long> {
    Animal findByName(String name);
    List<Animal> findByUserId(Long userId);
    Animal findByDeviceId(Long deviceId);
}