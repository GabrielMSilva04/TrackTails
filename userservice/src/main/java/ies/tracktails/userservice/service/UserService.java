package ies.tracktails.userservice.service;

import ies.tracktails.userservice.entity.User;
import ies.tracktails.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    // Basic CRUD methods
    //Create
    Long registerUser(String displayName, String password1, String password2, String email);

    //Read
    User getUserById(Long id);
    User getUserByDisplayName(String displayName);
    User getUserByEmail(String email);

    //Update
    Long updateUser(Long userId, String newDisplayName, String newEmail, String newPassword1, String newPassword2);

    //Delete
    Long deleteUser(Long userId);


    // Authentication Methods
    Boolean authenticateUser(String password, String username);


    //Auxiliary Methods
    Boolean userExists(Long userId);
    List<User> listAllUsers();
}
