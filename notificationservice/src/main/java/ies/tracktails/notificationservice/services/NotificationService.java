package ies.tracktails.notificationservice.services;

import ies.tracktails.notificationservice.entities.Notification;
import java.util.List;

public interface NotificationService {
    public Notification addNotification(Notification notification);
    public void removeNotification(long id);
//    public Notification updateNotification(long id, Notification notification);
    public Notification getNotification(long id);
    public Notification getNotificationByTitle(String title);
    public Notification getNotificationByContent(String content);
//    public Notification getNotificationByDate(String date);
    public List<Notification> getAllNotifications();
}
