package ies.tracktails.actionservice.services;

import java.util.List;

import ies.tracktails.actionservice.dtos.ActionDTO;

public interface ActionService {
    void triggerAction(ActionDTO action);
    List<String> getValidActionTypes();
    boolean isActionTypeValid(String actionType);
}
