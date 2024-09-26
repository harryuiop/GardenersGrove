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

    public List<String> getPlantSuggestionsForGarden(Garden garden) {
        //Current default prompt for testing
        String prompt = "give me 3 plant suggestions for a christchurch (new zealand) garden";

        if (arduinoDataPointService.checkFourteenDaysOfData(garden.getId())) {
            // Create prompt and get suggestion based on Arduino data
            String arduinoPrompt = getArduinoPrompt(garden.getId());

            if (!arduinoPrompt.equals("Given me 3 plant suggestions given my garden has")) {
                try {
                    return parseSuggestions(getSuggestions(arduinoPrompt));
                } catch (ProfanityCheckingException e) {
                    logger.error(e.getMessage());
                    List<String> suggestions = new ArrayList<>();
                    suggestions.add("Invalid Response, no suggestions");
                    return suggestions;
                }
            }
            List<String> suggestions = new ArrayList<>();
            suggestions.add("Please Check Arduino Connection");
            return suggestions;
        } else if (garden.getLocation().isLocationRecognized()) {
           // Create prompt and get suggestion based on location
            List<String> suggestions = new ArrayList<>();
            suggestions.add("");
            return suggestions;
        } else {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("Please connect a device to your garden or update your location.");
            return suggestions;
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

    public String getArduinoPrompt(long gardenId) {
        List<String> sensors = new ArrayList<>(Arrays.asList("Temperature", "Moisture", "Light", "Air Pressure", "Humidity"));
        StringBuilder prompt = new StringBuilder("Given me 3 plant suggestions given my garden has");

        for (String sensor : sensors) {
            String unit;
            if (sensor.equals("Temperature")) {
                unit = "C";
            } else if (sensor.equals("Air Pressure")) {
                unit = "atm";
            } else {
                unit = "%";
            }
            Double max = arduinoDataPointService.getMaxValueInRange(gardenId, LocalDateTime.now().minusDays(14), sensor.toUpperCase());
            Double min = arduinoDataPointService.getMinValueInRange(gardenId, LocalDateTime.now().minusDays(14), sensor.toUpperCase());
            if (max != null && min != null) {
                prompt.append(", ").append(sensor).append(" between ").append(min).append(unit).append("-").append(max).append(unit);
            }
        }
        logger.info("Request sent to gemma: "+prompt);
        return prompt.toString();
    }

    public List<String> parseSuggestions(String response) {
        List<String> plants = new ArrayList<>();
        String[] splitResponse = response.split("Option");
        for (String environment : splitResponse) {
            String[] temp = environment.split("\\* \\*\\*");
            if (temp.length > 1) {
                String str = temp[1].replace("\n", " ").replace("** ", "\n");
                int index = str.indexOf(":");
                String before = str.substring(0, index+1);
                String after = str.substring(index+1);
                plants.add("<b>" + before + "</b>" + after);
            }
        }
        return  plants;
    }
}
