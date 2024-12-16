package ies.tracktails.reportservice.services;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.reportservice.entities.Report;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    Report createReport(Long animalId, String fileName, String start, String end, String interval, String metrics);
    Report getReport(UUID id);
    List<Report> getReportsByAnimalId(Long animalId);
}
