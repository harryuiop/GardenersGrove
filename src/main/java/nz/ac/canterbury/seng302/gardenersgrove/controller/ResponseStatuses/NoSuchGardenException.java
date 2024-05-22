package nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find a garden with the specified ID owned by the logged in user.")
public class NoSuchGardenException extends Exception {

    public NoSuchGardenException(long gardenId) {
        super("Unable to find garden with id " + gardenId + " owned by logged in user.");
    }
}
