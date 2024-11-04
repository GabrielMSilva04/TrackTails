package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalservice.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ies.tracktails.animalservice.components.JwtUtil;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {
    private final AnimalService animalService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AnimalController(AnimalService animalService, JwtUtil jwtUtil) {
        this.animalService = animalService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Animal> createAnimal(@RequestBody Animal animal, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authorizationHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        animal.setUserId(Long.parseLong(userId));

        Animal savedAnimal = animalService.addAnimal(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable long id) {
        animalService.removeAnimal(id);
        return new ResponseEntity<>("Animal deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable long id, @RequestBody Animal animal) {
        animal.setId(id);
        Animal updatedAnimal = animalService.updateAnimal(id, animal);
        return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable long id) {
        Animal animal = animalService.getAnimal(id);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllAnimals(@RequestParam(name= "name", required = false) String name, @RequestParam(name= "userId", required = false) Long userId) {
        if (name != null) {
            Animal animal = animalService.getAnimalByName(name);
            return new ResponseEntity<>(animal, HttpStatus.OK);
        }
        if (userId != null) {
            return new ResponseEntity<>(animalService.getAnimalsByUserId(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>(animalService.getAllAnimals(), HttpStatus.OK);
    }
}