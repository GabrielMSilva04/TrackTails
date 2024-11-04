package ies.tracktails.userservice.controllers;

import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Long registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PutMapping("{userId}")
    public Long updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("{userId}")
    public Long deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping
    public List<User> listAllUsers() {
        return userService.listAllUsers();
    }
}

