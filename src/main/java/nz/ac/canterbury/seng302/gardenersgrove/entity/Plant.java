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
    private long id;

    @Column(nullable = false)
    private String name;

    @Column()
    private Integer count;

    @Column()
    private String description;

    @Column()
    private Date plantedOn;

    @Column
    private String imageFileName;

    @Column()
    private Long gardenId;

    /**
     * JPA required no-args constructor
     */
    protected Plant() {
    }


    public Plant(String name, Integer count, String description, Date plantedOn, String imageFileName, Long gardenId) {
        this.name = name;
        this.count = count;
        this.description = description;
        this.plantedOn = plantedOn;
        this.imageFileName = imageFileName;
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

    public String getImageFileName() {
        return imageFileName;
    }

    public String getImageFilePath() {
        return "/uploads/" + imageFileName;
    }

    public Date getPlantedOn() {
        return plantedOn;
    }

    public Long getGardenId() {
        return gardenId;
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

    public void setPlantedOn(Date plantedOn) {
        this.plantedOn = plantedOn;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * Check if image has been set to be used in frontend via html.
     *
     * @return If image is set.
     */
    public boolean isImageSet() {
        return imageFileName != null;
    }

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
