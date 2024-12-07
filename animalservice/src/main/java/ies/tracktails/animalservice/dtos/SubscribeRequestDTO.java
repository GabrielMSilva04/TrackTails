package ies.tracktails.animalservice.dtos;

import jakarta.validation.constraints.NotBlank;

public class SubscribeRequestDTO {
    @NotBlank(message = "Action is required.")
    private String action;

    @NotBlank(message = "Animal ID is required.")
    private String animalId;

    // Getters and setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }
}
