package ies.tracktails.userservice.controllers;

import ies.tracktails.userservice.components.JwtTokenProvider;
import ies.tracktails.userservice.dtos.JwtResponse;
import ies.tracktails.userservice.dtos.LoginRequest;
import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
            return new ResponseEntity<>("The field 'displayName' is mandatory.", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return new ResponseEntity<>("The field 'email' is mandatory.", HttpStatus.BAD_REQUEST);
        }
        if (user.getHashPassword() == null || user.getHashPassword().isEmpty()) {
            return new ResponseEntity<>("The field 'password' is mandatory.", HttpStatus.BAD_REQUEST);
        }

        try {
            User savedUser = userService.registerUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return new ResponseEntity<>("The field 'email' is mandatory.", HttpStatus.BAD_REQUEST);
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return new ResponseEntity<>("The field 'password' is mandatory.", HttpStatus.BAD_REQUEST);
        }

        try {
            boolean isAuthenticated = userService.authenticateUser(
                    loginRequest.getPassword(),
                    loginRequest.getEmail()
            );

            if (!isAuthenticated) {
                return new ResponseEntity<>("Invalid Credentials.", HttpStatus.UNAUTHORIZED);
            }

            String token = jwtTokenProvider.generateToken(loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
        if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
            return new ResponseEntity<>("The field 'displayName' is mandatory.", HttpStatus.BAD_REQUEST);
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return new ResponseEntity<>("The field 'email' is mandatory.", HttpStatus.BAD_REQUEST);
        }

        try {
            User updatedUser = userService.updateUser(userId, user);
            if (updatedUser == null) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        User deletedUser = userService.deleteUser(userId);
        if (deletedUser == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }


    @GetMapping
    public List<User> listAllUsers() {
        return userService.listAllUsers();
    }
}

