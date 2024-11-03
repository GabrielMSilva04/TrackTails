package ies.tracktails.notificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ies.tracktails.notificationservice.entities.Notification;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByTitle(String title);

    Notification findByContent(String content);

    //Notification findByDate(String date);

    Notification save(Notification notification);

    void deleteById(long id);

    Optional<Notification> findById(long id);

    List<Notification> findAll();
}
