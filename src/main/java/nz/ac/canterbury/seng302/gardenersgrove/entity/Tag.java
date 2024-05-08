package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Entity class representing a tag for a garden.
 * Tags are user-specified and are used to browse for gardens that match specific interests.
 */
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @ManyToOne
    private Garden garden;

    /**
     * JPA required no-args constructor
     */
    protected Tag() {
    }

    public Tag(String name, Garden garden) {
        this.name = name;
        this.garden = garden;
    }


    public String getName() {
        return name;
    }
}
