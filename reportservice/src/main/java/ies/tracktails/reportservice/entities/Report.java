package ies.tracktails.reportservice.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_id", nullable = false)
    private Long animalId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Lob
    @Column(name = "file_content", columnDefinition = "MEDIUMBLOB", nullable = true)
    private byte[] fileContent;

    public Report() {
        this.timestamp = LocalDateTime.now();
    }

    public Report(Long animalId, String fileName) {
        this.animalId = animalId;
        this.timestamp = LocalDateTime.now();
        this.fileName = fileName;
    }

    // Getters and Setters

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

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
