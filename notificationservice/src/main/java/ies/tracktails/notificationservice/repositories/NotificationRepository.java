package ies.tracktails.notificationservice.repositories;

import ies.tracktails.notificationservice.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByUserId(Long userId);

    Notification findByStatus(String status);

    Notification findByCreatedAt(String createdAt);

    Notification findByUserIdAndStatus(Long userId, String status);

    Notification findByUserIdAndCreatedAt(Long userId, String createdAt);

    Notification findByMessageAndStatus(String message, String status);

    Notification findByStatusAndCreatedAt(String status, String createdAt);

    Notification findByUserIdAndStatusAndCreatedAt(Long userId, String status, String createdAt);

    Notification findByMessageAndStatusAndCreatedAt(String message, String status, String createdAt);
}
