package ies.tracktails.reportservice.controllers;

import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("Hello from Report Service", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody  Report report) {
        Report savedReport = reportService.createReport(report.getAnimalId(), report.getFileName());
        if (savedReport == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}

