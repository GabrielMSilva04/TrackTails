package ies.tracktails.animalsDataCore.dtos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // To ignore empty fields instead of returning null
public class AnimalDataDTO {
    private String animalId;
    private Optional<Double> weight;
    private Optional<Double> height;
    private Optional<Double> latitude;
    private Optional<Double> longitude;
    private Optional<Double> speed;
    private Optional<Double> heartRate;
    private Optional<Double> breathRate;
    private Optional<Double> batteryPercentage;
    private Optional<Boolean> blinking;
    private Map<String, String> additionalTags;
    private Optional<Instant> timestamp;

    public AnimalDataDTO() {
        this.weight = Optional.empty();
        this.height = Optional.empty();
        this.latitude = Optional.empty();
        this.longitude = Optional.empty();
        this.speed = Optional.empty();
        this.heartRate = Optional.empty();
        this.breathRate = Optional.empty();
        this.batteryPercentage = Optional.empty();
        this.blinking = Optional.empty();
        this.additionalTags = new HashMap<>();
        this.timestamp = Optional.empty();
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
        this.batteryPercentage = Optional.empty();
        this.blinking = Optional.empty();
        this.additionalTags = new HashMap<>();
        this.timestamp = Optional.empty();
    }

    public AnimalDataDTO(String animalId, Double weight, Double height, Double latitude, Double longitude, Double speed, Double heartRate, Double breathRate, Double batteryPercentage, Boolean blinking, Instant timestamp) {
        this.animalId = animalId;
        this.weight = Optional.ofNullable(weight);
        this.height = Optional.ofNullable(height);
        this.latitude = Optional.ofNullable(latitude);
        this.longitude = Optional.ofNullable(longitude);
        this.speed = Optional.ofNullable(speed);
        this.heartRate = Optional.ofNullable(heartRate);
        this.breathRate = Optional.ofNullable(breathRate);
        this.batteryPercentage = Optional.ofNullable(batteryPercentage);
        this.blinking = Optional.ofNullable(blinking);
        this.additionalTags = new HashMap<>();
        this.timestamp = Optional.ofNullable(timestamp);
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

    public Optional<Double> getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(Double batteryPercentage) {
        this.batteryPercentage = Optional.ofNullable(batteryPercentage);
    }

    public void setBlinking(Boolean blinking) {
        this.blinking = Optional.ofNullable(blinking);
    }

    public Optional<Boolean> getBlinking() {
        return blinking;
    }

    public Optional<Instant> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = Optional.ofNullable(timestamp);
    }

    public Map<String, String> getAdditionalTags() {
        return additionalTags;
    }

    public void addAdditionalTag(String key, String value) {
        this.additionalTags.put(key, value);
    }

    public void addField(String key, String value) {
        switch (key) {
            case "weight":
                this.setWeight(Double.parseDouble(value));
                break;
            case "height":
                this.setHeight(Double.parseDouble(value));
                break;
            case "latitude":
                this.setLatitude(Double.parseDouble(value));
                break;
            case "longitude":
                this.setLongitude(Double.parseDouble(value));
                break;
            case "speed":
                this.setSpeed(Double.parseDouble(value));
                break;
            case "heartRate":
                this.setHeartRate(Double.parseDouble(value));
                break;
            case "breathRate":
                this.setBreathRate(Double.parseDouble(value));
                break;
            case "batteryPercentage":
                this.setBatteryPercentage(Double.parseDouble(value));
                break;
            case "blinking":
                this.setBlinking(Boolean.parseBoolean(value));
                break;
            default:
                break;
        }
    }

    public String getField(String key) {
        switch (key) {
            case "weight":
                return this.getWeight().map(Object::toString).orElse(null);
            case "height":
                return this.getHeight().map(Object::toString).orElse(null);
            case "latitude":
                return this.getLatitude().map(Object::toString).orElse(null);
            case "longitude":
                return this.getLongitude().map(Object::toString).orElse(null);
            case "speed":
                return this.getSpeed().map(Object::toString).orElse(null);
            case "heartRate":
                return this.getHeartRate().map(Object::toString).orElse(null);
            case "breathRate":
                return this.getBreathRate().map(Object::toString).orElse(null);
            case "batteryPercentage":
                return this.getBatteryPercentage().map(Object::toString).orElse(null);
            case "blinking":
                return this.getBlinking().map(Object::toString).orElse(null);
            default:
                return null;
        }
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"animalId\": \"").append(animalId).append("\", ");
        weight.ifPresent(value -> sb.append("\"weight\": ").append(value).append(", "));
        height.ifPresent(value -> sb.append("\"height\": ").append(value).append(", "));
        latitude.ifPresent(value -> sb.append("\"latitude\": ").append(value).append(", "));
        longitude.ifPresent(value -> sb.append("\"longitude\": ").append(value).append(", "));
        speed.ifPresent(value -> sb.append("\"speed\": ").append(value).append(", "));
        heartRate.ifPresent(value -> sb.append("\"heartRate\": ").append(value).append(", "));
        breathRate.ifPresent(value -> sb.append("\"breathRate\": ").append(value).append(", "));
        batteryPercentage.ifPresent(value -> sb.append("\"batteryPercentage\": ").append(value).append(", "));
        blinking.ifPresent(value -> sb.append("\"blinking\": ").append(value).append(", "));
        timestamp.ifPresent(value -> sb.append("\"timestamp\": ").append(value.toEpochMilli()).append(", "));
        sb.append("\"additionalTags\": {");
        for (Map.Entry<String, String> entry : additionalTags.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\", ");
        }
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return serialize();
    }
}
