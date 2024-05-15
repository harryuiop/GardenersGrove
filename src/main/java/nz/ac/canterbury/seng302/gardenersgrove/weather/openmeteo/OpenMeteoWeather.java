package nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.weather.UnableToFetchWeatherException;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OpenMeteoWeather implements WeatherService {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Gets the json data from the Open Meteo API
     *
     * @param latitude  specified by the location of the garden
     * @param longitude specified by the location of the garden
     * @return a string containing the JSON data required
     * @throws InterruptedException          if the Http request send operation is interrupted
     * @throws UnableToFetchWeatherException if the server request fails
     */
    private String getJsonFromApi(double latitude, double longitude) throws InterruptedException {
        URI uri = new DefaultUriBuilderFactory().builder()
                .scheme("https")
                .host("api.open-meteo.com")
                .path("v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("hourly", "relative_humidity_2m")
                .queryParam("current", "temperature_2m,relative_humidity_2m,weather_code")
                .queryParam("daily", "weather_code,temperature_2m_max")
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

    /**
     * Gets the weather data from the API and converts the weather response
     * to a list of ForecastWeatherData objects
     *
     * @param latitude  specified by the location of the garden
     * @param longitude specified by the location of the garden
     * @return a converted weather response to a list of ForecastWeatherData objects
     * @throws InterruptedException          if the Http request send operation is interrupted
     * @throws UnableToFetchWeatherException if the server request fails or JSON parsing fails
     */
    @Override
    public List<WeatherData> getWeatherData(double latitude, double longitude) throws InterruptedException {
        try {
            WeatherResponse response = objectMapper.readValue(getJsonFromApi(latitude, longitude), WeatherResponse.class);
            return convertWeatherResponse(response);
        } catch (JsonProcessingException exception) {
            throw new UnableToFetchWeatherException("Failed to parse JSON response from API", exception);
        }
    }

    /**
     * Converts the weather response to a list of ForecastWeatherData objects
     *
     * @param response a response from the Open-Meteo API
     * @return an array list of ForecastWeatherData objects
     */
    private List<WeatherData> convertWeatherResponse(WeatherResponse response) {
        ArrayList<WeatherData> dataArrayList = new ArrayList<>();
        List<String> timeStamps = response.getDailyWeather().getTimeStamps();
        List<Double> temps = response.getDailyWeather().getMaximumTemperatures();
        List<Integer> weatherCodes = response.getDailyWeather().getWeatherCodes();
        List<Integer> dailyHumidity = getDailyHumidity(response);
        for (int i = 0; i < timeStamps.size(); i++) {
            LocalDate date = LocalDate.parse(timeStamps.get(i));
            dataArrayList.add(new WeatherData(
                    date,
                    temps.get(i),
                    WeatherResponse.weatherCodes.get(weatherCodes.get(i))[0],
                    dailyHumidity.get(i)
            ));
        }

        // add the current weather data into the array list
        double currentTemp = response.getCurrentWeather().getTemperature2m();
        LocalDate currentDate = LocalDateTime.parse(response.getCurrentWeather().getTime()).toLocalDate();
        String currentDescription = WeatherResponse.weatherCodes.get(response.getCurrentWeather().getWeatherCode())[0];
        int currentHumidity = response.getCurrentWeather().getRelativeHumidity2m();
        WeatherData currentWeatherData = new WeatherData(currentDate, currentTemp, currentDescription, currentHumidity);
        dataArrayList.set(2, currentWeatherData);
        return dataArrayList;
    }

    /**
     * Gets and calculates daily humidity from the hourly humidity
     * values by averaging them over a 24-hour period as specified
     *
     * @param response a response from the Open-Meteo API
     * @return a list of calculated averages representing the dailyHumidity
     */
    private List<Integer> getDailyHumidity(WeatherResponse response) {
        List<Integer> dailyHumidity = new ArrayList<>();
        List<Integer> hourlyHumidity = response.getHourlyWeather().getRelativeHumidity();
        final int NUM_DAYS = 6; // number of days of data to be fetched according to the AC's
        final int NUM_HOURS = 24; // number of hours in a day
        int humiditySum = 0;
        for (int i = 0; i < NUM_DAYS; i++) {
            int humidityAvg;
            for (int j = 0; j < NUM_HOURS; j++) {
                humiditySum += hourlyHumidity.get(i * NUM_HOURS + j);
            }
            humidityAvg = humiditySum / NUM_HOURS;
            humiditySum = 0;
            dailyHumidity.add(humidityAvg);
        }
        return dailyHumidity;
    }
}
