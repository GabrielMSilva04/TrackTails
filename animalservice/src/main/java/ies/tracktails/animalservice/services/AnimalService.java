package ies.tracktails.animalservice.services;

import ies.tracktails.animalservice.entities.Animal;
import java.util.List;

public interface AnimalService {
    public Animal addAnimal(Animal animal);
    public void removeAnimal(long id);
    public Animal updateAnimal(long id, Animal animal);
    public Animal getAnimal(long id);
    public Animal getAnimalByName(String name);
    public List<Animal> getAllAnimals();
}