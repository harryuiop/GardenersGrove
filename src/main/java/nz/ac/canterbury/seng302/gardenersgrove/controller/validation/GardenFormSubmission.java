package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Checks the validity of the entries into the garden form
 */
public class GardenFormSubmission {

    private static final String GARDEN_NAME_ERROR = "gardenNameError";
    private static final String GARDEN_LOCATION_ERROR = "gardenLocationError";
    private static final String GARDEN_SIZE_ERROR = "gardenSizeError";

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
        return true;
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
        return true;
    }

        /**
         * Checks for valid user entries that meet the given requirements
         * @param gardenName represents the name given
         * @param gardenLocation represents the location given
         * @param gardenSize represents the size given
         * @return a mapping of the error labels and messages
         */
    public Map<String, String> formErrors(String gardenName, String gardenLocation, Float gardenSize) {
        HashMap<String, String> errors = new HashMap<>();

        try {
            this.checkName(gardenName);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Blank")) {
                errors.put(GARDEN_NAME_ERROR, "Garden name cannot by empty");
            } else if (e.getMessage().equals("InvalidChar")) {
                errors.put(
                        GARDEN_NAME_ERROR,
                        "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
            }
        }

        try {
            this.checkName(gardenLocation);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Blank")) {
                errors.put(GARDEN_LOCATION_ERROR, "Location cannot be empty");
            } else if (e.getMessage().equals("InvalidChar")) {
                errors.put(
                        GARDEN_LOCATION_ERROR,
                        "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
            }
        }

        try {
            this.checkSize(gardenSize);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Negative")) {
                errors.put(GARDEN_SIZE_ERROR, "Garden size must be a positive number");
            }
        }
        return errors;
    }

    /**
     * Add error attributes to given model.
     *
     * @param model Model in thyme leaf to add to.
     * @param errors Errors hashmap.
     */
    public void addErrorAttributes(Model model, Map<String, String> errors) {
        for (Map.Entry<String, String> error : errors.entrySet()) {
            model.addAttribute(error.getKey(), error.getValue());
        }
    }
}
