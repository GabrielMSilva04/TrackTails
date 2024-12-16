package ies.tracktails.reportservice.services.impl;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.reportservice.dtos.TableData;
import ies.tracktails.reportservice.dtos.TableRow;
import ies.tracktails.reportservice.entities.Report;
import ies.tracktails.reportservice.repositories.ReportRepository;
import ies.tracktails.reportservice.services.ReportService;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itextpdf.html2pdf.HtmlConverter;

import java.time.Instant;
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
    private final TemplateEngine templateEngine;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, AnimalDataService animalDataService, TemplateEngine templateEngine) {
        this.reportRepository = reportRepository;
        this.animalDataService = animalDataService;
        this.templateEngine = templateEngine;
    }

    @Override
    public Report createReport(Long animalId, String fileName, String start, String end, String interval, String metrics) {
        try {
            List<String> metricsList;
            if (metrics.equals("all")) {
                metricsList = Arrays.asList("weight", "height", "heartRate", "breathRate", "speed");
            } else {
                metricsList = Arrays.asList(metrics.split(","));
            }
            // Generate the PDF
            byte[] pdfBytes = generatePdfFromTemplate(animalId, start, end, interval, metricsList);

            Report report = new Report();
            report.setAnimalId(animalId);
            report.setFileName(fileName);
            report.setFileContent(pdfBytes);

            return reportRepository.save(report);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report PDF", e);
        }
    }

    private byte[] generatePdfFromTemplate(Long animalId, String start, String end, String interval, List<String> metrics) throws Exception {
        Context context = new Context();

        // Replace placeholders with data
        populateTemplate(context, animalId, start, end, interval, metrics);

        String populatedHtml = templateEngine.process("animal-report", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(populatedHtml, outputStream);
            return outputStream.toByteArray();
        }
    }


    private void populateTemplate(Context context, Long animalId, String start, String end, String interval, List<String> metrics) {
        context.setVariable("generatedAt", Instant.now().toString());
        
        List<TableData> tables = createTables(animalId, start, end, interval, metrics);
        context.setVariable("tables", tables);
    }

    private List<TableData> createTables(Long animalId, String start, String end, String interval, List<String> metrics) {
        List<TableData> tables = new ArrayList<>();
        
        for (String metric : metrics) {
            System.out.println("Getting data for metric " + metric + " for animal " + animalId + " from " + start + " to " + end + " with interval " + interval);
            List<AnimalDataDTO> animalDataDTOs_mean = animalDataService.getRangeValues(
                    animalId.toString(), metric, start, end, interval, "mean");

            if (animalDataDTOs_mean == null) {
                System.out.println("No data found for metric " + metric);
                continue;
            }

            TableData tableData = new TableData();
            String title = convertCamelCaseToTitleCase(metric);
            tableData.setTableTitle("Data for " + title);
            tableData.setColumnTitle("Mean " + convertCamelCaseToTitleCase(title));  // Título da coluna

            List<TableRow> rows = new ArrayList<>();
            for (AnimalDataDTO data : animalDataDTOs_mean) {
                TableRow row = new TableRow();
                row.setTimestamp(data.getTimestamp().isPresent() ? data.getTimestamp().get().toString() : "N/A");
                String value = data.getField(metric);
                // if is a number, round it
                try {
                    Double numberValue = Double.parseDouble(value);
                    value = round(numberValue).toString();
                } catch (NumberFormatException e) { /* Do nothing */ }
                row.setDynamicValue(value);
                rows.add(row);
            }

            tableData.setRows(rows);
            tables.add(tableData);
        }

        return tables;
    }

    private String convertCamelCaseToTitleCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder sb = new StringBuilder();
        
        sb.append(Character.toUpperCase(camelCase.charAt(0)));
        for (char c : camelCase.substring(1).toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(" ");
            }
            sb.append(c);
        }

        return sb.toString().trim();
    }

    private Double round(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }



    @Override
    public Report getReport(UUID id) {
        return reportRepository.findById(id).orElse(null);
    }

    @Override
    public List<Report> getReportsByAnimalId(Long animalId) {
        return reportRepository.findByAnimalId(animalId);
    }
}
