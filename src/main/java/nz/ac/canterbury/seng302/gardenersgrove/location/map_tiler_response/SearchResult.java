package nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection of features/locations from the search result.
 */
public class SearchResult {
    private List<Feature> features;

    /**
     * @return List of features from search.
     * A feature contains information about a specific location.
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * Get the location names only from the search result.
     * @return Hashmap in format: locations : [streetName, outerLocation, country, city, suburb, postcode], where outerLocation
     * is the suburb, city, country. To be used as JSON when called from JavaScript.
     */
    public Map<String, List<Map<String, String>>> getAutocompleteSuggestions() {
        Map<String, List<Map<String, String>>> json = new HashMap<>();
        List<Map<String, String>> locationList = new ArrayList<>();
        if (features == null || features.isEmpty()) return json;
        for (Feature feature : features) {
            locationList.add(getLocationMap(feature));
        }
        json.put("locations", locationList);
        return json;
    }

    /**
     * Get mapping of single location to send to javascript autcomplete.
     */
    private Map<String, String> getLocationMap(Feature feature) {
        Map<String, String> locationMap = new HashMap<>();

        String streetAddress = feature.getStreetAddress();
        if (streetAddress == null || !feature.getId().startsWith(LocationType.ADDRESS.toString())) {
            streetAddress = "";
            locationMap.put("primaryAddress", feature.getText());
        } else {
            locationMap.put("primaryAddress", streetAddress);
        }
        locationMap.put("streetAddress", streetAddress);

        locationMap.put("outerLocation", feature.getOuterLocation());

        if (feature.getId().startsWith(LocationType.COUNTRY.toString())) {
            locationMap.put("country", feature.getText());
        } else {
            locationMap.put("country", feature.getCountry());
        }

        if (feature.getId().startsWith(LocationType.CITY.toString())) {
            locationMap.put("city", feature.getText());
        } else {
            locationMap.put("city", feature.getCity());
        }

        if (feature.getId().startsWith(LocationType.SUBURB.toString())) {
            locationMap.put("suburb", feature.getText());
        } else {
            locationMap.put("suburb", feature.getSuburb());

        }

        if (feature.getId().startsWith(LocationType.POSTCODE.toString())) {
            locationMap.put("postcode", feature.getText());
        } else {
            locationMap.put("postcode", feature.getPostcode());
        }

        return locationMap;
    }
}
