package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Context;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Feature;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.SearchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchResultTest {
    private SearchResult searchResult = new SearchResult();

    @Test
    void getAutocompleteSuggestions_emptyFeatures_returnEmptyList() {
        Map<String, List<Map<String, String>>> suggestions = searchResult.getAutocompleteSuggestions();
        Assertions.assertEquals(0, suggestions.size());
    }

    @Test
    void getAutocompleteSuggestions_containsSingleFeature_returnSuggestions() {
        Feature feature = createFeature("New Zealand", "Christchurch", "Ilam",
                "8041", "Example Street", "123");

        ReflectionTestUtils.setField(searchResult, "features", List.of(feature));

        Map<String, List<Map<String, String>>> suggestions = searchResult.getAutocompleteSuggestions();
        List<Map<String, String>> locations = suggestions.get("locations");
        Assertions.assertEquals(1, locations.size());

        Map<String, String> locationMap = locations.get(0);
        Assertions.assertEquals("New Zealand", locationMap.get("country"));
        Assertions.assertEquals("123 Example Street", locationMap.get("streetAddress"));
        Assertions.assertEquals("Christchurch", locationMap.get("city"));
        Assertions.assertEquals("8041", locationMap.get("postcode"));
        Assertions.assertEquals("Ilam", locationMap.get("suburb"));
        Assertions.assertEquals("Ilam Christchurch New Zealand", locationMap.get("outerLocation"));
    }

    @Test
    void getAutocompleteSuggestions_containsMultipleFeatures_returnSuggestions() {
        Feature feature1 = createFeature("New Zealand", "Christchurch", "Ilam",
                "8041", "Example Street", "123");
        Feature feature2 = createFeature("New Zealand", "Christchurch", "Ilam",
                "9000", "Example Street", "125");
        Feature feature3 = createFeature("Australia", "Sydney", "Example",
                "1234", "Exemplar Road", "123");

        ReflectionTestUtils.setField(searchResult, "features", Arrays.asList(feature1, feature2, feature3));

        Map<String, List<Map<String, String>>> suggestions = searchResult.getAutocompleteSuggestions();
        List<Map<String, String>> locations = suggestions.get("locations");
        Assertions.assertEquals(3, locations.size());

        Map<String, String> locationMap1 = locations.get(0);
        Assertions.assertEquals("New Zealand", locationMap1.get("country"));
        Assertions.assertEquals("123 Example Street", locationMap1.get("streetAddress"));
        Assertions.assertEquals("Christchurch", locationMap1.get("city"));
        Assertions.assertEquals("8041", locationMap1.get("postcode"));
        Assertions.assertEquals("Ilam", locationMap1.get("suburb"));
        Assertions.assertEquals("Ilam Christchurch New Zealand", locationMap1.get("outerLocation"));

        Map<String, String> locationMap2 = suggestions.get("locations").get(1);
        Assertions.assertEquals("New Zealand", locationMap2.get("country"));
        Assertions.assertEquals("125 Example Street", locationMap2.get("streetAddress"));
        Assertions.assertEquals("Christchurch", locationMap2.get("city"));
        Assertions.assertEquals("9000", locationMap2.get("postcode"));
        Assertions.assertEquals("Ilam", locationMap2.get("suburb"));
        Assertions.assertEquals("Ilam Christchurch New Zealand", locationMap2.get("outerLocation"));

        Map<String, String> locationMap3 = suggestions.get("locations").get(2);
        Assertions.assertEquals("Australia", locationMap3.get("country"));
        Assertions.assertEquals("123 Exemplar Road", locationMap3.get("streetAddress"));
        Assertions.assertEquals("Sydney", locationMap3.get("city"));
        Assertions.assertEquals("1234", locationMap3.get("postcode"));
        Assertions.assertEquals("Example", locationMap3.get("suburb"));
        Assertions.assertEquals("Example Sydney Australia", locationMap3.get("outerLocation"));
    }

    private Context createContext(String contextId, String contextText) {
        Context newContext = new Context();
        ReflectionTestUtils.setField(newContext, "id", contextId);
        ReflectionTestUtils.setField(newContext, "text", contextText);
        return newContext;
    }

    private Feature createFeature(String country, String city, String suburb, String postcode,
                                  String street, String streetNumber) {
        Feature feature  = new Feature();
        List<Context> contextList = new ArrayList<>();

        contextList.add(createContext("country", country));
        contextList.add(createContext("county", city));
        contextList.add(createContext("municipality", suburb));
        contextList.add(createContext("postal_code", postcode));

        ReflectionTestUtils.setField(feature, "context", contextList);

        ReflectionTestUtils.setField(feature, "text", street);
        ReflectionTestUtils.setField(feature, "address", streetNumber);

        return feature;
    }
}
