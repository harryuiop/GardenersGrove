package nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response;

/**
 * Response from MapTiler API. A context contains additional information about a single
 * location/feature. This is used for
 * - postal code, id starts with: postal_code
 * - country, id starts with country
 * - city, id starts with county
 * - suburb, id starts with municipality
 */
public class Context {
    private String id;
    private String text;

    /**
     * @return the string id of a certain context of the feature/location. The id is used to
     * differentiate the type of context, for example, postal_code.3373541.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The text content of the context, for example, New Zealand.
     */
    public String getText() {
        return text;
    }

}
