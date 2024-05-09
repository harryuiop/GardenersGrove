package nz.ac.canterbury.seng302.gardenersgrove.weather;

public class UnableToFetchWeatherException extends RuntimeException {
    public UnableToFetchWeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
