package ies.tracktails.notificationservice;

import ies.tracktails.notificationservice.entities.Notification;
import ies.tracktails.notificationservice.services.NotificationService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "notification_topic", groupId = "notification-service-group")
    public void consume(String message) {
        try {
            System.out.println("Received message: " + message);

            String json = objectMapper.readTree(message).asText();
            Notification notification = objectMapper.readValue(json, Notification.class);



            if (!notificationService.existsUnreadNotification(
                    notification.getUserId(),
                    notification.getAnimalId(),
                    notification.getTitle(),
                    notification.getContent())
                && !notificationService.existsNotification(
                    notification.getUserId(),
                    notification.getAnimalId(),
                    notification.getTitle(),
                    notification.getContent(),
                    LocalDateTime.now().minusMinutes(5))) {

                notificationService.addNotification(notification);
                System.out.println("Notification processed and saved: " + notification);

            } else {
                System.out.println("Duplicate unread notification detected, skipping: " + notification);
            }

        } catch (Exception e) {
            System.err.println("Error processing notification message: " + e.getMessage());
        }
    }
}