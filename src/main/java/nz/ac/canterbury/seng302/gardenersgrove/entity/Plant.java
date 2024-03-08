package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Entity class representing a plant in a garden
 */
@Entity
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column()
    private Integer count;

    @Column()
    private String description;

    @Column()
    private String plantedOn;

    @Column()
    private Long gardenId;

    /**
     * JPA required no-args constructor
     */
    protected Plant() {
    }


    public Plant(String name, Integer count, String description, String plantedOn, Long gardenId) {
        this.name = name;
        this.count = count;
        this.description = description;
        this.plantedOn = plantedOn;
        this.gardenId = gardenId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public String getPlantedOn() { return plantedOn; }

    public Long getGardenId() { return gardenId; }

    public void setName(String name) {
        this.name=name;
    }

    public void setCount(Integer count) {
        this.count=count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlantedOn(String plantedOn) { this.plantedOn = plantedOn; }

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", description='" + description + '\'' +
                ", plantedOn=" + plantedOn +
                '}';
    }
}
