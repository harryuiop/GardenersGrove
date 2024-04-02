package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Checks the validity of the entries into the garden form
 */
public class ErrorChecker {

    private final FormValuesValidator valuesValidator = new FormValuesValidator();

    /**
     * Checks for valid user entries that meet the given requirements
     *
     * @param gardenName     represents the name given
     * @param gardenLocation represents the location given
     * @param gardenSize     represents the size given
     * @return a mapping of the error labels and messages
     */
    public Map<String, String> gardenFormErrors(String gardenName, String gardenLocation, Float gardenSize) {
        HashMap<String, String> errors = new HashMap<>();

        if (!valuesValidator.checkBlank(gardenName)) {
            errors.put("gardenNameError", "Garden name cannot by empty");
        } else if (!valuesValidator.checkCharacters(gardenName)) {
            errors.put(
                            "gardenNameError",
                            "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (!valuesValidator.checkBlank(gardenLocation)) {
            errors.put("gardenLocationError", "Location cannot be empty");
        } else if (!valuesValidator.checkCharacters(gardenLocation)) {
            errors.put(
                            "gardenLocationError",
                            "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (!valuesValidator.checkSize(gardenSize)) {
            errors.put("gardenSizeError", "Garden size must be a positive number");
        }

        return errors;
    }


    public Map<String, String> plantFormErrors(
                    String plantName,
                    Integer plantCount,
                    String plantDescription
    ) {
        HashMap<String, String> errors = new HashMap<>();

        if (!valuesValidator.checkBlank(plantName) || !valuesValidator.checkCharacters(plantName)) {
            errors.put(
                            "plantNameError",
                            "Plant name cannot be empty and must only include letters, numbers, spaces, dots, hyphens or apostrophes");
        }

        if (!valuesValidator.checkCount(plantCount)) {
            errors.put("plantCountError", "Plant count must be positive number");
        }

        if (!valuesValidator.checkDescription(plantDescription)) {
            errors.put("plantDescriptionError", "Plant description must be less than 512 characters");
        }

        return errors;
    }
}
