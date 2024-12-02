package ies.tracktails.reportservice.services.impl;

import ies.tracktails.animalsDataCore.services.AnimalDataService;
import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
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
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final AnimalDataService animalDataService;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, AnimalDataService animalDataService) {
        this.reportRepository = reportRepository;
        this.animalDataService = animalDataService;
    }

    @Override
    public Report createReport(Long animalId, String fileName) {
        try {
            // Fetch the latest data for the given animal ID
            AnimalDataDTO latestData = animalDataService.getLatestValues(animalId.toString());
            if (latestData == null) {
                throw new RuntimeException("No data found for animal ID: " + animalId);
            }

            // Generate PDF content using the fetched data
            byte[] pdfBytes = generatePdfContent(latestData);

            // Create and save the report entity
            Report report = new Report();
            report.setAnimalId(animalId);
            report.setFileName(fileName);
            report.setFileContent(pdfBytes);
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
    public List<Report> getReportsByAnimalId(Long animalId) {
        return reportRepository.findByAnimalId(animalId);
    }

    private byte[] generatePdfContent(AnimalDataDTO latestData) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add general information
            document.add(new Paragraph("Animal Report"));
            document.add(new Paragraph("Animal ID: " + latestData.getAnimalId()));
            document.add(new Paragraph("Timestamp: " + latestData.getTimestamp().orElse(null)));

            // Add optional fields if present
            latestData.getWeight().ifPresent(weight -> document.add(new Paragraph("Weight: " + weight + " kg")));
            latestData.getHeight().ifPresent(height -> document.add(new Paragraph("Height: " + height + " m")));
            latestData.getLatitude().ifPresent(lat -> document.add(new Paragraph("Latitude: " + lat)));
            latestData.getLongitude().ifPresent(lon -> document.add(new Paragraph("Longitude: " + lon)));
            latestData.getSpeed().ifPresent(speed -> document.add(new Paragraph("Speed: " + speed + " m/s")));
            latestData.getHeartRate().ifPresent(hr -> document.add(new Paragraph("Heart Rate: " + hr + " bpm")));
            latestData.getBreathRate().ifPresent(br -> document.add(new Paragraph("Breath Rate: " + br + " breaths/min")));
            latestData.getBatteryPercentage().ifPresent(battery -> document.add(new Paragraph("Battery Percentage: " + battery + "%")));

            // Add additional tags
            if (!latestData.getAdditionalTags().isEmpty()) {
                document.add(new Paragraph("Additional Information:"));
                latestData.getAdditionalTags().forEach((key, value) -> {
                    document.add(new Paragraph(" - " + key + ": " + value));
                });
            }

            document.close();
            return outputStream.toByteArray();
        }
    }
}
