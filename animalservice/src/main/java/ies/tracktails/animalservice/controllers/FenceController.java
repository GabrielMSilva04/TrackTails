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
import jakarta.persistence.EntityNotFoundException;
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
    @Operation(summary = "Get fence by animal ID",
            description = "Fetch the fence details for a specific animal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fence data retrieved successfully", content = @Content(schema = @Schema(implementation = FenceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fence not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{animalId}")
    public ResponseEntity<?> getFenceByAnimalId(@PathVariable Long animalId) {
        try {
            FenceDTO fenceDTO = fenceService.getFenceByAnimalId(animalId);
            if (fenceDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Fence not found for animalId: " + animalId));
            }
            return ResponseEntity.ok(fenceDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch fence: " + e.getMessage()));
        }
    }

    // Delete the fence by animal ID
    @Operation(summary = "Delete fence by animal ID",
            description = "Delete the geofence details for a specific animal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fence deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Fence not found"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{animalId}")
    public ResponseEntity<?> deleteFence(@PathVariable Long animalId) {
        try {
            // Call the service to delete the fence
            fenceService.deleteFence(animalId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Fence deleted successfully"));
        } catch (EntityNotFoundException e) {
            // Handle case when the fence doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Handle generic server-side errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete fence: " + e.getMessage()));
        }
    }

    @Operation(summary = "Check if a point is inside the fence",
            description = "Checks whether a given latitude and longitude point is inside the fence of a specified animal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Point check completed successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Fence not found for the animal"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping("/{animalId}/isInside")
    public ResponseEntity<Map<String, Object>> isPointInsideFence(
            @PathVariable Long animalId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        try {
            boolean isInside = fenceService.isInsideFence(animalId, latitude, longitude);
            return ResponseEntity.ok(Map.of(
                    "animalId", animalId,
                    "latitude", latitude,
                    "longitude", longitude,
                    "isInside", isInside
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check point: " + e.getMessage()));
        }
    }
}
