package ies.tracktails.reportservice.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name="report_tbl") //meti nome nesta tbl por uma questão de consistency
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long animalId;

    private LocalDateTime timestamp;

    private String fileName;

    public Report() {
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Report(Long animalId, LocalDateTime timestamp, String fileName){
        this.animalId = animalId;
        this.timestamp = LocalDateTime.now();
        this.fileName = fileName;
    }
}

