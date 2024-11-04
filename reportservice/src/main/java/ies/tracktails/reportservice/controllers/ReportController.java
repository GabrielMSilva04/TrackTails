package ies.tracktails.reportservice.controllers;

import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/create")
    public Report createReport(@RequestBody  Report report) {
        return reportService.createReport(report.getAnimalId(), report.getFileName());
    }

    @GetMapping("/{id}")
    public Report getReport(@PathVariable Long id) {
        return reportService.getReport(id).orElse(null);
    }

}

