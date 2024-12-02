package ies.tracktails.reportservice.services.impl;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.repositories.ReportRepository;
import ies.tracktails.reportservice.services.ReportService;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.html2pdf.HtmlConverter;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileNotFoundException;

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
    public Report createReport(Long animalId, String fileName, String start, String end, String interval) {
        try {
            // Fetch historical data for each field
            List<AnimalDataDTO> weightData = animalDataService.getRangeValues(
                    animalId.toString(), "weight", start, end, interval, "mean");
            List<AnimalDataDTO> heightData = animalDataService.getRangeValues(
                    animalId.toString(), "height", start, end, interval, "mean");
            List<AnimalDataDTO> heartRateData = animalDataService.getRangeValues(
                    animalId.toString(), "heartRate", start, end, interval, "mean");
            List<AnimalDataDTO> breathRateData = animalDataService.getRangeValues(
                    animalId.toString(), "breathRate", start, end, interval, "mean");

            List<AnimalDataDTO> consolidatedData = consolidateData(
                    weightData, heightData, heartRateData, breathRateData);

            // Generate the PDF
            byte[] pdfBytes = generatePdfFromTemplate(animalId, consolidatedData);

            Report report = new Report();
            report.setAnimalId(animalId);
            report.setFileName(fileName);
            report.setFileContent(pdfBytes);

            return reportRepository.save(report);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report PDF", e);
        }
    }

    private String loadHtmlTemplate() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/animal-report.html")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Template file not found: /templates/animal-report.html");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private byte[] generatePdfFromTemplate(Long animalId, List<AnimalDataDTO> historicalData) throws Exception {
        String htmlTemplate = loadHtmlTemplate();

        // Replace placeholders with data
        String populatedHtml = populateTemplate(htmlTemplate, animalId, historicalData);

        // Convert the populated HTML to PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(populatedHtml, outputStream);
            return outputStream.toByteArray();
        }
    }


    private String populateTemplate(String htmlTemplate, Long animalId, List<AnimalDataDTO> historicalData) {
        // Replace static placeholders
        String populatedHtml = htmlTemplate
                .replace("{{animalId}}", animalId.toString())
                .replace("{{generatedAt}}", Instant.now().toString());

        StringBuilder historicalDataHtml = new StringBuilder();
        for (AnimalDataDTO data : historicalData) {
            historicalDataHtml.append("<tr>")
                    .append("<td>").append(data.getTimestamp().map(Instant::toString).orElse("N/A")).append("</td>")
                    .append("<td>").append(data.getWeight().orElse(null)).append("</td>")
                    .append("<td>").append(data.getHeight().orElse(null)).append("</td>")
                    .append("<td>").append(data.getHeartRate().orElse(null)).append("</td>")
                    .append("<td>").append(data.getBreathRate().orElse(null)).append("</td>")
                    .append("</tr>");
        }

        // Replace the historical data placeholder in the template
        populatedHtml = populatedHtml.replace("{{historicalData}}", historicalDataHtml.toString());

        return populatedHtml;
    }

    private List<AnimalDataDTO> consolidateData(List<AnimalDataDTO>... dataLists) {
        Map<Instant, AnimalDataDTO> consolidatedMap = new TreeMap<>();

        for (List<AnimalDataDTO> dataList : dataLists) {
            for (AnimalDataDTO data : dataList) {
                if (data.getTimestamp().isPresent()) {
                    Instant timestamp = data.getTimestamp().get();

                    AnimalDataDTO existingData = consolidatedMap.getOrDefault(
                            timestamp, new AnimalDataDTO(data.getAnimalId())
                    );

                    existingData.setTimestamp(timestamp);

                    data.getWeight().ifPresent(existingData::setWeight);
                    data.getHeight().ifPresent(existingData::setHeight);
                    data.getHeartRate().ifPresent(existingData::setHeartRate);
                    data.getBreathRate().ifPresent(existingData::setBreathRate);

                    consolidatedMap.put(timestamp, existingData);
                }
            }
        }
        return new ArrayList<>(consolidatedMap.values());
    }


    private void addDataToMap(List<AnimalDataDTO> dataList, Map<Instant, AnimalDataDTO> map, String field) {
        if (dataList == null) return;

        for (AnimalDataDTO data : dataList) {
            Instant timestamp = data.getTimestamp().orElse(null);
            if (timestamp != null) {
                map.putIfAbsent(timestamp, new AnimalDataDTO(data.getAnimalId()));
                map.get(timestamp).addField(field, data.getWeight().orElse(null).toString());
                map.get(timestamp).setTimestamp(timestamp);
            }
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
}
