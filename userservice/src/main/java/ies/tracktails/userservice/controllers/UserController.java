package ies.tracktails.userservice.controllers;

import ies.tracktails.userservice.components.JwtTokenProvider;
import ies.tracktails.userservice.dtos.JwtResponse;
import ies.tracktails.userservice.dtos.LoginRequest;
import ies.tracktails.userservice.dtos.RegisterRequest;
import ies.tracktails.userservice.entities.User;
import ies.tracktails.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user_id")
    public ResponseEntity<String> getUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok("User ID: " + userId);
    }

    @Operation(summary = "Get current user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved", content = @Content(schema = @Schema(implementation = User.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            // Validate token
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            // Extract user ID
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            // Fetch user details
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage());
        }
    }

    @Operation(summary = "Register user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered", content = @Content(schema = @Schema(implementation = User.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.getDisplayName() == null || registerRequest.getDisplayName().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("The field 'displayName' is mandatory.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("The field 'email' is mandatory.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        if (registerRequest.getPhoneNumber() == 0) {
            return new ResponseEntity<>(new ErrorResponse("The field 'phoneNumber' is mandatory.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("The field 'password' is mandatory.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        try {
            User savedUser = userService.registerUser(
                    registerRequest.getDisplayName(),
                    registerRequest.getEmail(),
                    registerRequest.getPhoneNumber(),
                    registerRequest.getPassword()
            );
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User authenticated", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
                return new ResponseEntity<>(new ErrorResponse("Credenciais inválidas.", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            User user = userService.getUserByEmail(loginRequest.getEmail());
            String token = jwtTokenProvider.generateToken(user.getUserId());

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated", content = @Content(schema = @Schema(implementation = User.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest newUser) {
        if (newUser.getDisplayName() == null || newUser.getDisplayName().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("The field 'displayName' is mandatory.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        if (newUser.getEmail() == null || newUser.getEmail().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("The field 'email' is mandatory.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        try {
            User updatedUser = userService.updateUser(userId, newUser.getDisplayName(), newUser.getEmail(), newUser.getPhoneNumber(), newUser.getPassword());
            if (updatedUser == null) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved", content = @Content(schema = @Schema(implementation = User.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        User deletedUser = userService.deleteUser(userId);
        if (deletedUser == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }

    @Operation(summary = "List all users")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved", content = @Content(schema = @Schema(implementation = User.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping
    public ResponseEntity<?> listAllUsers() {
        List<User> users = userService.listAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("Users not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}


class ErrorResponse {
    private String message;
    private HttpStatus status;
    private int code;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
