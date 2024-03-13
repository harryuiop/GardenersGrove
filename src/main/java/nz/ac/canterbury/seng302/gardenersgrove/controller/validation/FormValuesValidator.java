package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class FormValuesValidator {
    /**
     * Checks the String contains valid characters
     *
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public boolean checkCharacters(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Checks whether the name meets the given standards
     *
     * @param string represents the string being checked
     * @return false to show there are no errors
     * @throws IllegalArgumentException
     */
    public Boolean checkBlank(String string) {
        return !string.isBlank();
    }

    /**
     * checks that if the size is not empty it is not negative
     *
     * @param size the size being checked
     * @return false if there is no error
     * @throws IllegalArgumentException
     */
    public Boolean checkSize(Float size) {
        if (size != null && size <= 0) {
            return false;
        }
        return true;
    }

    public Boolean checkDescription(String description) {
        return description.length() <= 512;
    }

    public Boolean checkCount(Integer count) {
        return count > 0;
    }
}
