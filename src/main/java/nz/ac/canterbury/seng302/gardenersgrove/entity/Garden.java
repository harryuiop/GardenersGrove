package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

/**
 * Entity class reflecting an entry of name, and favourite programming language
 * Note the @link{Entity} annotation required for declaring this as a persistence entity
 */
@Entity
public class Garden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column()
    private Float size;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {
    }

    /**
     * Creates a new Garden object
     *
     * @param name     name of Garden
     * @param location user's favourite programming language
     */
    public Garden(String name, String location, Float size) {
        this.name = name;
        this.location = location;
        this.size = size;
    }


    public void setName(String newName) {
        this.name = newName;
    }

    public void setLocation(String newLocation) {
        this.location = newLocation;
    }

    public void setSize(Float newSize) {
        this.size = newSize;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Float getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Garden{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", size=" + size +
                '}';
    }
}
