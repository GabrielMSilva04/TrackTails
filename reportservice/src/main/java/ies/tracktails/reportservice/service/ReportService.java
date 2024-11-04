package ies.tracktails.reportservice.service;

import ies.tracktails.reportservice.entity.Report;

import java.util.Optional;

public interface ReportService {
    Report createReport(Long animalId, String fileName);

    Optional<Report> getReport(Long id);
}
