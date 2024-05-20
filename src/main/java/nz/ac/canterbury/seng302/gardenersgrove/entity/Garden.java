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

    @OneToMany(mappedBy = "garden", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Plant> plants;

    @ManyToMany(mappedBy = "gardens", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Tag> tags;

    @Column
    private boolean publicGarden;

    @Column
    private boolean verifiedDescription;

    /**
     * JPA required no-args constructor
     */
    protected Garden() {
    }

    /**
     * Creates a new Garden object
     *
     * @param owner                 User who create and therefore owns the garden
     * @param name                  name of Garden
     * @param description           a note made about the garden by the creator
     * @param location              The details of the physical place where the garden is
     * @param size                  The physical size of the garden in square metres
     * @param verifiedDescription   Whether the description is suitible for public consumption
     */
    public Garden(User owner, String name, String description, Location location, Float size, boolean verifiedDescription) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.location = location;
        this.size = size;
        this.plants = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.publicGarden = false;
        this.verifiedDescription = verifiedDescription;
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

    public List<Tag> getTags() {
        return tags;
    }

    public boolean getPublicGarden() {
        return publicGarden;
    }

    public void setPublicGarden(boolean publicGarden) {
        this.publicGarden = publicGarden;
    }

    public boolean getVerifiedDescription() {
        return verifiedDescription;
    }

    public void setVerifiedDescription(boolean verifiedDescription) {
        this.verifiedDescription = verifiedDescription;
    }

    @Override
    public String toString() {
        return "Garden{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", size=" + size +
                ", tags=" + tags +
                ", publicGarden=" + publicGarden +
                ", verifiedDescription=" + verifiedDescription +
                '}';
    }
}
