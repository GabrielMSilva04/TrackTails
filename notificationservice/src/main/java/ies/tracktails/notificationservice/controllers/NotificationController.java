package ies.tracktails.notificationservice.controllers;

import ies.tracktails.notificationservice.entities.Notification;
import ies.tracktails.notificationservice.services.NotificationService;
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

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.addNotification(notification);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable long id) {
        notificationService.removeNotification(id);
        return new ResponseEntity<>("Notification deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable long id, @RequestBody Notification notification) {
        notification.setId(id);
        notificationService.updateNotification(id, notification);
        return new ResponseEntity<>(notification, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        return new ResponseEntity<>(notificationService.getAllNotifications(), HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("Hello from Notification Service", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable long id) {
        return new ResponseEntity<>(notificationService.getNotification(id), HttpStatus.OK);
    }
}
