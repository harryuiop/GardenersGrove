package nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find a garden with the specified ID visible to the logged in user.")
public class NoSuchFriendException extends Exception {
    public NoSuchFriendException(long friendId) {
        super("You do not have a friend with id " + friendId + ".");
    }

}
