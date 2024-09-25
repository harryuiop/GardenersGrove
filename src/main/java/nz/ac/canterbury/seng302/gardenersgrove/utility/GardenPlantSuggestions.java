package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GardenPlantSuggestions {
    Logger logger = LoggerFactory.getLogger(GardenPlantSuggestions.class);

    ArduinoDataPointService arduinoDataPointService;

    public GardenPlantSuggestions(ArduinoDataPointService arduinoDataPointService) {
        this.arduinoDataPointService = arduinoDataPointService;
    }

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
        //Current default prompt for testing
        StringBuilder prompt = new StringBuilder("give me 3 plant suggestions for a christchurch (new zealand) garden");

        if (arduinoDataPointService.checkFourteenDaysOfData(garden.getId())) {
            // Create prompt and get suggestion based on Arduino data
            prompt = new StringBuilder("Given me 3 plant suggestions given my garden has");
            List<String> sensors = new ArrayList<>(Arrays.asList("Temperature", "Moisture", "Light", "Air Pressure", "Humidity"));
            for (String sensor : sensors) {
                String unit;
                if (sensor.equals("Temperature")) {
                    unit = "C";
                } else if (sensor.equals("Air Pressure")) {
                    unit = "atm";
                } else {
                    unit = "%";
                }
                Double max = arduinoDataPointService.getMaxValueInRange(garden.getId(), LocalDateTime.now().minusDays(14), sensor.toUpperCase());
                Double min = arduinoDataPointService.getMinValueInRange(garden.getId(), LocalDateTime.now().minusDays(14), sensor.toUpperCase());
                if (max != null && min != null) {
                    prompt.append(", ").append(sensor).append(" between ").append(min).append(unit).append("-").append(max).append(unit);
                }
            }
            logger.info("Request sent to gemma: "+prompt);

            if (prompt.toString().equals("Given me 3 plant suggestions given my garden has")) {
                try {
                    return getSuggestions(prompt.toString());
                } catch (ProfanityCheckingException e) {
                    logger.error(e.getMessage());
                    return "Profanity found";
                }
            }

            return "Please Check Arduino Connection";
        } else if (garden.getLocation().isLocationRecognized()) {
           // Create prompt and get suggestion based on location
            return "";
        } else {
            return "Please connect a device to your garden or update your location.";
        }
    }

    public static String getSuggestions(String prompt) throws ProfanityCheckingException {
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
        return responseList.get(11);
    }
}
