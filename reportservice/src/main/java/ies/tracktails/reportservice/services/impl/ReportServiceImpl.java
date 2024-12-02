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

            // Combine data into a consolidated list
            List<AnimalDataDTO> consolidatedData = consolidateData(
                    weightData, heightData, heartRateData, breathRateData);

            // Generate the PDF
            byte[] pdfBytes = generatePdfFromTemplate(animalId, consolidatedData);

            // Save the report
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
        // Load the HTML template
        String htmlTemplate = loadHtmlTemplate();

        // Replace placeholders with actual data
        String populatedHtml = populateTemplate(htmlTemplate, animalId, historicalData);

        // Convert the populated HTML to PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(populatedHtml, outputStream);
            return outputStream.toByteArray();
        }
    }


    private String populateTemplate(String template, Long animalId, List<AnimalDataDTO> historicalData) {
        // Replace basic placeholders
        String result = template.replace("{{animalId}}", animalId.toString())
                .replace("{{generatedAt}}", java.time.Instant.now().toString());

        // Generate table rows for historical data
        String historicalDataRows = historicalData.stream()
                .sorted(Comparator.comparing(a -> a.getTimestamp().orElse(java.time.Instant.EPOCH)))
                .map(data -> String.format(
                        "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        data.getTimestamp().orElse(null),
                        data.getWeight().orElse(null),
                        data.getHeight().orElse(null),
                        data.getHeartRate().orElse(null),
                        data.getBreathRate().orElse(null)
                ))
                .collect(Collectors.joining());

        // Replace the historical data placeholder
        result = result.replace("{{#historicalData}}", historicalDataRows);

        return result;
    }

    private List<AnimalDataDTO> consolidateData(List<AnimalDataDTO>... dataLists) {
        Map<Instant, AnimalDataDTO> consolidatedMap = new HashMap<>();

        for (List<AnimalDataDTO> dataList : dataLists) {
            for (AnimalDataDTO data : dataList) {
                if (data.getTimestamp().isPresent()) {
                    Instant timestamp = data.getTimestamp().get();
                    AnimalDataDTO existingData = consolidatedMap.getOrDefault(timestamp, new AnimalDataDTO(data.getAnimalId()));

                    data.getWeight().ifPresent(existingData::setWeight);
                    data.getHeight().ifPresent(existingData::setHeight);
                    data.getHeartRate().ifPresent(existingData::setHeartRate);
                    data.getBreathRate().ifPresent(existingData::setBreathRate);

                    consolidatedMap.put(timestamp, existingData);
                }
            }
        }

        // Convert the consolidated map into a sorted list of AnimalDataDTO
        return consolidatedMap.values().stream()
                .sorted((a, b) -> a.getTimestamp().orElse(Instant.EPOCH)
                        .compareTo(b.getTimestamp().orElse(Instant.EPOCH)))
                .collect(Collectors.toList());
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
