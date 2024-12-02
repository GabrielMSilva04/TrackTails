package ies.tracktails.reportservice.services;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.reportservice.entities.Report;

import java.util.List;

public interface ReportService {
    Report createReport(Long animalId, String fileName, String start, String end, String interval);
    Report getReport(Long id);
    List<Report> getReportsByAnimalId(Long animalId);
}
