package ies.tracktails.reportservice.controllers;

import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Create a new report as PDF")
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        Report savedReport = reportService.createReport(report.getAnimalId(), report.getFileName());
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }


    @Operation(summary = "Download a report by ID")
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);
        if (report == null || report.getFileContent() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", report.getFileName());

        return new ResponseEntity<>(report.getFileContent(), headers, HttpStatus.OK);
    }
}
