package ies.tracktails.animalsDataCore.services;

import ies.tracktails.animalsDataCore.entities.Animal;
import java.util.List;

public interface AnimalService {
    public Animal addAnimal(Animal animal);
    public void removeAnimal(Long id);
    public Animal updateAnimal(Long id, Animal animal);
    public Animal getAnimal(Long id);
    public Animal getAnimalByName(String name);
    public List<Animal> getAllAnimals();
    public List<Animal> getAnimalsByUserId(Long userId);
    public Animal getAnimalByDeviceId(Long deviceId);
    public Boolean userHasAccessToAnimal(String userId, String animalId);
}