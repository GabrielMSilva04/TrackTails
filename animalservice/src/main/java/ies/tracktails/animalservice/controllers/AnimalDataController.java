package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/animaldata")
public class AnimalDataController {
    private final AnimalDataService animalDataService;

    @Autowired
    public AnimalDataController(AnimalDataService animalDataService) {
        this.animalDataService = animalDataService;
    }

    @PostMapping
    public ResponseEntity<AnimalDataDTO> createAnimalData(@RequestBody AnimalDataDTO animalDataDTO) {
        animalDataService.writeAnimalData(animalDataDTO);
        return new ResponseEntity<>(animalDataDTO, HttpStatus.CREATED);
    }

    @GetMapping("/latest/{animalId}/{field}")
    public ResponseEntity<Double> getLatestValue(@PathVariable String animalId, @PathVariable String field) {
        Double latestValue = animalDataService.getLatestValue(animalId, field);
        if (latestValue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se não encontrar valor
        }
        return new ResponseEntity<>(latestValue, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Endpoint is working!");
    }
}
