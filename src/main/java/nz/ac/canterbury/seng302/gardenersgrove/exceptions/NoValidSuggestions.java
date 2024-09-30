package nz.ac.canterbury.seng302.gardenersgrove.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No valid plant suggestions")
public class NoValidSuggestions extends Exception{
    public NoValidSuggestions(String msg) {
        super(msg);
    }

}
