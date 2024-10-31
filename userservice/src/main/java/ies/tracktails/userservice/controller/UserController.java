package ies.tracktails.userservice.controller;

import ies.tracktails.userservice.entity.User;
import ies.tracktails.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Long registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PutMapping("/update/{userId}")
    public Long updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/delete/{userId}")
    public Long deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping("/list")
    public List<User> listAllUsers() {
        return userService.listAllUsers();
    }
}

