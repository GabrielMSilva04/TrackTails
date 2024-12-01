package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalsDataCore.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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
}