package nz.ac.canterbury.seng302.gardenersgrove.weather;

import java.util.List;

/**
 * An interface for classes dedicated to any given weather
 * API that specify how they should fetch weather data
 */
public interface WeatherService {

    /**
     * Gets a list of six days of weather data consisting data from
     * 2 past days, the current day, and 3 future days
     *
     * @param longitude specified by the location of the garden
     * @param latitude  specified by the location of the garden
     * @return a list of weather data items from the API
     * @throws InterruptedException          if the Http request send operation is interrupted
     * @throws UnableToFetchWeatherException if the server request fails or JSON parsing fails
     */
    List<WeatherData> getWeatherData(double longitude, double latitude) throws InterruptedException;
    String getWeatherAdvice(List<WeatherData> weatherData);

    /**
     * @return The number of forecasted days.
     */
    int getPastDaysCount();

    /**
     * @return The number of previous days.
     */
    int getForecastDayCount();

    boolean isRainy(List<WeatherData> weatherData);
}
