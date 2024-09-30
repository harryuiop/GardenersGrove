package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoValidSuggestions;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.GemmaException;
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
    private final String CONTEXT = "Give a response of 3 plants in the form 1. Plant Name : plant description, with no extra text before or after, suggestion plants that are suitable for these given environment factors; ";

    public GardenPlantSuggestions(ArduinoDataPointService arduinoDataPointService)  {
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

    public List<String> getPlantSuggestionsForGarden(Garden garden, boolean retry) throws ProfanityCheckingException {
        if (arduinoDataPointService.checkFourteenDaysOfData(garden.getId())) {
            // Create prompt and get suggestion based on Arduino data
            String arduinoPrompt = getArduinoPrompt(garden.getId());

            if (!arduinoPrompt.equals(CONTEXT)){
                return generateResponse(arduinoPrompt, retry);
            }
            List<String> suggestions = new ArrayList<>();
            suggestions.add("Please Check Arduino Connection");
            return suggestions;
        } else if (garden.getLocation().isLocationRecognized()) {
            String locationPrompt = String.format(
                    "give me 3 plant suggestions for a garden in %s" +
                    "[insert plant name]: [insert plant description that has 2-3 sentences]" +
                            "note: please do not include the texts 'Plant Name' or 'Plant Description'",
                    garden.getLocation());
            return generateResponse(locationPrompt, retry);
        } else {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("Please make sure devices has been connected for 14 days or more or update your location.");
            return suggestions;
        }
    }

    public static String getSuggestions(String prompt) throws GemmaException, InterruptedException {
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
        } catch (IOException exception) {
            throw new GemmaException();
        }

        List<String> responseList = Arrays.asList(responseMessage.body().split("\""));
        return responseList.get(11);
    }

    /**
     * Given a garden it builds a prompt to feed into Gemma based on max and min data points for each sensor and returns
     * the prompt.
     * @param gardenId  The garden id that plants are being suggested for
     * @return          The string to be feed into Gemma
     */
    public String getArduinoPrompt(long gardenId) {
        List<String> sensors = new ArrayList<>(Arrays.asList("Temperature", "Moisture", "Light", "Air Pressure", "Humidity"));
        StringBuilder prompt = new StringBuilder(CONTEXT);

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
        return prompt.toString();
    }

    /**
     * Given a response from Gemma it cuts it down into 3 plant suggestions with the name of the plant being put into
     * bold.
     * @param response  The response from Gemma to be parsed
     * @return          A list of 3 string descriptions of plants
     */
    public List<String> parseSuggestions(String response) {
        List<String> plants = new ArrayList<>();
        String[] splitResponse = response.split("[0-9]. ");
        for (String environment : splitResponse) {
                String str = environment.replace("\\n", "\n").replace("*", "");
                int index = str.indexOf(":");
                String before = str.substring(0, index+1);
                String after = str.substring(index+1);
                plants.add("<b>" + before + "</b>" + after);
        }
        return  plants;
    }

    public List<String> generateResponse(String prompt, Boolean retry) {
        try {
            String response = getSuggestions(prompt);
            while (!response.contains(":")) {
                logger.warn("Regenerate response");
                response = getSuggestions(prompt);
            }
            List<String> parsedResponse = parseSuggestions(response);

            if (parsedResponse.size() < 4 && retry){
                return generateResponse(prompt, false);
            } else if (parsedResponse.size() < 4) {
                throw new NoValidSuggestions("No valid plant suggestions");
            }
            return parsedResponse;
        } catch (Exception e) {
            logger.error(e.getMessage());
            List<String> suggestions = new ArrayList<>();
            suggestions.add("Invalid Response, no suggestions, try again later.");
            return suggestions;
        }
    }

}

