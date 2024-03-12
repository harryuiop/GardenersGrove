package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

public class FormValuesValidator {
    /**
     * Checks the String contains valid characters
     *
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Checks whether the name meets the given standards
     *
     * @param string represents the string being checked
     * @return false to show there are no errors
     * @throws IllegalArgumentException
     */
    public String checkName(String string) {
        if (string.isBlank()) {
            return "Blank";
        } else if (!checkString(string)) {
            return "InvalidChar";
        }
        return null;
    }

    /**
     * checks that if the size is not empty it is not negative
     *
     * @param size the size being checked
     * @return false if there is no error
     * @throws IllegalArgumentException
     */
    public String checkSize(Float size) {
        if (size != null && size <= 0) {
            return "Negative";
        }
        return null;
    }
}
