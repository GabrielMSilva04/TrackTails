package ies.tracktails.userservice.repositories;

import ies.tracktails.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByDisplayName(String displayName);
    Optional<User> findByPhoneNumber(int phoneNumber);
}
