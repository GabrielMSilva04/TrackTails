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
    public List<Notification> getNotificationsByUserId(long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getNotificationsByAnimalId(long animalId) {
        return notificationRepository.findByAnimalId(animalId);
    }

    @Override
    public List<Notification> getNotificationsByUserIdAndAnimalId(long userId, long animalId) {
        return notificationRepository.findByUserIdAndAnimalId(userId, animalId);
    }

    @Override
    public List<Notification> getNotificationsUnreadByUserId(long userId) {
        return notificationRepository.findByUserIdAndRead(userId, false);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
