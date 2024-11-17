package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalservice.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import ies.tracktails.animalservice.components.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {
    private final AnimalService animalService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AnimalController(AnimalService animalService, JwtTokenProvider jwtTokenProvider) {
        this.animalService = animalService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private ResponseEntity<?> validateAndExtractUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Unauthorized: Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }
        String token = authorizationHeader.substring(7);
        try {
            jwtTokenProvider.validateToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            return ResponseEntity.ok(userId);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Unauthorized: Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Create animal", description = "Create a new animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Animal created successfully", content = @Content(schema = @Schema(implementation = Animal.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<?> createAnimal(@RequestBody Animal animal, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        ResponseEntity<?> response = validateAndExtractUserId(authorizationHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        animal.setUserId(userId);

        Animal savedAnimal = animalService.addAnimal(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete animal", description = "Delete an animal by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animal deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animal not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable long id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        ResponseEntity<?> response = validateAndExtractUserId(authorizationHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        animalService.removeAnimal(id);
        return new ResponseEntity<>("Animal deleted successfully", HttpStatus.OK);
    }

    @Operation(summary = "Update animal", description = "Update an animal by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animal updated successfully", content = @Content(schema = @Schema(implementation = Animal.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animal not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<?> updateAnimal(@PathVariable long id, @RequestBody Animal animal, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        ResponseEntity<?> response = validateAndExtractUserId(authorizationHeader);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        Long userId = (Long) response.getBody();
        animal.setId(id);
        animal.setUserId(userId);

        Animal updatedAnimal = animalService.updateAnimal(id, animal);
        return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
    }

    @Operation(summary = "Get animal",
            description = "Get animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animal retrieved successfully", content = @Content(schema = @Schema(implementation = Animal.class))),
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Animals retrieved successfully", content = @Content(schema = @Schema(implementation = Animal.class))),
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