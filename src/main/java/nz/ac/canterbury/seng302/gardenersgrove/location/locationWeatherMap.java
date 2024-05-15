package nz.ac.canterbury.seng302.gardenersgrove.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class locationWeatherMap {
    public static HashMap<Integer, String[]> weatherCodes = new HashMap<>();
    static {
        weatherCodes.put(0, new String[]{"Clear sky", "bi bi-brightness-high"});
        weatherCodes.put(1, new String[]{"Mainly clear", "bi bi-brightness-low"});
        weatherCodes.put(2, new String[]{"Partly cloudy", "bi bi-cloud-sun"});
        weatherCodes.put(3, new String[]{"Overcast", "bi bi-clouds"});
        weatherCodes.put(45, new String[]{"Fog", "bi bi-cloud-fog2"});
        weatherCodes.put(48, new String[]{"Depositing rine fog", "bi bi-cloud-fog2-fill"});
        weatherCodes.put(51, new String[]{"Light drizzle", "bi bi-cloud-drizzle"});
        weatherCodes.put(53, new String[]{"Moderate drizzle", "bi bi-cloud-drizzle"});
        weatherCodes.put(55, new String[]{"Dense drizzle", "bi bi-cloud-drizzle-fill"});
        weatherCodes.put(56, new String[]{"Light freezing drizzle", "bi bi-cloud-sleet"});
        weatherCodes.put(57, new String[]{"Dense freezing drizzle", "bi bi-cloud-sleet-fill"});
        weatherCodes.put(61, new String[]{"Slight rain", "bi bi-cloud-rain"});
        weatherCodes.put(63, new String[]{"Moderate rain", "bi bi-cloud-rain"});
        weatherCodes.put(65, new String[]{"Heavy rain", "bi bi-cloud-rain-fill"});
        weatherCodes.put(67, new String[]{"Moderate rain", "bi bi-cloud-rain-fill"});
        weatherCodes.put(71, new String[]{"Slight snow fall", "bi bi-snow"});
        weatherCodes.put(73, new String[]{"Moderate snow fall", "bi bi-snow3"});
        weatherCodes.put(75, new String[]{"Heavy snow fall", "bi bi-snow2"});
        weatherCodes.put(77, new String[]{"Snow grains", "bi bi-snow2"});
        weatherCodes.put(80, new String[]{"Slight rain showers", "bi bi-cloud-rain"});
        weatherCodes.put(81, new String[]{"Moderate rain showers", "bi bi-cloud-rain"});
        weatherCodes.put(82, new String[]{"Violent rain showers", "bi bi-cloud-rain-fill"});
        weatherCodes.put(85, new String[]{"Light snow showers", "bi bi-cloud-snow"});
        weatherCodes.put(86, new String[]{"Heavy snow showers", "bi bi-cloud-snow-fill"});
        weatherCodes.put(95, new String[]{"Moderate thunderstorm", "bi bi-lightning"});
        weatherCodes.put(96, new String[]{"Thunderstorm with light hail", "bi bi-cloud-lightning-rain"});
        weatherCodes.put(99, new String[]{"Thunderstorm with heavy hail", "bi bi-cloud-lightning-rain-fill"});
    }
}
