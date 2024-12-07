package ies.tracktails.animalsDataCore.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class FenceDTO {
    @NotNull(message = "Animal ID is required")
    private Long animalId;

    @NotNull(message = "Point 1 latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double point1Latitude;

    @NotNull(message = "Point 1 longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double point1Longitude;

    @NotNull(message = "Point 2 latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double point2Latitude;

    @NotNull(message = "Point 2 longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double point2Longitude;

    @NotNull(message = "Point 3 latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double point3Latitude;

    @NotNull(message = "Point 3 longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double point3Longitude;

    @NotNull(message = "Point 4 latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private Double point4Latitude;

    @NotNull(message = "Point 4 longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private Double point4Longitude;

    public FenceDTO() {
    }

    public FenceDTO(Long animalId, Double point1Latitude, Double point1Longitude, Double point2Latitude, Double point2Longitude, Double point3Latitude, Double point3Longitude, Double point4Latitude, Double point4Longitude) {
        this.animalId = animalId;
        this.point1Latitude = point1Latitude;
        this.point1Longitude = point1Longitude;
        this.point2Latitude = point2Latitude;
        this.point2Longitude = point2Longitude;
        this.point3Latitude = point3Latitude;
        this.point3Longitude = point3Longitude;
        this.point4Latitude = point4Latitude;
        this.point4Longitude = point4Longitude;
    }

    public FenceDTO(Long animalId) {
        this.animalId = animalId;
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
}
