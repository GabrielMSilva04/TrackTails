package ies.tracktails.reportservice.services;

import ies.tracktails.reportservice.entities.Report;

public interface ReportService {
    Report createReport(Long animalId, String fileName);
    Report getReport(Long id);
    Report getReportByAnimalId(Long animalId);
}

