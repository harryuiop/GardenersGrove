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

import java.io.*;
import java.net.URLEncoder;
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
    //Currently not working and not sure why :(
    @Value("${apiKey}")
    private String apiKey;

    private final int locationLimit = 3; // Max amount of locations returned by API

    private final RateLimiter rateLimiter = new RateLimiter(30, 5);

    private final RestTemplate restTemplate = new RestTemplate();

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
     * @return String url.
     * @throws UnsupportedEncodingException Invalid url. From query or countryCode input.
     */
    private String generateRequestURL(String query, String countryCode) throws UnsupportedEncodingException {
        String apiKey = "my_api_key";
        StringBuilder urlBuilder = new StringBuilder("https://api.maptiler.com/geocoding/");
        urlBuilder.append(URLEncoder.encode(query, "UTF-8")).append(".json?");

        if (countryCode != null && !countryCode.isEmpty()) {
            urlBuilder.append("country=").append(URLEncoder.encode(countryCode, "UTF-8")).append("&");
        }
        urlBuilder.append("proximity=ip&fuzzyMatch=true&limit=" + locationLimit + "&key=").append(URLEncoder.encode(apiKey, "UTF-8"));
        return urlBuilder.toString();
    }

    /**
     * Generate Java object from request url.
     *
     * @param requestUrl request url for mapTiler geocoding service.
     * @return Search Result object, collection of locations.
     */
    private SearchResult parseResponseJSON(String requestUrl) {
        return restTemplate.getForObject(requestUrl, SearchResult.class);
    }

    /**
     * Get full search results from MapTiler API as a Search Results Java object
     * , given query and optional country code.
     *
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @return Search Result object, collection of locations or empty object is error occurs
     * when generating SearchResult object.
     */
    public SearchResult getSearchResult(String query, String countryCode) {
        try {
            String requestUrl = generateRequestURL(query, countryCode);
            return parseResponseJSON(requestUrl);
        } catch (Exception e) {
            if (e instanceof UnsupportedEncodingException) {
                logger.error("Unsupported encoding exception when generating request url.");
            } else {
                logger.error("Error occurred during parsing response. Please check API Key is valid.");
            }
            e.printStackTrace();
            return new SearchResult();
        }
    }

    /**
     * Get first location/feature only from search results fetched from MapTiler API.
     *
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @return Feature object which is information about a specific location.
     */
    public Feature getFirstSearchResult(String query, String countryCode) {
        List<Feature> features = getSearchResult(query, countryCode).getFeatures();
        if (features == null || features.isEmpty()) {
            logger.info("No locations found from search result.");
            return null;
        }
        return features.get(0);
    }

    /**
     * Get autocomplete results from user input if request is allowed. A request is allowed if it
     * is within the maximum number of requests in the set timeframe.
     *
     * @param query User input (in street address field)
     * @return Autocomplete suggestions or empty map if request is rejected.
     * JSON like output to be handled in JavaScript.
     */
    @GetMapping("/searchresults")
    public Map<String, List<Map<String, String>>> getSearchResults(@RequestParam String query) {
        if (rateLimiter.allowRequest()) {
            SearchResult searchResult = getSearchResult(query, null);
            return searchResult.getAutocompleteSuggestions();
        } else {
            return new HashMap<>();
        }
    }
}
