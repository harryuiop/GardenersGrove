package nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find a garden with the specified ID visible to the logged in user.")
public class NoSuchGardenException extends Exception {

    public NoSuchGardenException(long gardenId) {
        super("Unable to find garden with id " + gardenId + " visible to logged in user.");
    }

    public NoSuchGardenException() {
        super("Unable to view the requested information as the current logged in user.");
    }
}
