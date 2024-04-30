package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.util.List;

/**
 * Location Entity only used for a Garden. Does not exist without a garden.
 */
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column()
    private String streetAddress;

    @Column()
    private String suburb;

    @Column()
    private String postcode;

    @Column()
    private double lat;

    @Column()
    private double lng;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    private Garden garden;

    /**
     * Initialize a Location with the required fields.
     * @param country Country
     * @param city City (Fetched as County from MapTiler API)
     */
    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }

    /**
     * JPA required no-args constructor
     */
    protected Location() {
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    /**
     * Set longitude, latitude coordinates for location.
     * @param lngLat Double List with first value lng, second value lat.
     */
    public void setLngLat(List<Double> lngLat) {
        this.lng = lngLat.get(0);
        this.lat = lngLat.get(1);
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", streetAddress, suburb, city, country, postcode);
    }
}
