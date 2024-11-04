package ies.tracktails.reportservice.controller;

import ies.tracktails.reportservice.entity.Report;
import ies.tracktails.reportservice.service.ReportService;
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

