package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import org.h2.util.json.JSONObject;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;


public class GardenPlantSuggestions {

    // Decide if there is an arduino connected
    // Pass to the correct method to deal with what data we have
    // if no arduino data
        //
    // if we no location and no arduino
        //
    // else no location and no data
        //
    // pass back a string with suggestions

    public String getPlantSuggestionsForGarden(Garden garden) {
        return "";
    }

    public String getSuggestionsWithData(Garden garden) {
        return "";
    }

    public static String getSuggestionsWithLocation(Garden garden) throws ProfanityCheckingException {
        String prompt = "give me 3 plant suggestions for a christchurch (new zealand) garden";

        URI uri = new DefaultUriBuilderFactory().builder()
                .scheme("http")
                .host("localhost")
                .port(11434)
                .path("/api/generate")
                .build();

        // Create the JSON request body
        String jsonInput = "{\"model\":\"gemma2:2b\",\"prompt\":\"" + prompt + "\",\"stream\":false}";

        // Create the POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();

        HttpResponse<String> responseMessage;
        try (HttpClient client = HttpClient.newHttpClient()) {
            responseMessage = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new ProfanityCheckingException("Failed to check for profanity", exception);
        }

        List<String> responseList = Arrays.asList(responseMessage.body().split("\""));
        System.out.println(responseList);
        return responseList.get(11);
    }
}
