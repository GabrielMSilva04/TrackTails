package ies.tracktails.animalservice.entities;

import jakarta.persistence.*;
import java.util.Date;

@Table(name = "animals")
@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private Date birthDate;

    //TODO: Add a ManyToOne relationship with the User entity
    // @ManyToOne

    public Animal() {
        super();
    }

    public Animal(String name, String species, Date birthDate) {
        super();
        this.name = name;
        this.species = species;
        this.birthDate = birthDate;
    }

    public long getId() {
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

    public void setId(long id) {
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

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}