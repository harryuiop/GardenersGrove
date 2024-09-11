package nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response;

/**
 * Get the location type from the api.
 */
public enum LocationType {
    CITY("county"),
    COUNTRY("country"),
    SUBURB("municipality"),
    POSTCODE("postal_code"),
    ADDRESS("address");

    public final String locationString;

    private LocationType(String locationString) {
        this.locationString = locationString;
    }

    @Override
    public String toString() {
        return locationString;
    }
}
