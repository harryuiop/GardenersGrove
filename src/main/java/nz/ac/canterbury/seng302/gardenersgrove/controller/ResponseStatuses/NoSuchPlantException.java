package nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Plant with specified ID Not Found")
public class NoSuchPlantException extends Exception {

    public NoSuchPlantException(String msg) {
        super(msg);
    }
}
