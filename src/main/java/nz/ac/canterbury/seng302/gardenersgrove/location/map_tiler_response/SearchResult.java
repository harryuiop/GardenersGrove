package nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Collection of features/locations from the search result.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    private List<Feature> features;

    /**
     * @return List of features from search.
     * A feature contains information about a specific location.
     */
    public List<Feature> getFeatures() {
        return features;
    }
}
