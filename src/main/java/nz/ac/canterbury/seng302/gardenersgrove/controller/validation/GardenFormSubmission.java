package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

/**
 * Checks the validity of the entries into the garden form
 */
public class GardenFormSubmission {
    /**
     * Checks the String contains valid characters
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Checks whether the name meets the given standards
     * @param string represents the string being checked
     * @return false to show there are no errors
     * @throws IllegalArgumentException
     */
    public boolean checkName(String string) throws IllegalArgumentException {
        if (string.isBlank()) {
            throw new IllegalArgumentException("Blank");
        } else if (!checkString(string)) {
            throw new IllegalArgumentException("InvalidChar");
        }
        return false;
    }

    /**
     * checks that if the size is not empty it is not negative
     * @param size the size being checked
     * @return false if there is no error
     * @throws IllegalArgumentException
     */
    public boolean checkSize (Float size) throws IllegalArgumentException {
        if (size != null && size <= 0) {
            throw new IllegalArgumentException("Negative");
        }
        return false;
    }
}
