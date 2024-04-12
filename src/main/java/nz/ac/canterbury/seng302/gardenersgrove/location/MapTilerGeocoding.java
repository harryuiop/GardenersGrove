package nz.ac.canterbury.seng302.gardenersgrove.location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Feature;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.SearchResult;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Geocode locations from a query using the MapTiler API.
 */
public class MapTilerGeocoding {
    //Currently not working and not sure why :(
    @Value("${apiKey}")
    private String apiKey;

    /**
     * Send API request, given input query by user and country code for filtering.
     * - Currently only limits to 3 locations per request as set as parameter in request.
     * - Proximity parameter set to ip, which gives locations closer to the user a higher rank.
     * - Fuzzy match is set to true which allows API to approximate matches.
     * (show results even if query was spelt incorrectly).
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @return JSON String response from MapTiler API.
     */
    private String sendRequest(String query, String countryCode) {
        StringBuilder response = new StringBuilder();
        try {
            String apiKey = "will be fetched from application-dev.properties once i figure it out";
            StringBuilder urlBuilder = new StringBuilder("https://api.maptiler.com/geocoding/");
            urlBuilder.append(URLEncoder.encode(query, "UTF-8")).append(".json?");

            if (countryCode != null && !countryCode.isEmpty()) {
                urlBuilder.append("country=").append(URLEncoder.encode(countryCode, "UTF-8")).append("&");
            }
            urlBuilder.append("proximity=ip&fuzzyMatch=true&limit=3&key=").append(URLEncoder.encode(apiKey, "UTF-8"));
            String apiUrl = urlBuilder.toString();
            URL url = new URL(apiUrl);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            // Read the response
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    /**
     * Parse JSON into Java object using the Jackson library.
     * @param responseString JSON Response string received from API request.
     * @return Search Result object, collection of locations.
     * @throws JsonProcessingException Invalid JSON.
     */
    private SearchResult parseResponseJSON(String responseString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseString, SearchResult.class);
    }

    /**
     * Get full search results from MapTiler API as a Search Results Java object
     * , given query and optional country code.
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @return Search Result object, collection of locations.
     */
    public SearchResult getSearchResult(String query, String countryCode) {

        String response = sendRequest(query, countryCode);
        // TODO Handle these errors better instead of just returning null.
        try {
            return parseResponseJSON(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get first location/feature only from search results fetched from MapTiler API.
     * @param query Input query by user.
     * @param countryCode ISO 3166-1 alpha-2 country code or null if not set.
     * @return Feature object which is information about a specific location.
     */
    public Feature getFirstSearchResult(String query, String countryCode) {
        List<Feature> features = getSearchResult(query, countryCode).getFeatures();
        if (!features.isEmpty()) {
            return features.get(0);
        }
        return null;
    }

    // Testing purposes only.
    public static void main(String[] args) throws JsonProcessingException {
        /*String jsonInput = mapTilerGeocoding.sampleResponse;
        System.out.println(jsonInput);

        ObjectMapper mapper = new ObjectMapper();
        SearchResult searchResult = mapper.readValue(jsonInput, SearchResult.class);
        System.out.println(searchResult.features.get(0).place_name);*/

        /*String sampleResponse = mapTilerGeocoding.sampleResponse;
        ObjectMapper mapper = new ObjectMapper();
        SearchResult searchResult = mapper.readValue(sampleResponse, SearchResult.class);
        Feature feature = searchResult.features.get(0);
        System.out.println(feature.getCountry());
        System.out.println(feature.getCity());
        System.out.println(feature.getSuburb());
        System.out.println(feature.getPostcode());
        System.out.println(feature.getStreetAddress());*/

        //mapTilerGeocoding.sendRequest("22 Bengal Drive", "NZ");

    }
}
