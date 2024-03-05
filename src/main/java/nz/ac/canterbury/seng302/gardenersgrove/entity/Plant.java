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
    private int count;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date plantedOn;

    @Lob
    @Column(name = "image", columnDefinition="BLOB")
    private byte[] image;

    /**
     * JPA required no-args constructor
     */
    protected Plant() {
    }


    public Plant(String name, int count, String description, Date plantedOn, byte[] image) {
        this.name = name;
        this.count = count;
        this.description = description;
        this.plantedOn = plantedOn;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
