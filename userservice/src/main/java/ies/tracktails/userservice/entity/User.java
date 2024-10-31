package ies.tracktails.userservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name = "user_tbl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String userId;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    public String hashPassword;

    @JsonIgnore
    @Column
    private String salt;

    public User() {
    }

    public User(String userId, String displayName, String email, String hashPassword, String salt) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.salt = salt;
        this.hashPassword = new BCryptPasswordEncoder().encode(hashPassword + salt);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getHashPassword() {
        return hashPassword;
    }

    @JsonProperty
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    @JsonIgnore
    public String getSalt() {
        return salt;
    }

    @JsonProperty
    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
