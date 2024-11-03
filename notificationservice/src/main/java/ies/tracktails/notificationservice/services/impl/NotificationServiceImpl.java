package ies.tracktails.notificationservice.services.impl;

import ies.tracktails.notificationservice.entities.Notification;
import ies.tracktails.notificationservice.repositories.NotificationRepository;
import ies.tracktails.notificationservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification addNotification(Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }


    @Override
    public void removeNotification(long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification updateNotification(long id, Notification notification) {
        Notification existingNotification = notificationRepository.findById(notification.getId()).get();
        existingNotification.setTitle(notification.getTitle());
        existingNotification.setContent(notification.getContent());
        existingNotification.setCreatedAt(notification.getCreatedAt());
        existingNotification.setRead(notification.isRead());
        return notificationRepository.save(existingNotification);
    }

    @Override
    public Notification getNotification(long id) {
        return notificationRepository.findById(id).isPresent() ? notificationRepository.findById(id).get() : null;
    }

    @Override
    public Notification getNotificationByTitle(String title) {
        return notificationRepository.findByTitle(title);
    }

    @Override
    public Notification getNotificationByContent(String content) {
        return notificationRepository.findByContent(content);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
