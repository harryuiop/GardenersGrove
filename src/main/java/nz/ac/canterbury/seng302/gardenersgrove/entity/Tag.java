package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Entity class representing a tag for a garden.
 * Tags are user-specified and are used to browse for gardens that match specific interests.
 */
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT", unique = true)
    private String name;

    @ManyToMany
    private List<Garden> gardens;

    /**
     * JPA required no-args constructor
     */
    protected Tag() {
    }

    public Tag(String name, Garden garden) {
        this.name = name;
        this.gardens = Arrays.asList(garden);
    }

    public String getName() {
        return name;
    }

    public void addGarden(Garden garden) {
        gardens.add(garden);
    }
}
