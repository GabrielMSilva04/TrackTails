package ies.tracktails.notificationservice.controllers;

import ies.tracktails.notificationservice.entities.Notification;
import ies.tracktails.notificationservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Notification>> getNotifications() {
        return new ResponseEntity<>(notificationService.getNotifications(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        return new ResponseEntity<>(notificationService.createNotification(notification), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        Notification notification = notificationService.getNotification(id);
        return notification != null ? new ResponseEntity<>(notification, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<Notification> updateNotification(@RequestBody Notification notification) {
        return new ResponseEntity<>(notificationService.updateNotification(notification), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Notification> getNotificationByUserId(@PathVariable Long userId) {
        Notification notification = notificationService.getNotificationByUserId(userId);
        return notification != null ? new ResponseEntity<>(notification, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Notification> getNotificationByStatus(@PathVariable String status) {
        Notification notification = notificationService.getNotificationByStatus(status);
        return notification != null ? new ResponseEntity<>(notification, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/created-at/{createdAt}")
    public ResponseEntity<Notification> getNotificationByCreatedAt(@PathVariable String createdAt) {
        Notification notification = notificationService.getNotificationByCreatedAt(createdAt);
        return notification != null ? new ResponseEntity<>(notification, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
