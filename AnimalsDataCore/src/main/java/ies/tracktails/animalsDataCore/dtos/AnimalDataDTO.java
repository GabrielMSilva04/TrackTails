package ies.tracktails.animalsDataCore.dtos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnimalDataDTO {
    private String animalId;
    private Optional<Double> weight;
    private Optional<Double> height;
    private Optional<Double> latitude;
    private Optional<Double> longitude;
    private Optional<Double> speed;
    private Optional<Double> heartRate;
    private Optional<Double> breathRate;
    private Map<String, String> additionalTags;

    public AnimalDataDTO() {
        this.weight = Optional.empty();
        this.height = Optional.empty();
        this.latitude = Optional.empty();
        this.longitude = Optional.empty();
        this.speed = Optional.empty();
        this.heartRate = Optional.empty();
        this.breathRate = Optional.empty();
        this.additionalTags = new HashMap<>();
    }

    public AnimalDataDTO(String animalId) {
        this.animalId = animalId;
        this.weight = Optional.empty();
        this.height = Optional.empty();
        this.latitude = Optional.empty();
        this.longitude = Optional.empty();
        this.speed = Optional.empty();
        this.heartRate = Optional.empty();
        this.breathRate = Optional.empty();
        this.additionalTags = new HashMap<>();
    }

    public AnimalDataDTO(String animalId, Double weight, Double height, Double latitude, Double longitude, Double speed, Double heartRate, Double breathRate) {
        this.animalId = animalId;
        this.weight = Optional.ofNullable(weight);
        this.height = Optional.ofNullable(height);
        this.latitude = Optional.ofNullable(latitude);
        this.longitude = Optional.ofNullable(longitude);
        this.speed = Optional.ofNullable(speed);
        this.heartRate = Optional.ofNullable(heartRate);
        this.breathRate = Optional.ofNullable(breathRate);
        this.additionalTags = new HashMap<>();
    }

    // Getters e Setters
    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public Optional<Double> getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = Optional.ofNullable(weight); // Armazena o peso, pode ser nulo
    }

    public Optional<Double> getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = Optional.ofNullable(height);
    }

    public Optional<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = Optional.ofNullable(latitude);
    }

    public Optional<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = Optional.ofNullable(longitude);
    }

    public Optional<Double> getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = Optional.ofNullable(speed);
    }

    public Optional<Double> getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Double heartRate) {
        this.heartRate = Optional.ofNullable(heartRate);
    }

    public Optional<Double> getBreathRate() {
        return breathRate;
    }

    public void setBreathRate(Double breathRate) {
        this.breathRate = Optional.ofNullable(breathRate);
    }

    public Map<String, String> getAdditionalTags() {
        return additionalTags;
    }

    public void addAdditionalTag(String key, String value) {
        this.additionalTags.put(key, value);
    }
}
