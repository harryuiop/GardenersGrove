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
     * @return Single valued hashmap of name: locations.
     * To be used as JSON when called from JavaScript.
     */
    public Map<String, List<String>> getAutocompleteSuggestions() {
        Map<String, List<String>> json = new HashMap<>();
        List<String> locationList = new ArrayList<>();
        for (Feature feature : features) {
            locationList.add(feature.toString());
        }
        json.put("name", locationList);
        return json;
    }
}
