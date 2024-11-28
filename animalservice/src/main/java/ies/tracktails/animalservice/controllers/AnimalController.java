package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalservice.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {
    private final AnimalService animalService;

    @Value("${upload.max-file-size}")
    private long maxFileSize;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    private ResponseEntity<?> validateAndGetUserId(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.isBlank()) {
            return new ResponseEntity<>("Unauthorized: Missing X-User-Id header", HttpStatus.UNAUTHORIZED);
        }
        try {
            Long userId = Long.parseLong(userIdHeader);
            return ResponseEntity.ok(userId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Unauthorized: Invalid X-User-Id header", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Create animal", description = "Create a new animal")
    @PostMapping
    public ResponseEntity<?> createAnimal(@RequestBody Animal animal, @RequestHeader("X-User-Id") String userIdHeader) {
        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        animal.setUserId(userId);

        Animal savedAnimal = animalService.addAnimal(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete animal", description = "Delete an animal by ID")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable long id, @RequestHeader("X-User-Id") String userIdHeader) {
        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        Animal animal = animalService.getAnimal(id);
        if (animal == null || !animal.getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized or Animal not found", HttpStatus.NOT_FOUND);
        }

        animalService.removeAnimal(id);
        return new ResponseEntity<>("Animal deleted successfully", HttpStatus.OK);
    }

    @Operation(summary = "Update animal", description = "Update an animal by ID")
    @PutMapping("{id}")
    public ResponseEntity<?> updateAnimal(@PathVariable long id, @RequestBody Animal animal, @RequestHeader("X-User-Id") String userIdHeader) {
        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        Animal existingAnimal = animalService.getAnimal(id);
        if (existingAnimal == null || !existingAnimal.getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized or Animal not found", HttpStatus.NOT_FOUND);
        }

        animal.setId(id);
        animal.setUserId(userId);

        Animal updatedAnimal = animalService.updateAnimal(id, animal);
        return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
    }

    @Operation(summary = "Get animal", description = "Get animal")
    @GetMapping("{id}")
    public ResponseEntity<?> getAnimal(@PathVariable long id, @RequestHeader("X-User-Id") String userIdHeader) {
        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        Animal animal = animalService.getAnimal(id);
        if (animal == null || !animal.getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized or Animal not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @Operation(summary = "Get all animals", description = "Get all animals")
    @GetMapping
    public ResponseEntity<?> getAllAnimals(@RequestHeader("X-User-Id") String userIdHeader) {
        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        List<Animal> animals = animalService.getAnimalsByUserId(userId);
        return new ResponseEntity<>(animals, HttpStatus.OK);
    }
}
