package ies.tracktails.animalservice.dtos;

import java.util.Optional;

public class FenceDTO {
    private Long animalId;
    private Optional<Double> point1Latitude;
    private Optional<Double> point1Longitude;
    private Optional<Double> point2Latitude;
    private Optional<Double> point2Longitude;
    private Optional<Double> point3Latitude;
    private Optional<Double> point3Longitude;
    private Optional<Double> point4Latitude;
    private Optional<Double> point4Longitude;

    public FenceDTO(Long animalId, Double point1Latitude, Double point1Longitude, Double point2Latitude, Double point2Longitude, Double point3Latitude, Double point3Longitude, Double point4Latitude, Double point4Longitude) {
        this.animalId = animalId;
        this.point1Latitude = Optional.ofNullable(point1Latitude);
        this.point1Longitude = Optional.ofNullable(point1Longitude);
        this.point2Latitude = Optional.ofNullable(point2Latitude);
        this.point2Longitude = Optional.ofNullable(point2Longitude);
        this.point3Latitude = Optional.ofNullable(point3Latitude);
        this.point3Longitude = Optional.ofNullable(point3Longitude);
        this.point4Latitude = Optional.ofNullable(point4Latitude);
        this.point4Longitude = Optional.ofNullable(point4Longitude);
    }

    public FenceDTO(Long animalId) {
        this.animalId = animalId;
        this.point1Latitude = Optional.empty();
        this.point1Longitude = Optional.empty();
        this.point2Latitude = Optional.empty();
        this.point2Longitude = Optional.empty();
        this.point3Latitude = Optional.empty();
        this.point3Longitude = Optional.empty();
        this.point4Latitude = Optional.empty();
        this.point4Longitude = Optional.empty();
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Optional<Double> getPoint1Latitude() {
        return point1Latitude;
    }

    public void setPoint1Latitude(Double point1Latitude) {
        this.point1Latitude = Optional.ofNullable(point1Latitude);
    }

    public Optional<Double> getPoint1Longitude() {
        return point1Longitude;
    }

    public void setPoint1Longitude(Double point1Longitude) {
        this.point1Longitude = Optional.ofNullable(point1Longitude);
    }

    public Optional<Double> getPoint2Latitude() {
        return point2Latitude;
    }

    public void setPoint2Latitude(Double point2Latitude) {
        this.point2Latitude = Optional.ofNullable(point2Latitude);
    }

    public Optional<Double> getPoint2Longitude() {
        return point2Longitude;
    }

    public void setPoint2Longitude(Double point2Longitude) {
        this.point2Longitude = Optional.ofNullable(point2Longitude);
    }

    public Optional<Double> getPoint3Latitude() {
        return point3Latitude;
    }

    public void setPoint3Latitude(Double point3Latitude) {
        this.point3Latitude = Optional.ofNullable(point3Latitude);
    }

    public Optional<Double> getPoint3Longitude() {
        return point3Longitude;
    }

    public void setPoint3Longitude(Double point3Longitude) {
        this.point3Longitude = Optional.ofNullable(point3Longitude);
    }

    public Optional<Double> getPoint4Latitude() {
        return point4Latitude;
    }

    public void setPoint4Latitude(Double point4Latitude) {
        this.point4Latitude = Optional.ofNullable(point4Latitude);
    }

    public Optional<Double> getPoint4Longitude() {
        return point4Longitude;
    }

    public void setPoint4Longitude(Double point4Longitude) {
        this.point4Longitude = Optional.ofNullable(point4Longitude);
    }
}
