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
        for (Feature feature : features) {
            String streetAddress = feature.getStreetAddress();
            if (streetAddress == null) streetAddress = "";
            Map<String, String> locationMap = new HashMap<>();
            locationMap.put("streetAddress", streetAddress);
            locationMap.put("outerLocation", feature.getOuterLocation());
            locationMap.put("country", feature.getCountry());
            locationMap.put("city", feature.getCity());
            locationMap.put("suburb", feature.getSuburb());
            locationMap.put("postcode", feature.getPostcode());
            locationList.add(locationMap);
        }
        json.put("locations", locationList);
        return json;
    }
}
