package ies.tracktails.userservice.service;

import ies.tracktails.userservice.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    // Basic CRUD methods
    //Create
    // UserService.java
    Long registerUser(User user);

    //Read
    User getUserById(Long id);
    User getUserByDisplayName(String displayName);
    User getUserByEmail(String email);

    //Update
    Long updateUser(Long userId, User user);

    //Delete
    Long deleteUser(Long userId);

    // Authentication Methods
    Boolean authenticateUser(String password, String username);

    //Auxiliary Methods
    Boolean userExists(Long userId);
    List<User> listAllUsers();
}
