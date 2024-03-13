package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Checks the validity of the entries into the garden form
 */
public class GardenFormSubmission {
    private FormValuesValidator valuesValidator = new FormValuesValidator();

    private static final String GARDEN_NAME_ERROR = "gardenNameError";
    private static final String GARDEN_LOCATION_ERROR = "gardenLocationError";
    private static final String GARDEN_SIZE_ERROR = "gardenSizeError";

        /**
         * Checks for valid user entries that meet the given requirements
         * @param gardenName represents the name given
         * @param gardenLocation represents the location given
         * @param gardenSize represents the size given
         * @return a mapping of the error labels and messages
         */
    public Map<String, String> formErrors(String gardenName, String gardenLocation, Float gardenSize) {
        HashMap<String, String> errors = new HashMap<>();

        if (valuesValidator.checkName(gardenName) == "Blank") {
            errors.put(GARDEN_NAME_ERROR, "Garden name cannot by empty");
        } else if (valuesValidator.checkName(gardenName) == "InvalidChar") {
            errors.put(
                    GARDEN_NAME_ERROR,
                    "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (valuesValidator.checkName(gardenLocation) == "Blank") {
            errors.put(GARDEN_LOCATION_ERROR, "Location cannot be empty");
        } else if (valuesValidator.checkName(gardenLocation) == "InvalidChar") {
            errors.put(
                    GARDEN_LOCATION_ERROR,
                    "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (valuesValidator.checkSize(gardenSize) == "Negative") {
                errors.put(GARDEN_SIZE_ERROR, "Garden size must be a positive number");
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
