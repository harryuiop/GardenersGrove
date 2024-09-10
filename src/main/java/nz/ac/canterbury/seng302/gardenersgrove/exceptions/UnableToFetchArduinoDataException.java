package nz.ac.canterbury.seng302.gardenersgrove.exceptions;

/**
 * A custom exception class that get thrown if the
 * data received from the Arduino cannot be fetched
 */
public class UnableToFetchArduinoDataException extends RuntimeException {
  public UnableToFetchArduinoDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
