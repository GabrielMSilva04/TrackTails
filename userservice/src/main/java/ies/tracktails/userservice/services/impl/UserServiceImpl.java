package ies.tracktails.userservice.services.impl;

import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.repositories.UserRepository;
import ies.tracktails.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public Long registerUser(User user) {
        if(!userRepository.findByEmail(user.getEmail()).isEmpty()){
            throw new IllegalArgumentException("Email already in use");
        }
        if (!userRepository.findByDisplayName(user.getDisplayName()).isEmpty()){
            throw new IllegalArgumentException("Display name already in use");
        }

        String salt = "qwerty"; // Mudar para gerar um salt aleatório
        user.setSalt(salt);
        user.setHashPassword(new BCryptPasswordEncoder().encode(user.getHashPassword() + salt));

        User savedUser = userRepository.save(user);
        return savedUser.getUserId();
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
    public User updateUser(Long userId, User user){
        User userToUpdate = getUserById(userId);
        if (userToUpdate == null){
            return null;
        }
        userToUpdate.setDisplayName(user.getDisplayName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setHashPassword(user.getHashPassword());
        userToUpdate.setSalt(user.getSalt());
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
    public Boolean authenticateUser(String password, String email){
        User userToAuthenticate = getUserByEmail(email);
        if(userToAuthenticate == null){
            throw new IllegalArgumentException("User not found");
        }

        return new BCryptPasswordEncoder().matches(password + userToAuthenticate.getSalt(), userToAuthenticate.getHashPassword());
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
