package ies.tracktails.userservice.service;

import ies.tracktails.userservice.entity.User;
import ies.tracktails.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    // Basic CRUD methods
    //Create
    Long registerUser();

    //Read
    User getUserById(Long id);
    User getUserByDisplayName(String displayName);
    User getUserByEmail(String email);

    //Update
    Long updateUser(User user);

    //Delete
    Long deleteUser(User user);


    // Authentication Methods
    Boolean authenticateUser(String password, String username);


    //Auxiliary Methods
    Boolean userExists(User user);
    List<User> listAllUsers();

}
