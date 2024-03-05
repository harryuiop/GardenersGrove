package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import java.util.HashMap;

public class FormSubmission {

    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }
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
