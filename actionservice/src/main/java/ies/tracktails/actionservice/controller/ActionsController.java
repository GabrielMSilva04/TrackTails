package ies.tracktails.actionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.tracktails.actionservice.dtos.ActionDTO;
import ies.tracktails.actionservice.services.ActionService;
import ies.tracktails.animalsDataCore.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/actions")
public class ActionsController {
    private final ActionService actionService;
    private final AnimalService animalService;

    @Autowired
    public ActionsController(ActionService actionService, AnimalService animalService) {
        this.actionService = actionService;
        this.animalService = animalService;
    }

    @Operation(summary = "Trigger an action")
    @PostMapping
    public ResponseEntity<?> triggerAction(@RequestHeader("X-User-Id") String userIdHeader, @RequestBody ActionDTO actionDTO) {
        if (actionService.isActionTypeValid(actionDTO.getActionType())) {
            return new ResponseEntity<>(new ErrorResponse("Invalid action type", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        if (animalService.getAnimal(actionDTO.getAnimalId()) == null) {
            return new ResponseEntity<>(new ErrorResponse("Animal not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        if (userIdHeader == null || userIdHeader.isBlank()) {
            return new ResponseEntity<>(new ErrorResponse("Missing X-User-Id header", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        if (!userIdHeader.equals(animalService.getAnimal(actionDTO.getAnimalId()).getUserId().toString())) {
            return new ResponseEntity<>(new ErrorResponse("Invalid X-User-Id header", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        }

        actionService.triggerAction(actionDTO);
        return ResponseEntity.ok().build();
    }
}

class ErrorResponse {
    private String message;
    private HttpStatus status;
    private int code;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
