package ies.tracktails.userservice.services;

import ies.tracktails.userservice.entities.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    // Basic CRUD methods
    //Create
    // UserService.java
    User registerUser(String displayName, String email, int phoneNumber, String password);

    //Read
    User getUserById(Long id);
    List<User> getUserByDisplayName(String displayName);
    User getUserByEmail(String email);
    User getUserByPhoneNumber(int phoneNumber);

    //Update
    User updateUser(Long userId, String displayName, String email, int phoneNumber, String password);

    //Delete
    User deleteUser(Long userId);

    // Authentication Methods
    Boolean authenticateUser(String password, String email);

    //Auxiliary Methods
    Boolean userExists(Long userId);
    List<User> listAllUsers();
}
