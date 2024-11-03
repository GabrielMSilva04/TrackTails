package ies.tracktails.notificationservice.services;

import ies.tracktails.notificationservice.entities.Notification;
import java.util.List;

public interface NotificationService {
    public Notification addNotification(Notification notification);
    public void removeNotification(long id);
    public Notification updateNotification(long id, Notification notification);

    public List<Notification> getAllNotifications();
    public Notification getNotification(long id);
    public List<Notification> getNotificationsByUserId(long userId);
    public List<Notification> getNotificationsByAnimalId(long animalId);
    public List<Notification> getNotificationsByUserIdAndAnimalId(long userId, long animalId);
    public List<Notification> getNotificationsUnreadByUserId(long userId);
}
