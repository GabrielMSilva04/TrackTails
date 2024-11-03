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

    @GetMapping("/latest/{animalId}")
    public ResponseEntity<AnimalDataDTO> getLatestValues(@PathVariable String animalId) {
        AnimalDataDTO latestValues = animalDataService.getLatestValues(animalId);
        if (latestValues == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se não encontrar valor
        }
        return new ResponseEntity<>(latestValues, HttpStatus.OK);
    }

    @GetMapping("/range/{animalId}/{field}")
    public ResponseEntity<?> getRangeValues(
        @PathVariable String animalId,
        @PathVariable String field,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) String interval) {
        return new ResponseEntity<>(animalDataService.getRangeValues(animalId, field, start, end, interval), HttpStatus.OK);
    }
}
