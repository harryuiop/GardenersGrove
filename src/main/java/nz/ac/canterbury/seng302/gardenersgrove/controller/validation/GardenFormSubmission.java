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

    public boolean checkName(String string) throws IllegalArgumentException {
        if (string.isBlank()) {
            throw new IllegalArgumentException("Blank");
        } else if (!checkString(string)) {
            throw new IllegalArgumentException("InvalidChar");
        }
        return false;
    }

    public boolean checkSize (Float size) throws IllegalArgumentException {
        if (size != null && size <= 0) {
            throw new IllegalArgumentException("Negative");
        }
        return false;
    }
}
