package ies.tracktails.animalsDataCore.services.impl;

import ies.tracktails.animalsDataCore.services.AnimalService;
import ies.tracktails.animalsDataCore.repositories.AnimalRepository;
import ies.tracktails.animalsDataCore.entities.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public Animal addAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    @Override
    public void removeAnimal(Long id) {
        animalRepository.deleteById(id);
    }

    @Override
    public Animal updateAnimal(Long id, Animal animal) {
        Animal existingAnimal = animalRepository.findById(animal.getId()).get();
        existingAnimal.setName(animal.getName());
        existingAnimal.setSpecies(animal.getSpecies());
        existingAnimal.setBreed(animal.getBreed());
        existingAnimal.setSex(animal.getSex());
        existingAnimal.setBirthday(animal.getBirthday());
        existingAnimal.setUserId(animal.getUserId());
        existingAnimal.setDeviceId(animal.getDeviceId());
        existingAnimal.setBeCarefulWith(animal.getBeCarefulWith());
        existingAnimal.setImagePath(animal.getImagePath());
        return animalRepository.save(existingAnimal);
    }

    @Override
    public Animal getAnimal(Long id) {
        return animalRepository.findById(id).isPresent() ? animalRepository.findById(id).get() : null;
    }

    @Override
    public Animal getAnimalByName(String name) {
        return animalRepository.findByName(name);
    }

    @Override
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    @Override
    public List<Animal> getAnimalsByUserId(Long userId) {
        return animalRepository.findByUserId(userId);
    }

    @Override
    public Animal getAnimalByDeviceId(Long deviceId) {
        return animalRepository.findByDeviceId(deviceId);
    }

    @Override
    public Boolean userHasAccessToAnimal(String userId, String animalId) {
        Animal animal = animalRepository.findById(Long.parseLong(animalId)).get();
        return animal.getUserId().equals(Long.parseLong(userId));
    }
}