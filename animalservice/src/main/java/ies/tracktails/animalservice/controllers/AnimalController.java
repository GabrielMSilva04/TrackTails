package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalservice.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ies.tracktails.animalservice.components.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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

    @Operation(summary = "Create animal",
            description = "Create animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Animal created successfully", content = @Content(schema = @Schema(implementation = Animal.class)))
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = String.class)))
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Delete animal",
            description = "Delete animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animal deleted successfully", content = @Content(schema = @Schema(implementation = String.class)))
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animal not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable long id) {
        animalService.removeAnimal(id);
        return new ResponseEntity<>("Animal deleted successfully", HttpStatus.OK);
    }

    @Operation(summary = "Update animal",
            description = "Update animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animal updated successfully", content = @Content(schema = @Schema(implementation = Animal.class)))
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animal not found", content = @Content(schema = @Schema(implementation = String.class)))
            @
    })
    @PutMapping("{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable long id, @RequestBody Animal animal) {
        animal.setId(id);
        Animal updatedAnimal = animalService.updateAnimal(id, animal);
        return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
    }

    @Operation(summary = "Get animal",
            description = "Get animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animal retrieved successfully", content = @Content(schema = @Schema(implementation = Animal.class)))
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animal not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable long id) {
        Animal animal = animalService.getAnimal(id);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @Operation(summary = "Get all animals",
            description = "Get all animals")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animals retrieved successfully", content = @Content(schema = @Schema(implementation = Animal.class)))
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animals not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
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