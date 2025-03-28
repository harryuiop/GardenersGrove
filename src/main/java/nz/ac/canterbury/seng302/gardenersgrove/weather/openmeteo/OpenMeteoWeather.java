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
import java.util.Arrays;
import java.util.List;


/**
 * This is a class that implements the WeatherService interface
 * and provides the methods specific to the Open-Meteo API that
 * fetch and deserialize weather data.
 */
@Component
public class OpenMeteoWeather implements WeatherService {

    private final int NUM_HOURS_IN_DAY = 24;
    private final int PAST_DAYS = 2;
    private final int FORECAST_DAYS = 3;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final List<String> sunnyDescription = Arrays.asList("Clear sky", "Mainly clear", "Partly cloudy");
    private final List<String> otherDescription = Arrays.asList("Overcast", "Fog", "Depositing rine fog");

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
                .queryParam("past_days", PAST_DAYS)
                .queryParam("forecast_days", FORECAST_DAYS)
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
     * @return The number of forecasted days.
     */
    public int getForecastDayCount() {
        return FORECAST_DAYS;
    }

    /**
     * @return The number of previous days.
     */
    public int getPastDaysCount() {
        return PAST_DAYS;
    }

    /**
     * Gets the weather data from the API and converts the weather response
     * to a list of ForecastWeatherData objects
     *
     * @param longitude specified by the location of the garden
     * @param latitude  specified by the location of the garden
     * @return a converted weather response to a list of ForecastWeatherData objects
     * @throws InterruptedException          if the Http request send operation is interrupted
     * @throws UnableToFetchWeatherException if the server request fails or JSON parsing fails
     */
    @Override
    public List<WeatherData> getWeatherData(double longitude, double latitude) throws InterruptedException {
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
     * @return an array list of WeatherData objects
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
                    dailyHumidity.get(i),
                    WeatherResponse.weatherCodes.get(weatherCodes.get(i))[1]
            ));
        }

        // add the current weather data into the array list
        double currentTemp = response.getCurrentWeather().getTemperature2m();
        LocalDate currentDate = LocalDateTime.parse(response.getCurrentWeather().getTime()).toLocalDate();
        String currentDescription = WeatherResponse.weatherCodes.get(response.getCurrentWeather().getWeatherCode())[0];
        String weatherIconName = WeatherResponse.weatherCodes.get(response.getCurrentWeather().getWeatherCode())[1];
        int currentHumidity = response.getCurrentWeather().getRelativeHumidity2m();
        WeatherData currentWeatherData = new WeatherData(currentDate, currentTemp, currentDescription,
                currentHumidity, weatherIconName);
        dataArrayList.set(2, currentWeatherData);
        return dataArrayList;
    }

    /**
     * Gets and calculates daily humidity from the hourly humidity
     * values by averaging them over a 24-hour period as specified
     *
     * @param response a response from the Open-Meteo API
     * @return a list of calculated averages representing the daily humidity
     */
    private List<Integer> getDailyHumidity(WeatherResponse response) {
        List<Integer> dailyHumidity = new ArrayList<>();
        List<Integer> hourlyHumidity = response.getHourlyWeather().getRelativeHumidity();
        int humiditySum = 0;
        for (int i = 0; i < PAST_DAYS + FORECAST_DAYS; i++) {
            int humidityAvg;
            for (int j = 0; j < NUM_HOURS_IN_DAY; j++) {
                humiditySum += hourlyHumidity.get(i * NUM_HOURS_IN_DAY + j);
            }
            humidityAvg = humiditySum / NUM_HOURS_IN_DAY;
            humiditySum = 0;
            dailyHumidity.add(humidityAvg);
        }
        return dailyHumidity;
    }

    /**
     * This method takes the description from the last two days of weather for a specific garden
     * and adds the correct watering advice into the model
     *
     * @param weatherData List of weather data from the Open Meteo API
     */
    @Override
    public String getWeatherAdvice(List<WeatherData> weatherData) {

        List<String> weatherDescriptions = this.getWeatherDescription(weatherData);

        // Check to see if past two consecutive days match sunny or rainy weather descriptions
        if (sunnyDescription.contains(weatherDescriptions.get(0)) && sunnyDescription.contains(weatherDescriptions.get(1))) {
            return "There hasn’t been any rain recently, make sure to water your plants if they need it";
        } else if (checkWeatherIsRainy(weatherDescriptions)) {
            return "Outdoor plants don’t need any water today";
        } else {
            return "Have you checked on your garden today?";
        }
    }

    /**
     * This method checks if the weather is rainy
     *
     * @param weatherData List of weather data forwarded from the Open Meteo API
     * @return true if the weather is rainy, false otherwise
     */
    @Override
    public boolean isRainy(List<WeatherData> weatherData) {

        List<String> weatherDescriptions = getWeatherDescription(weatherData);

        return checkWeatherIsRainy(weatherDescriptions);
    }

    /**
     * This method checks weatherDescriptions if the weather is rainy
     *
     * @param weatherDescriptions List of weather descriptions
     * @return true if the weather is rainy, false otherwise
     */
    private boolean checkWeatherIsRainy(List<String> weatherDescriptions) {
        return !sunnyDescription.contains(weatherDescriptions.get(2)) && !otherDescription.contains(weatherDescriptions.get(2));
    }


    /**
     * This method gets the weather description from the weather data
     *
     * @param weatherData List of weather data forwarded from the Open Meteo API
     * @return a list of detail about the weather description
     */
    private List<String> getWeatherDescription(List<WeatherData> weatherData) {

        List<String> weatherDescriptions = new ArrayList<>();

        weatherDescriptions.add(weatherData.get(0).getWeatherDescription());
        weatherDescriptions.add(weatherData.get(1).getWeatherDescription());
        weatherDescriptions.add(weatherData.get(2).getWeatherDescription());

        return weatherDescriptions;
    }
}
