package nz.ac.canterbury.seng302.gardenersgrove.weather;

/**
 * A custom exception class that get thrown if the
 * weather data from a given weather monitoring API
 * cannot be fetched
 */
public class UnableToFetchWeatherException extends RuntimeException {
    public UnableToFetchWeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
