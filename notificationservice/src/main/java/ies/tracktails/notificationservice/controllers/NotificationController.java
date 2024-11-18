package ies.tracktails.notificationservice.controllers;

import ies.tracktails.notificationservice.entities.Notification;
import ies.tracktails.notificationservice.services.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Create a new notification")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Notification not created")
    })
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.addNotification(notification);
        if (savedNotification == null) {
            return new ResponseEntity<>(new ErrorResponse("Notification not created", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a notification")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable long id) {
        Notification notification = notificationService.removeNotification(id);
        if (notification == null) {
            return new ResponseEntity<>(new ErrorResponse("Notification not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    @Operation(summary = "Update a notification")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<?> updateNotification(@PathVariable long id, @RequestBody Notification notification) {
        Notification updatedNotification = notificationService.updateNotification(id, notification);
        if (updatedNotification == null) {
            return new ResponseEntity<>(new ErrorResponse("Notification not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @Operation(summary = "Get all notifications")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))})
    })
    @GetMapping
    public ResponseEntity<?> getAllNotifications(@RequestParam(name = "userId", required = false) Long userId,
                                                 @RequestParam(name = "animalId", required = false) Long animalId) {
        if (userId != null && animalId != null) {
            return new ResponseEntity<>(notificationService.getNotificationsByUserIdAndAnimalId(userId, animalId), HttpStatus.OK);
        } else if (userId != null) {
            return new ResponseEntity<>(notificationService.getNotificationsByUserId(userId), HttpStatus.OK);
        } else if (animalId != null) {
            return new ResponseEntity<>(notificationService.getNotificationsByAnimalId(animalId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(notificationService.getAllNotifications(), HttpStatus.OK);
        }
    }

    @Operation(summary = "Get a notification by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getNotification(@PathVariable long id) {
        Notification notification = notificationService.getNotification(id);
        if (notification == null) {
            return new ResponseEntity<>(new ErrorResponse("Notification not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notification, HttpStatus.OK);
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
