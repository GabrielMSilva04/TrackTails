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
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.addNotification(notification);
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
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
    public ResponseEntity<?> getNotification(@PathVariable long id) {
        return new ResponseEntity<>(notificationService.getNotification(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable long id) {
        notificationService.removeNotification(id);
        return new ResponseEntity<>("Notification deleted successfully", HttpStatus.OK);
    }

//    @PutMapping("{id}")
//    public ResponseEntity<?> updateNotification(@PathVariable long id, @RequestBody String message) {
//        notificationService.updateNotification(id, message);
//        return new ResponseEntity<>("Notification updated successfully", HttpStatus.OK);
//    }

//    @GetMapping("/send")
//    public ResponseEntity<?> sendNotification() {
//        notificationService.sendNotification("Test notification");
//        return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
//    }
//
//    @GetMapping("/send/{id}")
//    public ResponseEntity<?> sendNotification(@PathVariable long id) {
//        notificationService.sendNotification(id);
//        return new ResponseEntity<>("Notification sent successfully", HttpStatus.OK);
//    }
}
