package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

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
    public boolean checkBlank(String string) {
        return !string.isBlank();
    }

    /**
     * checks that if the size is not empty it is not negative
     *
     * @param size the size being checked
     * @return false if there is no error
     * @throws IllegalArgumentException
     */
    public boolean checkSize(Float size) {
        return size == null || size > 0;
    }

    public boolean checkDescription(String description) {
        return description == null || description.length() <= 512;
    }

    public boolean checkCount(Integer count) {
        return count == null || count > 0;
    }
}
