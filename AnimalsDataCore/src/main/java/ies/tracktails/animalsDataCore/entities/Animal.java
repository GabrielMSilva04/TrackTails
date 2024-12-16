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

    @Column
    private String breed;

    @Column
    private char sex;

    @Column
    private Date birthday;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private Long deviceId;

    @Column
    private String beCarefulWith;

    @Column(name="image_path", nullable = true)
    private String imagePath;

    public Animal() {
        super();
    }

    public Animal(String name, String species, String breed, char sex,Date birthday, Long userId, Long deviceId, String beCarefulWith, String imagePath) {
        super();
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.sex = sex;
        this.birthday = birthday;
        this.userId = userId;
        this.deviceId = deviceId;
        this.beCarefulWith = beCarefulWith;
        this.imagePath = imagePath;
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

    public Date getBirthday() {
        return birthday;
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

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", sex'" + sex + '\'' +
                ", birthday=" + birthday +
                ", userId=" + userId +
                ", deviceId=" + deviceId +
                ", beCarefulWith='" + beCarefulWith + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}