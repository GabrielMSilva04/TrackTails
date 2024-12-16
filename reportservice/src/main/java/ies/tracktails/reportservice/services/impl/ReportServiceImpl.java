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

        // Convert the populated HTML to PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(populatedHtml, outputStream);
            return outputStream.toByteArray();
        }
    }


    private void populateTemplate(Context context, Long animalId, String start, String end, String interval, List<String> metrics) {
        // Preencher o contexto com os dados básicos
        context.setVariable("generatedAt", Instant.now().toString());
        
        // Cria uma estrutura de tabelas
        List<TableData> tables = createTables(animalId, start, end, interval, metrics);
        context.setVariable("tables", tables);
    }

    private List<TableData> createTables(Long animalId, String start, String end, String interval, List<String> metrics) {
        // Aqui criamos uma tabela exemplo com o título e as linhas
        // Para simplificar, vamos assumir que cada métrica é uma tabela
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
            tableData.setTableTitle("Data for " + metric);  // Título da tabela
            tableData.setColumnTitle(metric); // Título da coluna

            // Cria linhas de exemplo com timestamp e valores dinâmicos
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

    private Double round(Double value) {
        return Math.round(value * 100.0) / 100.0;
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
