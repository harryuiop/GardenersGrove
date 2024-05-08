package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class reflecting a garden's full information
 * Note the @link{Entity} annotation required for declaring this as a persistence entity
 */
@Entity
public class Garden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owner;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(length = 512)
    private String description;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column()
    private Float size;

    @OneToMany(mappedBy = "garden", cascade = CascadeType.REMOVE)
    private List<Plant> plants;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {
    }

    /**
     * Creates a new Garden object
     *
     * @param name     name of Garden
     * @param location The details of the physical place where the garden is
     * @param size     The physical size of the garden in square metres
     */
    public Garden(User owner, String name, Location location, Float size) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.size = size;
        this.plants = new ArrayList<>();
    }


    public void setName(String newName) {
        this.name = newName;
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public void setSize(Float newSize) {
        this.size = newSize;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Used in frontend to show full location.
     * @return Location in string format.
     */
    public String getLocationString() {
        return location.toString();
    }

    public Float getSize() {
        return size;
    }

    public User getOwner() {
        return owner;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void addPlant(Plant plant) {
        this.plants.add(plant);
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
