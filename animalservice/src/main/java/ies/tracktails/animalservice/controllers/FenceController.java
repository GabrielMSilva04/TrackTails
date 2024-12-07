package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalservice.services.FenceService;
import ies.tracktails.animalservice.dtos.FenceDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/fences")
public class FenceController {

    private final FenceService fenceService;

    @Autowired
    public FenceController(FenceService fenceService) {
        this.fenceService = fenceService;
    }

    @PostMapping
    public ResponseEntity<?> addOrUpdateFence(@RequestBody @Valid FenceDTO fenceDTO) {
        try {
            fenceService.addOrUpdateFence(fenceDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Fence created/updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid input data: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process request: " + e.getMessage()));
        }
    }

    // Get the fence by animal ID
    @GetMapping("/{animalId}")
    public ResponseEntity<FenceDTO> getFenceByAnimalId(@PathVariable Long animalId) {
        try {
            FenceDTO fenceDTO = fenceService.getFenceByAnimalId(animalId);
            if (fenceDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(fenceDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete the fence by animal ID
    @DeleteMapping("/{animalId}")
    public ResponseEntity<String> deleteFence(@PathVariable Long animalId) {
        try {
            fenceService.deleteFence(animalId);
            return new ResponseEntity<>("Fence deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete fence", HttpStatus.BAD_REQUEST);
        }
    }
}
