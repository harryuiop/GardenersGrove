package nz.ac.canterbury.seng302.gardenersgrove.location;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Feature;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.SearchResult;
import nz.ac.canterbury.seng302.gardenersgrove.utility.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Geocode locations from a query using the MapTiler API.
 * Update autocomplete suggestions through user input.
 */
@RestController
@RequestMapping("/maptiler")
public class MapTilerGeocoding {

    @Value("${maptiler.api.key}")
    private String apiKey;
    private final int locationLimit = 3; // Max amount of locations returned by API

    private final RateLimiter rateLimiter = new RateLimiter(30, 5);

    private final RestTemplate restTemplate = new RestTemplate();

    //  How well the returned feature matches the user’s query on a scale from 0 to 1.
    //  0 means the result does not match the query text at all, while 1 means the result fully matches the query text.
    private final double FEATURE_RELEVANCE = 0.9;

    Logger logger = LoggerFactory.getLogger(MapTilerGeocoding.class);

    /**
     * Generate the request url for MapTiler API, given input query by user and country code for filtering.
     * - Currently only limits to 3 locations per request as set as parameter in request.
     * - Proximity parameter set to ip, which gives locations closer to the user a higher rank.
     * - Fuzzy match is set to true which allows API to approximate matches.
     * (show results even if query was spelt incorrectly).
     *
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @param setApiKey API key if a non getMapping method is called (used by another Java controller)
     * @return String url.
     */
    private String generateRequestURL(String query, String countryCode, String setApiKey) {
        if (setApiKey != null) apiKey = setApiKey;

        StringBuilder urlBuilder = new StringBuilder("https://api.maptiler.com/geocoding/");
        urlBuilder.append(URLEncoder.encode(query, StandardCharsets.UTF_8)).append(".json?");


        if (countryCode != null && !countryCode.isEmpty()) {
            urlBuilder.append("country=").append(URLEncoder.encode(countryCode, StandardCharsets.UTF_8)).append("&");
        }

        urlBuilder.append("proximity=ip&fuzzyMatch=true&limit=" + locationLimit + "&key=");
        urlBuilder.append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));
        return urlBuilder.toString();
    }

    /**
     * Generate Java object from request url.
     *
     * @param requestUrl request url for mapTiler geocoding service.
     * @return Search Result object, collection of locations or empty
     * SearchResults object if no results.
     */
    private SearchResult parseResponseJSON(String requestUrl) {
        SearchResult response = restTemplate.getForObject(requestUrl, SearchResult.class);
        if (response == null) return new SearchResult();
        return response;
    }

    /**
     * Get full search results from MapTiler API as a Search Results Java object
     * , given query and optional country code.
     *
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @param setApiKey API key if a non getMapping method is called (used by another Java controller)
     * @return Search Result object, collection of locations or empty object is error occurs
     * when generating SearchResult object.
     */
    private SearchResult getSearchResult(String query, String countryCode, String setApiKey) {
        try {
            String requestUrl = generateRequestURL(query, countryCode, setApiKey);
            return parseResponseJSON(requestUrl);
        } catch (Exception e) {
            logger.error("Error occurred during parsing response. Please check API Key is valid.");
        }
        return new SearchResult();

    }

    /**
     * Get first location/feature only from search results fetched from MapTiler API if within
     * set relevance.
     *
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @param setApiKey API key if a non getMapping method is called (used by another Java controller)
     * @return Feature object which is information about a specific location.
     */
    public Feature getFirstSearchResult(String query, String countryCode, String setApiKey) {
        List<Feature> features = getSearchResult(query, countryCode, setApiKey).getFeatures();
        if (features == null || features.isEmpty()) {
            logger.info("No locations found from search result.");
            return null;
        }
        Feature feature = features.get(0);
        double featureRelevance = feature.getRelevance();
        if (featureRelevance < FEATURE_RELEVANCE) {
            logger.info("Location not within relevance of relevance set");
            return null;
        }
        return feature;
    }

    /**
     * Get autocomplete results from user input if request is allowed. A request is allowed if it
     * is within the maximum number of requests in the set timeframe.
     *
     * @param query User input (in street address field)
     * @return Autocomplete suggestions or empty map if request is rejected.
     * JSON like output to be handled in JavaScript.
     */
    @GetMapping("/search-results")
    public Map<String, List<Map<String, String>>> getSearchResults(@RequestParam String query) {
        if (rateLimiter.allowRequest()) {
            SearchResult searchResult = getSearchResult(query, null, this.apiKey);
            return searchResult.getAutocompleteSuggestions();
        } else {
            return new HashMap<>();
        }
    }
}
