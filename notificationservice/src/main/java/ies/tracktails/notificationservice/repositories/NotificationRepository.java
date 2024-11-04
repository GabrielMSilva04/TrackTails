package ies.tracktails.notificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ies.tracktails.notificationservice.entities.Notification;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAll();
    Optional<Notification> findById(long id);
    List<Notification> findByUserId(long userId);
    List<Notification> findByAnimalId(long animalId);
    List<Notification> findByUserIdAndAnimalId(long userId, long animalId);
    List<Notification> findByUserIdAndRead(long userId, boolean read);

    Notification save(Notification notification);

    void deleteById(long id);
}
