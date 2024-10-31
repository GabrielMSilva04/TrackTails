package ies.tracktails.userservice.service;

import ies.tracktails.userservice.entity.User;
import ies.tracktails.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

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
        if (!userExists(userId)){
            throw new IllegalArgumentException("User not found");
        };
        User wantedUser = userRepository.findById(userId).orElse(null);

        return wantedUser;
    }

    @Override
    public User getUserByDisplayName(String displayName){
        if (!userExists(userRepository.findByDisplayName(displayName).orElse(null).getUserId())){
            throw new IllegalArgumentException("User not found");
        };
        User wantedUser = userRepository.findByDisplayName(displayName).orElse(null);

        return wantedUser;
    }

    @Override
    public User getUserByEmail(String email){
        if (!userExists(userRepository.findByEmail(email).orElse(null).getUserId())){
            throw new IllegalArgumentException("User not found");
        };
        User wantedUser = userRepository.findByEmail(email).orElse(null);

        return wantedUser;
    }

    @Override
    public Long updateUser(Long userId, User user) {
        if (!userExists(userId)){
            throw new IllegalArgumentException("User not found");
        }
        User userToUpdate = userRepository.findById(userId).orElse(null);

        userToUpdate.setDisplayName(user.getDisplayName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setHashPassword(new BCryptPasswordEncoder().encode(user.getHashPassword() + userToUpdate.getSalt()));

        User updatedUser = userRepository.save(userToUpdate);
        return updatedUser.getUserId();
    }

    @Override
    public Long deleteUser(Long userId){
        if (!userExists(userId)){
            throw new IllegalArgumentException("User not found");
        };
        User userToDelete = userRepository.findById(userId).orElse(null);

        try {
            userRepository.delete(userToDelete);

            return userId;
        } catch (Exception e){
            throw new IllegalArgumentException("Error deleting user");
        }
    }

    @Override
    public Boolean authenticateUser(String password, String email){
        if (!userExists(userRepository.findByEmail(email).orElse(null).getUserId())){
            throw new IllegalArgumentException("User not found");
        };
        User userToAuthenticate = userRepository.findByEmail(email).orElse(null);

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
