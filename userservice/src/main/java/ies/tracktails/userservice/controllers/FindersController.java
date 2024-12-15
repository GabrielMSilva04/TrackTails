package ies.tracktails.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import ies.tracktails.userservice.dtos.FindersRequest;
import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.services.UserService;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalsDataCore.services.AnimalService;


@RestController
@RequestMapping("/api/v1/finders/user")
class FindersController {

    private final UserService userService;
    private final AnimalService animalService;

    @Autowired
    public FindersController(UserService userService, AnimalService animalService) {
        this.userService = userService;
        this.animalService = animalService;
    }

    @Operation(summary = "Get User By Id", description = "Get User Contact Information By Id")
    @PostMapping()
    public ResponseEntity<?> getAnimal(@RequestBody FindersRequest findersRequest) {
        Animal animal = animalService.getAnimalByDeviceId(findersRequest.getDeviceId());
        User user = userService.getUserById(animal.getUserId());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}