package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Location Entity only used for a Garden. Does not exist without a garden.
 */
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String country;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String city;

    @Column(columnDefinition = "TEXT")
    private String streetAddress;

    @Column(columnDefinition = "TEXT")
    private String suburb;

    @Column
    private String postcode;

    @Column
    private double lat;

    @Column
    private double lng;

    private boolean isCoordinatesSet = false;

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

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
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
        isCoordinatesSet = true;
    }

    /**
     * Check if the location is recognized by the API
     * @return boolean if location has been defaulted to 0, 0
     */
    public boolean isLocationRecognized() {
        return lat != 0 && lng != 0;
    }

    /**
     * Sets the garden location to 0.0, 0.0 if the location
     * has not been recognized by the API
     */
    public void setCoordinatesToZero() {
        List<Double> zeroList = new ArrayList<>();
        zeroList.add(0.0);
        zeroList.add(0.0);
        this.setLngLat(zeroList);
    }

    /**
     * @return If latitude and longitude have been set.
     */
    public boolean isCoordinatesSet() {
        return isCoordinatesSet;
    }

    @Override
    public String toString() {
        String string = String.format("%s, %s", city, country);
        if (suburb != null && !suburb.isEmpty() && !suburb.equals(" ")) {
            string = suburb + ", " + string;
        }
        if (streetAddress != null && !streetAddress.isEmpty() && !streetAddress.equals(" ")) {
            string = streetAddress + ", " + string;
        }
        if (postcode != null && !postcode.isEmpty() && !postcode.equals(" ")) {
            string += (", " + postcode);
        }
        return string;
    }
}
