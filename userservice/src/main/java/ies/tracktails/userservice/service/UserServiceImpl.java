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
    public Long registerUser(String displayName, String password1, String password2, String email){

        if(!password1.equals(password2)){
            throw new IllegalArgumentException("Passwords do not match");
        }

        if(!userRepository.findByEmail(email).isEmpty()){
            throw new IllegalArgumentException("Email already in use");
        }

        if (!userRepository.findByDisplayName(displayName).isEmpty()){
            throw new IllegalArgumentException("Display name already in use");
        }

        String salt = "qwerty"; //Temos que arranjar uma maneira de gerar um salt aleatório para garantir segurança
        String hashPassword = new BCryptPasswordEncoder().encode(password1 + salt);

        User newUser = new User(displayName,email,hashPassword,salt);

        User savedUser = userRepository.save(newUser);

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
    public Long updateUser(Long userId, String newDisplayName, String newEmail, String newPassword1, String newPassword2){
        if (!userExists(userId)){
            throw new IllegalArgumentException("User not found");
        };
        User userToUpdate = userRepository.findById(userId).orElse(null);

        if(!newPassword1.equals(newPassword2)){
            throw new IllegalArgumentException("Passwords do not match");
        }

        try{
            userToUpdate.setDisplayName(newDisplayName);
            userToUpdate.setEmail(newEmail);
            userToUpdate.setHashPassword(newPassword1);

            User updatedUser = userRepository.save(userToUpdate);

            return updatedUser.getUserId();
        }catch (Exception e){
            throw new IllegalArgumentException("Error updating user");
        }
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
