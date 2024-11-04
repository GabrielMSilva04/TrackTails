package ies.tracktails.userservice.services;

import ies.tracktails.userservice.entities.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    // Basic CRUD methods
    //Create
    // UserService.java
    User registerUser(User user);

    //Read
    User getUserById(Long id);
    List<User> getUserByDisplayName(String displayName);
    User getUserByEmail(String email);

    //Update
    User updateUser(Long userId, User user);

    //Delete
    User deleteUser(Long userId);

    // Authentication Methods
    Boolean authenticateUser(String password, String username);

    //Auxiliary Methods
    Boolean userExists(Long userId);
    List<User> listAllUsers();
}
