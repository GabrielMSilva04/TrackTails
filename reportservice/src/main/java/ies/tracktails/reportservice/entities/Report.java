package ies.tracktails.reportservice.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "animal_id", nullable = false)
    private Long animalId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Lob
    @Column(name = "file_content", columnDefinition = "MEDIUMBLOB", nullable = true)
    private byte[] fileContent;

    @PrePersist
    public void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID(); // Gerar UUID se não estiver definido
        }
    }

    public Report() {
        this.timestamp = LocalDateTime.now();
    }

    public Report(Long animalId, String fileName) {
        this.animalId = animalId;
        this.timestamp = LocalDateTime.now();
        this.fileName = fileName;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
