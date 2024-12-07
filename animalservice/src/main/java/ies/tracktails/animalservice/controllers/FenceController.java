package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalservice.services.FenceService;
import ies.tracktails.animalservice.dtos.FenceDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fences")
public class FenceController {

    private final FenceService fenceService;

    @Autowired
    public FenceController(FenceService fenceService) {
        this.fenceService = fenceService;
    }

    // Create or update fence
    @PostMapping
    public ResponseEntity<String> addOrUpdateFence(@RequestBody FenceDTO fenceDTO) {
        try {
            fenceService.addOrUpdateFence(fenceDTO);
            return new ResponseEntity<>("Fence created/updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create/update fence", HttpStatus.BAD_REQUEST);
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
