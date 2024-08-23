package nz.ac.canterbury.seng302.gardenersgrove.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find a friend request with the specified ID for the logged in user.")
public class NoSuchFriendRequestException extends Exception {

    public NoSuchFriendRequestException() {
        super("Unable to view the requested information as the current logged in user.");
    }

}
