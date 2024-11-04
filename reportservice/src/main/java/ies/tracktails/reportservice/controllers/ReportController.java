package ies.tracktails.reportservice.controllers;

import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.services.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Create a new report")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Report.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Animal not found")
//    })
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody  Report report) {
        Report savedReport = reportService.createReport(report.getAnimalId(), report.getFileName());
        if (savedReport == null) {
            return new ResponseEntity<>(new ErrorResponse("Animal not found", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a report by id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Report.class))),
//            @ApiResponse(responseCode = "404", description = "Report not found")
//    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(report, HttpStatus.OK);
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

