package ies.tracktails.notificationservice.services.impl;

import ies.tracktails.notificationservice.entities.Notification;
import ies.tracktails.notificationservice.repositories.NotificationRepository;
import ies.tracktails.notificationservice.services.NotificationService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotification(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public Notification getNotificationByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification getNotificationByStatus(String status) {
        return notificationRepository.findByStatus(status);
    }

    public Notification getNotificationByCreatedAt(String createdAt) {
        return notificationRepository.findByCreatedAt(createdAt);
    }

    public Notification getNotificationByUserIdAndStatus(Long userId, String status) {
        return notificationRepository.findByUserIdAndStatus(userId, status);
    }

    public Notification getNotificationByUserIdAndCreatedAt(Long userId, String createdAt) {
        return notificationRepository.findByUserIdAndCreatedAt(userId, createdAt);
    }

    public Notification getNotificationByMessageAndStatus(String message, String status) {
        return notificationRepository.findByMessageAndStatus(message, status);
    }

    public Notification getNotificationByStatusAndCreatedAt(String status, String createdAt) {
        return notificationRepository.findByStatusAndCreatedAt(status, createdAt);
    }

    public Notification getNotificationByUserIdAndStatusAndCreatedAt(Long userId, String status, String createdAt) {
        return notificationRepository.findByUserIdAndStatusAndCreatedAt(userId, status, createdAt);
    }

    public Notification getNotificationByMessageAndStatusAndCreatedAt(String message, String status, String createdAt) {
        return notificationRepository.findByMessageAndStatusAndCreatedAt(message, status, createdAt);
    }
}
