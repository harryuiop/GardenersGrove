package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.util.Base64;
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
    private Date plantedOn;

    @Lob
    @Column(name = "image", columnDefinition="BLOB")
    private byte[] image;

    @Column()
    private String imageType;

    @Column()
    private Long gardenId;

    /**
     * JPA required no-args constructor
     */
    protected Plant() {
    }


    public Plant(String name, Integer count, String description, Date plantedOn, byte[] image, String imageType, Long gardenId) {
        this.name = name;
        this.count = count;
        this.description = description;
        this.plantedOn = plantedOn;
        this.image = image;
        this.imageType = imageType;
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

    public byte[] getImage() { return image; }

    public Date getPlantedOn() { return plantedOn; }

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

    /**
     * Get image in base64 String format to be shown in the frontend via html.
     * @return Image in base64 String format
     */
    public String getBase64Image() {
        String base64Image = Base64.getEncoder().encodeToString(image);
        return "data:" + imageType + ";base64," + base64Image;
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
