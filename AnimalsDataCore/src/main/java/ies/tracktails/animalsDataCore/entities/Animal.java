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

    public Animal() {
        super();
    }

    public Animal(String name, String species, char sex,Date birthDate, Long userId) {
        super();
        this.name = name;
        this.species = species;
        this.sex = sex;
        this.birthDate = birthDate;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", sex'" + sex + '\'' +
                ", birthDate=" + birthDate +
                ", userId=" + userId +
                '}';
    }
}