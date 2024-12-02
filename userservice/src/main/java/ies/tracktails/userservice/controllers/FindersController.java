package ies.tracktails.userservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.services.UserService;

@RestController
@RequestMapping("/api/v1/finders/user")
class FindersController {

    private final UserService userService;

    @Autowired
    public FindersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get User By Id", description = "Get User Contact Information By Id")
    @GetMapping("{userId}")
    public ResponseEntity<?> getAnimal(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}