package nz.ac.canterbury.seng302.gardenersgrove.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not get a response form Gemma.")
public class GemmaException extends Exception {
    public GemmaException() {
        super("Could not get a response form Gemma.");
    }
}
