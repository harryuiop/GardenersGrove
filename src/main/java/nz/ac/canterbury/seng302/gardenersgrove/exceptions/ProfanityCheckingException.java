package nz.ac.canterbury.seng302.gardenersgrove.exceptions;

public class ProfanityCheckingException extends Exception {
    public ProfanityCheckingException(String message, Exception exception) {
        super(message, exception);
    }

    public ProfanityCheckingException(String message) {
        super(message);
    }
}
