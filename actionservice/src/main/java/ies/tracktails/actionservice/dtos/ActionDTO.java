package ies.tracktails.actionservice.dtos;

import jakarta.validation.constraints.NotBlank;

public class ActionDTO {
    @NotBlank
    String actionType;
    @NotBlank
    Long animalId;
    
    public ActionDTO() {
    }

    public ActionDTO(String actionType, Long animalId) {
        this.actionType = actionType;
        this.animalId = animalId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
}
