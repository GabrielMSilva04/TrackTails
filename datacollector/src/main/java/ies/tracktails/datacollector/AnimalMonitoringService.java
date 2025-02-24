package ies.tracktails.datacollector;

import ies.tracktails.animalsDataCore.dtos.AnimalDataDTO;
import ies.tracktails.animalsDataCore.services.AnimalService;
import ies.tracktails.animalsDataCore.services.FenceService;
import ies.tracktails.animalsDataCore.services.AnimalDataService;
import ies.tracktails.animalsDataCore.entities.Animal;
import ies.tracktails.animalsDataCore.dtos.FenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AnimalState {
    private boolean heartRateOutOfRange = false;
    private boolean breathRateOutOfRange = false;
    private boolean speedOutOfRange = false;
    private boolean fenceBreach = false;
    private boolean lowBattery = false;
    
    public AnimalState() {
    }

    public AnimalState(boolean heartRateOutOfRange, boolean breathRateOutOfRange, boolean speedOutOfRange,
            boolean fenceBreach, boolean lowBattery) {
        this.heartRateOutOfRange = heartRateOutOfRange;
        this.breathRateOutOfRange = breathRateOutOfRange;
        this.speedOutOfRange = speedOutOfRange;
        this.fenceBreach = fenceBreach;
        this.lowBattery = lowBattery;
    }
    
    public boolean isHeartRateOutOfRange() {
        return heartRateOutOfRange;
    }
    public void setHeartRateOutOfRange(boolean heartRateOutOfRange) {
        this.heartRateOutOfRange = heartRateOutOfRange;
    }
    public boolean isBreathRateOutOfRange() {
        return breathRateOutOfRange;
    }
    public void setBreathRateOutOfRange(boolean breathRateOutOfRange) {
        this.breathRateOutOfRange = breathRateOutOfRange;
    }
    public boolean isSpeedOutOfRange() {
        return speedOutOfRange;
    }
    public void setSpeedOutOfRange(boolean speedOutOfRange) {
        this.speedOutOfRange = speedOutOfRange;
    }
    public boolean isFenceBreach() {
        return fenceBreach;
    }
    public void setFenceBreach(boolean fenceBreach) {
        this.fenceBreach = fenceBreach;
    }
    public boolean isLowBattery() {
        return lowBattery;
    }
    public void setLowBattery(boolean lowBattery) {
        this.lowBattery = lowBattery;
    }
}

@Service
public class AnimalMonitoringService {

    private final FenceService fenceService;
    private final AnimalService animalService;
    private final AnimalDataService animalDataService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Save the last state to not send the same notification multiple times
    // This should be stored in a database in a real-world scenario (e.g. Redis)
    ConcurrentHashMap<Long, AnimalState> animalStates = new ConcurrentHashMap<>();

