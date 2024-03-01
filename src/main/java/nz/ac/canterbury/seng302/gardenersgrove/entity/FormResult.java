package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

/**
 * Entity class reflecting an entry of name, and favourite programming language
 * Note the @link{Entity} annotation required for declaring this as a persistence entity
 */
@Entity
public class FormResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String size;

    /**
     * JPA required no-args constructor
     */
    protected FormResult() {}

    /**
     * Creates a new FormResult object
     * @param name name of user
     * @param language user's favourite programming language
     */
    public FormResult(String name, String location, String size) {
        this.name = name;
        this.location = location;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() { return location;
    }

    public String getSize() { return size;
    }

    @Override
    public String toString() {
        return "FormResult{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
