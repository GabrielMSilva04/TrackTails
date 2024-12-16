package ies.tracktails.animalservice.controllers;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/v1/animaldata")
public class AnimalDataController {
    private final AnimalDataService animalDataService;
    private final Set<String> validAggregateFunctions = new HashSet<>();

    @Autowired
    public AnimalDataController(AnimalDataService animalDataService) {
        this.animalDataService = animalDataService;

        validAggregateFunctions.add("mean");
        validAggregateFunctions.add("median");
        validAggregateFunctions.add("max");
        validAggregateFunctions.add("min");
        validAggregateFunctions.add("sum");
        validAggregateFunctions.add("last");
        validAggregateFunctions.add("first");
        validAggregateFunctions.add("stddev");
    }

    @Operation(summary = "Create animal data",
            description = "Create animal data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Data created successfully", content = @Content(schema = @Schema(implementation = AnimalDataDTO.class)))
    })
    @PostMapping
    public ResponseEntity<AnimalDataDTO> createAnimalData(@RequestBody AnimalDataDTO animalDataDTO) {
        animalDataService.writeAnimalData(animalDataDTO);
        return new ResponseEntity<>(animalDataDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get latest data for a specific animal and field",
            description = "Get latest data for a specific animal and field")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Data retrieved successfully", content = @Content(schema = @Schema(implementation = Double.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/latest/{animalId}/{field}")
    public ResponseEntity<?> getLatestValue(@PathVariable String animalId, @PathVariable String field) {
        AnimalDataDTO latestValue = animalDataService.getLatestValue(animalId, field);
        if (latestValue == null) {
            return new ResponseEntity<>(new ErrorResponse("Data not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(latestValue, HttpStatus.OK);
    }

    @Operation(summary = "Get latest data for a specific animal",
            description = "Get latest data on all fields for a specific animal")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Data retrieved successfully", content = @Content(schema = @Schema(implementation = AnimalDataDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/latest/{animalId}")
    public ResponseEntity<?> getLatestValues(@PathVariable String animalId) {
        AnimalDataDTO latestValues = animalDataService.getLatestValues(animalId);
        if (latestValues == null) {
            return new ResponseEntity<>(new ErrorResponse("Data not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(latestValues, HttpStatus.OK);
    }

    @Operation(summary = "Get historic data for a specific animal and field",
            description = "Get historic data for a specific animal and field in a given time range")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Data retrieved successfully", content = @Content(schema = @Schema(implementation = AnimalDataDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid aggregate function", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/historic/{animalId}/{field}")
    public ResponseEntity<?> getRangeValues(
        @PathVariable String animalId,
        @PathVariable String field,
        @RequestParam(defaultValue = "-1d") String start,
        @RequestParam(defaultValue = "now()") String end,
        @RequestParam(defaultValue = "15m") String interval,
        @Schema(allowableValues = {"mean", "median", "max", "min", "sum", "last", "first", "stddev"})
        @RequestParam(defaultValue = "last") String aggregate) {

        if (!validAggregateFunctions.contains(aggregate)) {
            return new ResponseEntity<>(new ErrorResponse("Invalid aggregate function", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        List<AnimalDataDTO> data = animalDataService.getRangeValues(animalId, field, start, end, interval, aggregate);
        if (data == null) {
            return new ResponseEntity<>(new ErrorResponse("Data not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @Operation(summary = "Get sleep duration for today",
            description = "Retrieve the total sleep duration for a specific animal on the current day")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sleep duration retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Animal data not found")
    })
    @GetMapping("/sleep/{animalId}/today")
    public ResponseEntity<?> getSleepDurationToday(@PathVariable String animalId) {
        long sleepDuration = animalDataService.getSleepDurationToday(animalId);
        return ResponseEntity.ok(Map.of("animalId", animalId, "sleepDurationMinutes", sleepDuration));
    }
}

class ErrorResponse {
    private String message;
    private HttpStatus status;
    private int code;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
