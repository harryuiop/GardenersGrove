package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Column(length = 512)
    private String description;

    @Column()
    private Date plantedOn;

    @Column()
    private Long gardenId;

    private static final DateFormat printFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * JPA required no-args constructor
     */
    protected Plant() {
    }


    public Plant(String name, Integer count, String description, Date plantedOn, Long gardenId) {
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

    public Date getPlantedOn() {
        return plantedOn;
    }
    public String getDateString() {
        if (plantedOn == null) {
            return null;
        }
        return printFormat.format(plantedOn);
    };

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

    public void setPlantedOn(Date plantedOn) { this.plantedOn = plantedOn; }

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
