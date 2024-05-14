package nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.weather.ForecastWeatherData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.UnableToFetchWeatherException;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class OpenMeteoWeather implements WeatherService
{
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
                .queryParam("hourly", "relative_humidity_2m")
                .queryParam("current", "temperature_2m,relative_humidity_2m,weather_code")
                .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                .queryParam("timezone", "auto")
                .queryParam("past_days", 2)
                .queryParam("forecast_days", 4)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException exception) {
            throw new UnableToFetchWeatherException("IOException occurred while getting weather data from API", exception);
        }
    }

    private List<ForecastWeatherData> convertWeatherResponse(WeatherResponse response) {
        ArrayList<ForecastWeatherData> dataArrayList = new ArrayList<>();
        List<String> timeStamps = response.getDailyWeather().getTimeStamps();
        List<Double> maxTemps = response.getDailyWeather().getMaximumTemperatures();
        List<Double> minTemps = response.getDailyWeather().getMinimumTemperatures();
        List<Integer> weatherCodes = response.getDailyWeather().getWeatherCodes();
        for (int i=0; i < timeStamps.size(); i++) {
            dataArrayList.add(new ForecastWeatherData(
                    LocalDate.parse(timeStamps.get(i)),
                    maxTemps.get(i),
                    minTemps.get(i),
                    WeatherResponse.weatherCodes.get(weatherCodes.get(i))[0]
            ));
        }
        return dataArrayList;
    }

    public List<ForecastWeatherData> getWeatherData(float latitude, float longitude) throws InterruptedException {
        try {
            WeatherResponse response = objectMapper.readValue(getJsonFromApi(latitude, longitude), WeatherResponse.class);
            return convertWeatherResponse(response);
        } catch (JsonProcessingException exception) {
            throw new UnableToFetchWeatherException("Failed to parse JSON response from API", exception);
        }
    }
}
