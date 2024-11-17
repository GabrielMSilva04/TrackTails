package ies.tracktails.animalsDataCore.entities;

import jakarta.persistence.*;
import java.util.Date;

@Table(name = "animals")
@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    private char sex;

    private Date birthDate;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long deviceId;

    private String beCarefulWith;

    @Column(name="image_path", nullable = true)
    private String imagePath;

    public Animal() {
        super();
    }

    public Animal(String name, String species, char sex,Date birthDate, Long userId, Long deviceId, String beCarefulWith) {
        super();
        this.name = name;
        this.species = species;
        this.sex = sex;
        this.birthDate = birthDate;
        this.userId = userId;
        this.deviceId = deviceId;
        this.beCarefulWith = beCarefulWith;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public char getSex() {
        return sex;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getBeCarefulWith() {
        return beCarefulWith;
    }

    public void setBeCarefulWith(String beCarefulWith) {
        this.beCarefulWith = beCarefulWith;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", sex'" + sex + '\'' +
                ", birthDate=" + birthDate +
                ", userId=" + userId +
                ", deviceId=" + deviceId +
                ", beCarefulWith='" + beCarefulWith + '\'' +
                '}';
    }
}