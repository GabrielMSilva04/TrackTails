package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalsDataCore.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/finders/animal")
class FindersController {

    private final AnimalService animalService;

    @Autowired
    public FindersController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "Get Animal By DeviceId", description = "Get an animal by the id of the device that is attached to it")
    @GetMapping("{deviceId}")
    public ResponseEntity<?> getAnimal(@PathVariable long deviceId) {
        Animal animal = animalService.getAnimalByDeviceId(deviceId);
        if (animal == null) {
            return new ResponseEntity<>("Animal not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @Operation(summary = "Get Public Animal Image", description = "Retrieve the uploaded image for an animal (public access)")
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getAnimalImage(@PathVariable Long id) {
        Animal animal = animalService.getAnimal(id);
        if (animal == null) {
            return new ResponseEntity<>("Animal not found", HttpStatus.NOT_FOUND);
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