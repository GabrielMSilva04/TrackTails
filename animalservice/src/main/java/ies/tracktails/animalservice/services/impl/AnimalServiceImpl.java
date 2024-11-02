package ies.tracktails.animalservice.services.impl;

import ies.tracktails.animalservice.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ies.tracktails.animalsDataCore.repositories.AnimalRepository;
import ies.tracktails.animalsDataCore.entities.Animal;
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
    public void removeAnimal(long id) {
        animalRepository.deleteById(id);
    }

    @Override
    public Animal updateAnimal(long id, Animal animal) {
        Animal existingAnimal = animalRepository.findById(animal.getId()).get();
        existingAnimal.setName(animal.getName());
        existingAnimal.setSpecies(animal.getSpecies());
        existingAnimal.setBirthDate(animal.getBirthDate());
        return animalRepository.save(existingAnimal);
    }

    @Override
    public Animal getAnimal(long id) {
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
}