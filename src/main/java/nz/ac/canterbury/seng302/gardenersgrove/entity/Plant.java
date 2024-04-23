package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Entity class representing a plant in a garden
 */
@Entity
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer count;

    @Column(length = 512)
    private String description;

    @Column
    private LocalDate plantedOn;

    @Column
    private String imageFileName;

    @ManyToOne(optional = false)
    private Garden garden;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * JPA required no-args constructor
     */
    protected Plant() {
    }

    public Plant(String name, Integer count, String description, LocalDate plantedOn, String imageFileName, Garden garden) {
        this.name = name;
        this.count = count;
        this.description = description;
        this.plantedOn = plantedOn;
        this.imageFileName = imageFileName;
        this.garden = garden;
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

    public String getImageFileName() {
        return imageFileName;
    }

    public String getImageFilePath() {
        if (imageFileName == null) {
            return "/images/default-plant.jpg";
        }
        return "/uploads/" + imageFileName;
    }

    public String getDateString() {
        if (plantedOn == null) {
            return null;
        }
        return plantedOn.format(dateFormatter);
    }

    public LocalDate getPlantedOn() {
        return plantedOn;
    }

    public Garden getGarden() {
        return garden;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlantedOn(LocalDate plantedOn) {
        this.plantedOn = plantedOn;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", description='" + description + '\'' +
                        ", imageFileName='" + imageFileName + '\'' +
                ", plantedOn=" + plantedOn +
                '}';
    }
}
