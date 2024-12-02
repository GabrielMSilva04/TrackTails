package ies.tracktails.animalservice.dtos;

import ies.tracktails.animalservice.entities.Fence;

public class FenceDTO {
    private Long animalId;
    private Double point1Latitude;
    private Double point1Longitude;
    private Double point2Latitude;
    private Double point2Longitude;
    private Double point3Latitude;
    private Double point3Longitude;
    private Double point4Latitude;
    private Double point4Longitude;

    public FenceDTO() {}

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

    public FenceDTO(Fence fence) {
        this.animalId = fence.getAnimalId();
        this.point1Latitude = fence.getPoint1Latitude();
        this.point1Longitude = fence.getPoint1Longitude();
        this.point2Latitude = fence.getPoint2Latitude();
        this.point2Longitude = fence.getPoint2Longitude();
        this.point3Latitude = fence.getPoint3Latitude();
        this.point3Longitude = fence.getPoint3Longitude();
        this.point4Latitude = fence.getPoint4Latitude();
        this.point4Longitude = fence.getPoint4Longitude();
    }

    public FenceDTO(FenceDTO fenceDTO) {
        this.animalId = fenceDTO.getAnimalId();
        this.point1Latitude = fenceDTO.getPoint1Latitude();
        this.point1Longitude = fenceDTO.getPoint1Longitude();
        this.point2Latitude = fenceDTO.getPoint2Latitude();
        this.point2Longitude = fenceDTO.getPoint2Longitude();
        this.point3Latitude = fenceDTO.getPoint3Latitude();
        this.point3Longitude = fenceDTO.getPoint3Longitude();
        this.point4Latitude = fenceDTO.getPoint4Latitude();
        this.point4Longitude = fenceDTO.getPoint4Longitude();
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
