package ies.tracktails.notificationservice.services;

import ies.tracktails.notificationservice.entities.Notification;
import java.util.List;

public interface NotificationService {
    Notification createNotification(Notification notification);
    Notification getNotification(Long id);
    List<Notification> getNotifications();
    Notification updateNotification(Notification notification);
    void deleteNotification(Long id);
    Notification getNotificationByUserId(Long userId);
    Notification getNotificationByStatus(String status);
    Notification getNotificationByCreatedAt(String createdAt);
    Notification getNotificationByUserIdAndStatus(Long userId, String status);
    Notification getNotificationByUserIdAndCreatedAt(Long userId, String createdAt);
}
