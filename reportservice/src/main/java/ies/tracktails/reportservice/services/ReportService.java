package ies.tracktails.reportservice.services;

import ies.tracktails.reportservice.entities.Report;

import java.util.Optional;

public interface ReportService {
    Report createReport(Long animalId, String fileName);

    Report getReport(Long id);
}
