package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Checks the validity of the entries into the garden form
 */
public class ErrorChecker {
    /**
     * Contains functions to verify the form entries are in correct structure according to acceptance criteria
     */
    private final FormValuesValidator valuesValidator = new FormValuesValidator();
    /**
     * Contains functions to verify user form entries are correctly structured according to accepance criteria
     */
    private final UserValidation userValidator = new UserValidation();

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


    public Map<String, String> plantFormErrors(String plantName, Integer plantCount, String plantDescription) {
        HashMap<String, String> errors = new HashMap<>();

        if (!valuesValidator.checkBlank(plantName)||!valuesValidator.checkCharacters(plantName)) {
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

    public Map<String, String> registerUserFormErrors(String firstName, String lastName, Boolean noSurname, String email, String address,
                                                      String password, String passwordConfirm, String dateOfBirth)
    {
        Map<String, String> errors = new HashMap<>();
        // Checking first name
        if (!valuesValidator.checkBlank(firstName)) {
            errors.put("firstNameError", "First Name cannot be empty");
        } else if (!valuesValidator.checkNameLength(firstName)){
            errors.put("firstNameError", "First Name cannot exceed length of 64 characters");
        } else if (!valuesValidator.checkUserName(firstName)) {
            errors.put("firstNameError", "First name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        }
        // Checking last name
        if (!noSurname) {
            if (!valuesValidator.checkBlank(lastName)) {
                errors.put("lastNameError", "Last Name cannot be empty unless box is ticked");
            } else if (!valuesValidator.checkNameLength(lastName)) {
                errors.put("lastNameError", "Last Name cannot exceed length of 64 characters");
            } else if (!valuesValidator.checkUserName(lastName)) {
                errors.put("lastNameError", "Last Name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
            }
        }
        // Checking email
        if (!valuesValidator.checkBlank(email)) {
            errors.put("emailError", "Email cannot be empty");
        } else if (!userValidator.emailIsValid(email)) {
            errors.put("emailError", "This is not a valid email address");
        }
        // Checking password
        if (!valuesValidator.checkBlank(password)) {
            errors.put("passwordError", "Password cannot be empty");
        } else if (!userValidator.passwordIsValid(password)) {
            errors.put("passwordError", "Password must contain...");
        } else if (!valuesValidator.checkConfirmPasswords(password, passwordConfirm)) {
            errors.put("passwordConfirmError", "Passwords are not equal");
        }
        // Checking DOB
        if (!valuesValidator.checkBlank(dateOfBirth)) {
            errors.put("dateOfBirthError", "Date of Birth cannot be empty");
        }


        return errors;
    }
}
