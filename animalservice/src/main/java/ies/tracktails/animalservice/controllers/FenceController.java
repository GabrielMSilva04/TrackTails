package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.services.FenceService;
import ies.tracktails.animalsDataCore.dtos.FenceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
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

    @Operation(
        summary = "Create or update a fence",
        description = "Creates or updates a fence associated with the specified animal."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Fence created/updated successfully",
            content = @Content(schema = @Schema(example = "{\"message\": \"Fence created/updated successfully\"}"))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(example = "{\"error\": \"Invalid input data: <details>\"}"))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Failed to process request",
            content = @Content(schema = @Schema(example = "{\"error\": \"Failed to process request: <details>\"}"))
        )
    })
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
    @Operation(
        summary = "Get a fence by animal ID",
        description = "Fetches the fence details for the specified animal ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fence found",
            content = @Content(schema = @Schema(implementation = FenceDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Fence not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Failed to process request",
            content = @Content
        )
    })
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
    @Operation(
        summary = "Delete a fence by animal ID",
        description = "Deletes the fence associated with the specified animal ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fence deleted successfully",
            content = @Content(schema = @Schema(example = "Fence deleted successfully"))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to delete fence",
            content = @Content(schema = @Schema(example = "Failed to delete fence"))
        )
    })
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
