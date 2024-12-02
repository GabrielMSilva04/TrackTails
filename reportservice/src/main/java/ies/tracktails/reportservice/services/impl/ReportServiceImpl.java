package ies.tracktails.reportservice.services.impl;

import ies.tracktails.reportservice.services.ReportService;
import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.repositories.ReportRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report createReport(Long animalId, String fileName) {
        try {
            // Generate PDF content
            byte[] pdfBytes = generatePdfContent("Report for Animal ID: " + animalId);

            // Create and save the report entity
            Report report = new Report();
            report.setAnimalId(animalId);
            report.setFileName(fileName);
            report.setFileContent(pdfBytes); // Save PDF bytes
            return reportRepository.save(report);

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate report PDF", e);
        }
    }

    @Override
    public Report getReport(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    @Override
    public Report getReportByAnimalId(Long animalId) {
        return reportRepository.findByAnimalId(animalId);
    }

    private byte[] generatePdfContent(String content) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(content));
            document.close();

            return outputStream.toByteArray();
        }
    }
}
