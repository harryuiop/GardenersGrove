package nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response;

import java.util.List;

/**
 * A Feature is a single Location. It contains all information needed about a location.
 * Note: Jackson (for deserializing JSON to Java Objects) requires getters to have same name
 * as variables (or have public variables).
 */
public class Feature {
    private List<Double> center;
    private double relevance;
    private String text;

    // Type of Location. For example: city, country, street, etc.
    private String id;

    // Additional information about the location like the country, city, etc.
    private List<Context> context;

    private String address;

    /**
     * Get center point of location as longitude and latitude.
     * @return Double List with Longitude as first value and Latitude as second value.
     */
    public List<Double> getCenter() {
        return center;
    }

    /**
     * @return How relevant the search results matches the feature from 0-1.
     */
    public double getRelevance() {
        return relevance;
    }

    /**
     * The text content from a given id used for additional information about a location like city.
     * @param locationId Type of location like city, country, etc.
     * @return The text content result, for example, 8140.
     */
    private String getContextTextFromId(LocationType locationId) {
        if (context == null || context.isEmpty()) return null;
        for (Context contextResult : context) {
            if (contextResult.getId().startsWith(locationId.toString())) {
                return contextResult.getText();
            }
        }
        return null;
    }

    /**
     * @return Full country name. For example, New Zealand.
     */
    public String getCountry() {
        return getContextTextFromId(LocationType.COUNTRY);
    }

    /**
     * @return Full city name (county from MapTiler).
     */
    public String getCity() {
        return getContextTextFromId(LocationType.CITY);
    }

    /**
     * Get number and street of address, or just street name if number not given/found.
     * @return Full street address. For example, 1234 Example Street.
     */
    public String getStreetAddress() {
        String street = getText();
        String streetNumber = getAddress();
        if (streetNumber == null) {
            return street;
        } else {
            return streetNumber + " " + street;
        }
    }

    /**
     * @return Street name. For example, Example Street.
     */
    public String getText() {
        return text;
    }

    /**
     * @return Address number only. For example, 12.
     */
    public String getAddress() {
        return address;
    }


    /**
     * @return Full suburb name (Municipality from MapTiler).
     */
    public String getSuburb() {
        return getContextTextFromId(LocationType.SUBURB);
    }

    /**
     * @return Postal Code. For example, 8140.
     */
    public String getPostcode() {
        return getContextTextFromId(LocationType.POSTCODE);
    }

    /**
     * @return List of contexts. These are additional information about a location
     * like the country, city, etc.
     */
    public List<Context> getContext() {
        return context;
    }

    /**
     * @return Type of location (like city, country, etc.)
     */
    public String getId() {
        return id;
    }

    /**
     * Get the suburb, city, country. Whitespace for any null values.
     * @return The suburb, city, country.
     */
    public String getOuterLocation() {
        StringBuilder outerLocation = new StringBuilder();
        String suburbValue = getSuburb();
        if (suburbValue != null) outerLocation.append(suburbValue).append(" ");
        String cityValue = getCity();
        if (cityValue != null) outerLocation.append(cityValue).append(" ");
        String countryValue = getCountry();
        if (countryValue != null) outerLocation.append(countryValue);
        return outerLocation.toString().trim();
    }

    @Override
    public String toString() {
        return String.format("{%s, %s %s, %s, %s,  %s}",
                getStreetAddress(), getSuburb(), getCity(), getCountry(), getPostcode(), getRelevance());
    }
}
