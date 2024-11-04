package ies.tracktails.reportservice.services.impl;

import ies.tracktails.reportservice.services.ReportService;
import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report createReport(Long animalId, String fileName) {
        Report report = new Report();
        report.setAnimalId(animalId);
        report.setFileName(fileName);
        return reportRepository.save(report);
    }

    public Report getReport(Long id) {
        return reportRepository.findById(id).isPresent() ? reportRepository.findById(id).get() : null;
    }

    public Report getReportByAnimalId(Long animalId) {
        return reportRepository.findByAnimalId(animalId);
    }
}