    @Autowired
    public AnimalMonitoringService(FenceService fenceService, AnimalService animalService, AnimalDataService animalDataService, KafkaTemplate<String, String> kafkaTemplate) {
        this.fenceService = fenceService;
        this.animalService = animalService;
        this.animalDataService = animalDataService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void check(AnimalDataDTO data) {
        Long animalId = Long.parseLong(data.getAnimalId());
        Animal animal = animalService.getAnimal(animalId);
        String name = animal.getName();
        Long userId = animal.getUserId();

        AnimalState newState = new AnimalState();

        if (!animalStates.containsKey(animalId)) {
            animalStates.put(animalId, new AnimalState());
        }

        // Check Heart Rate
        if (!checkHeartRate(data, animal)) {
            newState.setHeartRateOutOfRange(true);
            if (!animalStates.get(animalId).isHeartRateOutOfRange()) {
                String title = "WARN - Abnormal Heart Rate Detected";
                String content = String.format("%s's heart rate is outside the safe range.", name);
                sendNotification(animalId, userId, title, content);
            }
        }

        // Check Breath Rate
        if (!checkBreathRate(data, animal)) {
            newState.setBreathRateOutOfRange(true);
            if (!animalStates.get(animalId).isBreathRateOutOfRange()) {
                String title = "WARN - Abnormal Breathing Rate Detected";
                String content = String.format("%s's breathing rate is outside the safe range.", name);
                sendNotification(animalId, userId, title, content);
            }
        }

        // Check Speed
        if (!checkSpeed(data)) {
            newState.setSpeedOutOfRange(true);
            if (!animalStates.get(animalId).isSpeedOutOfRange()) {
                String title = "WARN - Abnormal Speed Detected";
                String content = String.format("An abnormal speed was detected for %s.", name);
                sendNotification(animalId, userId, title, content);
            }
        }

        // Check Fence Breach
        if (!checkFence(data)) {
            newState.setFenceBreach(true);
            if (!animalStates.get(animalId).isFenceBreach()) {
                String title = "WARN - Fence Breach Detected";
                String content = String.format("%s has trespassed the fenced area.", name);
                sendNotification(animalId, userId, title, content);
            }
        }

        // Check Battery Percentage
        if (!checkBatteryPercentage(data)) {
            newState.setLowBattery(true);
            if (!animalStates.get(animalId).isLowBattery()) {
                String title = "WARN - Low Battery";
                String content = String.format("%s's device battery is below 20%%. Please recharge.", name);
                sendNotification(animalId, userId, title, content);
            }
        }

        animalStates.put(animalId, newState);
    }

    private void sendNotification(Long animalId, Long userId, String title, String content) {
        String notificationMessage = String.format(
                "{\"userId\":%d,\"animalId\":%d,\"title\":\"%s\",\"content\":\"%s\"}",
                userId, animalId, title, content
        );
        kafkaTemplate.send("notification_topic", notificationMessage);
    }

    private boolean checkHeartRate(AnimalDataDTO data, Animal animal) {
        String species = animal.getSpecies();
        if (species == null || data.getHeartRate().isEmpty()) {
            return true;
        }

        double heartRate = data.getHeartRate().get();

        if ("dog".equalsIgnoreCase(species)) {
            Date birthday = animal.getBirthday();

            // Retrieve the latest weight value safely
            AnimalDataDTO latestWeightData = animalDataService.getLatestValue(animal.getId().toString(), "weight");
            Optional<Double> optionalWeight = (latestWeightData != null) ? latestWeightData.getWeight() : Optional.empty();
            double weight = optionalWeight.orElse(Double.MAX_VALUE);

            int ageInMonths = (birthday != null) ? getAnimalAgeInMonths(birthday) : -1;
            if (ageInMonths == -1 && optionalWeight.isEmpty()) {
                return heartRate >= 60 && heartRate <= 200;
            } else if (ageInMonths == -1) {
                if (weight < 10) {
                    return heartRate >= 90 && heartRate <= 160; // Small breed
                } else if (weight <= 25) {
                    return heartRate >= 80 && heartRate <= 120; // Medium breed
                } else {
                    return heartRate >= 60 && heartRate <= 90; // Large breed
                }
            } else if (ageInMonths < 1) {
                return heartRate >= 160 && heartRate <= 200;
            } else {
                if (weight < 10) {
                    return heartRate >= 90 && heartRate <= 160; // Small breed
                } else if (weight <= 25) {
                    return heartRate >= 80 && heartRate <= 120; // Medium breed
                } else {
                    return heartRate >= 60 && heartRate <= 90; // Large breed
                }
            }
        } else if ("cat".equalsIgnoreCase(species)) {
            return heartRate >= 150 && heartRate <= 220; // Cats
        }

        return true;
    }

    private int getAnimalAgeInMonths(Date birthday) {
        if (birthday == null) {
            return 0;
        }

        Calendar now = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(birthday);

        int yearsDifference = now.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int monthsDifference = now.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);

        if (now.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
            monthsDifference--;
        }

        int totalMonths = yearsDifference * 12 + monthsDifference;

        return Math.max(totalMonths, 0);
    }

    private boolean checkBreathRate(AnimalDataDTO data, Animal animal) {
        String species = animal.getSpecies();
        if (species == null || data.getBreathRate().isEmpty()) {
            return true;
        }

        double breathRate = data.getBreathRate().get();

        if ("dog".equalsIgnoreCase(species)) {
            return breathRate >= 15 && breathRate <= 25; // Dogs
        } else if ("cat".equalsIgnoreCase(species)) {
            return breathRate >= 20 && breathRate <= 30; // Cats
        }

        return true;
    }

    private boolean checkSpeed(AnimalDataDTO data) {
        if (data.getSpeed().isEmpty()) {
            return true;
        }

        double speed = data.getSpeed().get();
        return speed <= 72;
    }

    private boolean checkFence(AnimalDataDTO data) {
        Long animalId = Long.parseLong(data.getAnimalId());

        if (data.getLatitude().isEmpty() || data.getLongitude().isEmpty()) {
            return true;
        }

        FenceDTO fenceDTO = fenceService.getFenceByAnimalId(animalId);
        if (fenceDTO == null) {
            return true;
        }

        double latitude = data.getLatitude().get();
        double longitude = data.getLongitude().get();
        return fenceService.isInsideFence(animalId, latitude, longitude);
    }

    private boolean checkBatteryPercentage(AnimalDataDTO data) {
        Optional<Double> batteryPercentage = data.getBatteryPercentage();
        return batteryPercentage.isEmpty() || batteryPercentage.get() >= 20.0;
    }
}