package ies.tracktails.userservice.services.impl;

import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.repositories.UserRepository;
import ies.tracktails.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String originalPassword = user.getHashPassword();
        String hashedPassword = BCrypt.hashpw(originalPassword, BCrypt.gensalt());
        user.setHashPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId){
        return userRepository.findById(userId).isPresent() ? userRepository.findById(userId).get() : null;
    }

    @Override
    public List<User> getUserByDisplayName(String displayName){
        return userRepository.findByDisplayName(displayName);
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).isPresent() ? userRepository.findByEmail(email).get() : null;
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userToUpdate = getUserById(userId);
        if (userToUpdate == null) {
            return null;
        }

        userToUpdate.setDisplayName(user.getDisplayName());
        userToUpdate.setEmail(user.getEmail());

        if (user.getHashPassword() != null && !user.getHashPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(user.getHashPassword(), BCrypt.gensalt());
            userToUpdate.setHashPassword(hashedPassword);
        }

        return userRepository.save(userToUpdate);
    }

    @Override
    public User deleteUser(Long userId){
        User userToDelete = getUserById(userId);
        if (userToDelete == null){
            return null;
        }
        userRepository.deleteById(userId);
        return userToDelete;
    }

    @Override
    public Boolean authenticateUser(String password, String email) {
        User userToAuthenticate = getUserByEmail(email);
        if (userToAuthenticate == null) {
            throw new IllegalArgumentException("User not found");
        }

        boolean isPasswordValid = BCrypt.checkpw(password, userToAuthenticate.getHashPassword());

        return isPasswordValid;
    }

    @Override
    public Boolean userExists(Long userId){
        return userRepository.findById(userId).isPresent();
    }

    @Override
    public List<User> listAllUsers(){
        return userRepository.findAll();
    }
}
