package nz.ac.canterbury.seng302.gardenersgrove.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.weather.UnableToFetchWeatherException;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WeatherService {
    Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String getJsonFromApi(float latitude, float longitude) throws InterruptedException {
        URI uri = new DefaultUriBuilderFactory().builder()
                .scheme("https")
                .host("api.open-meteo.com")
                .path("v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("hourly", "temperature_2m,weather_code")
                .queryParam("timezone", "auto")
                .queryParam("past_days", 2)
                .queryParam("forecast_days", 3)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newHttpClient();
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException exception) {
            throw new UnableToFetchWeatherException("IOException occurred while getting weather data from API", exception);
        }
    }

    public WeatherResponse getWeatherData(float latitude, float longitude) throws InterruptedException {
        try {
            return objectMapper.readValue(getJsonFromApi(latitude, longitude), WeatherResponse.class);
        } catch (JsonProcessingException exception) {
            throw new UnableToFetchWeatherException("Failed to parse JSON response from API", exception);
        }
    }
}
