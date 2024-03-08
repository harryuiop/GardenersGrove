package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.util.HashMap;

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
     * Checks for valid user entries that meet the given requirements
     * @param gardenName represents the name given
     * @param gardenLocation represents the location given
     * @param gardenSize represents the size given
     * @return a mapping of the error labels and messages
     */
    public HashMap<String, String> formErrors(String gardenName, String gardenLocation, Float gardenSize) {
        HashMap<String, String> errors = new HashMap<>();
        if (gardenName.isBlank()) {
            errors.put("gardenNameError", "Garden name cannot by empty");
        } else if (!checkString(gardenName)) {
            errors.put(
                    "gardenNameError",
                    "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }
        if (gardenLocation.isBlank()) {
            errors.put("gardenLocationError", "Location cannot be empty");
        } else if (!checkString(gardenLocation)) {
            errors.put(
                    "gardenLocationError",
                    "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
            );
        }
        if (gardenSize != null && gardenSize <= 0) {
            errors.put("gardenSizeError", "Garden size must be a positive number");
        }
        return errors;
    }
}
