package ies.tracktails.animalservice.entities;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Instant;

@Entity
public class Fence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long animalId;

    @Column(nullable = false)
    private Double point1Latitude;

    @Column(nullable = false)
    private Double point1Longitude;

    @Column(nullable = false)
    private Double point2Latitude;

    @Column(nullable = false)
    private Double point2Longitude;

    @Column(nullable = false)
    private Double point3Latitude;

    @Column(nullable = false)
    private Double point3Longitude;

    @Column(nullable = false)
    private Double point4Latitude;

    @Column(nullable = false)
    private Double point4Longitude;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    // Default constructor
    public Fence() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Double getPoint1Latitude() {
        return point1Latitude;
    }

    public void setPoint1Latitude(Double point1Latitude) {
        this.point1Latitude = point1Latitude;
    }

    public Double getPoint1Longitude() {
        return point1Longitude;
    }

    public void setPoint1Longitude(Double point1Longitude) {
        this.point1Longitude = point1Longitude;
    }

    public Double getPoint2Latitude() {
        return point2Latitude;
    }

    public void setPoint2Latitude(Double point2Latitude) {
        this.point2Latitude = point2Latitude;
    }

    public Double getPoint2Longitude() {
        return point2Longitude;
    }

    public void setPoint2Longitude(Double point2Longitude) {
        this.point2Longitude = point2Longitude;
    }

    public Double getPoint3Latitude() {
        return point3Latitude;
    }

    public void setPoint3Latitude(Double point3Latitude) {
        this.point3Latitude = point3Latitude;
    }

    public Double getPoint3Longitude() {
        return point3Longitude;
    }

    public void setPoint3Longitude(Double point3Longitude) {
        this.point3Longitude = point3Longitude;
    }

    public Double getPoint4Latitude() {
        return point4Latitude;
    }

    public void setPoint4Latitude(Double point4Latitude) {
        this.point4Latitude = point4Latitude;
    }

    public Double getPoint4Longitude() {
        return point4Longitude;
    }

    public void setPoint4Longitude(Double point4Longitude) {
        this.point4Longitude = point4Longitude;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString
    @Override
    public String toString() {
        return "Fence{" +
                "id=" + id +
                ", animalId=" + animalId +
                ", point1Latitude=" + point1Latitude +
                ", point1Longitude=" + point1Longitude +
                ", point2Latitude=" + point2Latitude +
                ", point2Longitude=" + point2Longitude +
                ", point3Latitude=" + point3Latitude +
                ", point3Longitude=" + point3Longitude +
                ", point4Latitude=" + point4Latitude +
                ", point4Longitude=" + point4Longitude +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
