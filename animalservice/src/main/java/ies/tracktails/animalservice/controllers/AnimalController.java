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

    @Operation(summary = "Upload image for an animal", description = "Upload an image and associate it with an animal")
    @PostMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAnimalImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile,
            @RequestHeader("X-User-Id") String userIdHeader) {

        // Validate user authentication
        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        // Validate animal ownership
        Long userId = (Long) response.getBody();
        Animal animal = animalService.getAnimal(id);
        if (animal == null || !animal.getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized or Animal not found", HttpStatus.NOT_FOUND);
        }

        try {
            // Validate file size
            long maxFileSizeBytes = 5 * 1024 * 1024; // 5MB
            if (imageFile.getSize() > maxFileSizeBytes) {
                return new ResponseEntity<>("File size exceeds the maximum limit of 2MB", HttpStatus.BAD_REQUEST);
            }

            // Validate file type
            String contentType = imageFile.getContentType();
            List<String> allowedTypes = List.of("image/png", "image/jpeg", "image/jpg", "image/heic", "image/heif");
            if (!allowedTypes.contains(contentType)) {
                return new ResponseEntity<>("Unsupported file format", HttpStatus.BAD_REQUEST);
            }

            // Save file logic
            Path uploadDir = Paths.get("/uploads/animals");
            Files.createDirectories(uploadDir);

            String filename = id + "_" + UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, imageFile.getBytes());

            // Update animal record
            animal.setImagePath(filename);
            animalService.updateAnimal(id, animal);

            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get animal image", description = "Retrieve the uploaded image for an animal")
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getAnimalImage(@PathVariable Long id, @RequestHeader("X-User-Id") String userIdHeader) {

        ResponseEntity<?> response = validateAndGetUserId(userIdHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();

        Animal animal = animalService.getAnimal(id);
        if (animal == null || !animal.getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized or Animal not found", HttpStatus.NOT_FOUND);
        }

        if (animal.getImagePath() == null) {
            return new ResponseEntity<>("No image found for this animal", HttpStatus.NOT_FOUND);
        }

        try {
            Path filePath = Paths.get("/uploads/animals").resolve(animal.getImagePath());
            Resource imageResource = new UrlResource(filePath.toUri());

            if (imageResource.exists() && imageResource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
            } else {
                return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
