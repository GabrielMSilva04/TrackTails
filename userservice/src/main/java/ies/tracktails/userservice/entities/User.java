package ies.tracktails.userservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(name = "display_name", nullable = false, length = 15)
    private String displayName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "hash_password", nullable = false)
    public String hashPassword;

    @JsonIgnore
    @Column(name = "salt", nullable = false)
    private String salt;

    public User() {
        super();
    }

    public User(String displayName, String email, String password, String salt) {
        super();
        this.displayName = displayName;
        this.email = email;
        this.salt = salt;
        this.hashPassword = new BCryptPasswordEncoder().encode(password + salt);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
                ", email='" + email + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
