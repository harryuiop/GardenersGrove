package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation.*;

/**
 * Checks the validity of the entries into the garden form
 */
public class ErrorChecker {

    /**
     * Checks for valid user entries that meet the given requirements
     *
     * @param gardenName     represents the name given
     * @param gardenLocation represents the location given
     * @param gardenSize     represents the size given
     * @return a mapping of the error labels and messages
     */
    public static Map<String, String> gardenFormErrors(String gardenName, String gardenLocation, Float gardenSize) {
        Map<String, String> errors = new HashMap<>();

        if (!FormValuesValidator.checkBlank(gardenName)) {
            errors.put("gardenNameError", "Garden name cannot by empty");
        } else if (!FormValuesValidator.checkCharacters(gardenName)) {
            errors.put(
                            "gardenNameError",
                            "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (!FormValuesValidator.checkBlank(gardenLocation)) {
            errors.put("gardenLocationError", "Location cannot be empty");
        } else if (!FormValuesValidator.checkCharacters(gardenLocation)) {
            errors.put(
                            "gardenLocationError",
                            "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (!FormValuesValidator.checkSize(gardenSize)) {
            errors.put("gardenSizeError", "Garden size must be a positive number");
        }

        return errors;
    }

    /**
     * Checks the provided information for adding plants to a garden.
     *
     * @param plantName        The string name of the plant.
     * @param plantCount       The integer number of plants to be added.
     * @param plantDescription The string description of the plant.
     * @return A HashMap<String, String> of the errors that have occurred based on the outcome of the error checks.
     */
    public static Map<String, String> plantFormErrors(
                    String plantName,
                    Integer plantCount,
                    String plantDescription,
                    MultipartFile imageFile
    ) {
        HashMap<String, String> errors = new HashMap<>();

        if (!FormValuesValidator.checkBlank(plantName) || !FormValuesValidator.checkCharacters(plantName)) {
            errors.put(
                            "plantNameError",
                            "Plant name cannot be empty and must only include " +
                                            "letters, numbers, spaces, dots, hyphens, or apostrophes"
            );
        }

        if (!FormValuesValidator.checkCount(plantCount)) {
            errors.put("plantCountError", "Plant count must be positive number");
        }

        if (!FormValuesValidator.checkDescription(plantDescription)) {
            errors.put("plantDescriptionError", "Plant description must be less than 512 characters");
        }

        ImageValidator imageValidator = new ImageValidator(imageFile);
        if (imageFile != null && !imageFile.isEmpty() && !imageValidator.isValid()) {
            errors.putAll(imageValidator.getErrorMessages());
        }

        return errors;
    }

    /**
     * Checks the provided information on the register page. If any fields are invalid, it returns
     * a map of errors and their descriptions.
     *
     * @param firstName       The first name string.
     * @param lastName        The last name string.
     * @param noSurname       Whether the noLastName box was ticked.
     * @param email           The email string.
     * @param password        The password string.
     * @param passwordConfirm The confirm password string.
     * @param validDate       Whether the date is valid or not.
     * @param dateOfBirth     The date of birth string.
     * @param userService     The userService instance used to query the database for checking whether an account
     *                        exists with the given information.
     * @return A HashMap<String, String> of the errors that have occurred based on the outcome of the error checks.
     */
    public static Map<String, String> registerUserFormErrors(String firstName, String lastName, Boolean noSurname, String email,
                                                             String password, String passwordConfirm, boolean validDate, String dateOfBirth,
                                                             UserService userService) {
        Map<String, String> errors = new HashMap<>();
        // Checking first name
        if (!FormValuesValidator.checkBlank(firstName)) {
            errors.put("firstNameError", "First Name cannot be empty");
        } else if (!FormValuesValidator.checkNameLength(firstName)) {
            errors.put("firstNameError", "First Name cannot exceed length of 64 characters");
        } else if (!FormValuesValidator.checkUserName(firstName)) {
            errors.put("firstNameError", "First name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        }
        // Checking last name
        if (!noSurname) {
            if (!FormValuesValidator.checkBlank(lastName)) {
                errors.put("lastNameError", "Last Name cannot be empty unless box is ticked");
            } else if (!FormValuesValidator.checkNameLength(lastName)) {
                errors.put("lastNameError", "Last Name cannot exceed length of 64 characters");
            } else if (!FormValuesValidator.checkUserName(lastName)) {
                errors.put("lastNameError", "Last Name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
            }
        }
        // Checking email
        if (!FormValuesValidator.checkBlank(email)) {
            errors.put("emailError", "Email cannot be empty");
        } else if (!emailIsValid(email)) {
            errors.put("emailError", "Email address must be in the form ‘jane@doe.nz");
        } else if (!FormValuesValidator.emailInUse(email, userService)) {
            errors.put("emailError", "This email address is already in use");
        }
        // Checking password
        if (!FormValuesValidator.checkBlank(password)) {
            errors.put("passwordError", "Password cannot be empty");
        } else if (!passwordIsValid(password)) {
            errors.put("passwordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        } else if (!FormValuesValidator.checkConfirmPasswords(password, passwordConfirm)) {
            errors.put("passwordConfirmError", "Passwords do not match");
        }
        // Checking DOB
        if (validDate) {
            if (!FormValuesValidator.checkBlank(dateOfBirth)) {
                errors.put("dateOfBirthError", "Date of Birth cannot be empty");
            } else if (!dobIsValid(dateOfBirth)) {
                errors.put("dateOfBirthError", "You must be 13 years or older to create an account");
            } else if (!FormValuesValidator.checkUnder120(dateOfBirth)) {
                errors.put("dateOfBirthError", "The maximum age allowed is 120 years");
            }
        } else {
            errors.put("plantedDateError", "Date is not in valid format, DD/MM/YYYY");
        }


        return errors;
    }

    /**
     * Checks the provided information from a user on the login in page. If the email is valid and the password
     * for that account is correct, then no errors are returned, otherwise, it returns a map of errors and their
     * descriptions.
     *
     * @param email       The email string provided by the user.
     * @param password    The password string provided by the user.
     * @param userService The userService instance used to query the database for checking whether an account
     *                    exists with the given information.
     * @return A HashMap<String, String> of the errors that have occurred based on the outcome of the error checks.
     */
    public static Map<String, String> loginFormErrors(String email, String password, UserService userService) {
        HashMap<String, String> errors = new HashMap<>();

        if (!UserValidation.emailIsValid(email)) {
            errors.put("emailError",
                            "Email address must be in the form ‘jane@doe.nz'");
        }
        User user = userService.getUserByEmailAndPassword(email, password);

        if (user == null) {
            errors.put("invalidError",
                            "The email address is unknown, or the password is invalid");
        }
        return errors;
    }

    public Map<String, String> editPasswordFormErrors(String oldPassword,
                                                      String newPassword,
                                                      String retypeNewPassword,
                                                      User user
    ) {
        Map<String, String> errors = new HashMap<>();

        // Checking old password
        if (!valuesValidator.checkBlank(oldPassword)) {
            errors.put("oldPasswordError", "Password cannot be empty");
        } else if (!passwordIsValid(oldPassword)) {
            errors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        } else if (!Objects.equals(user.getPassword(), oldPassword)) {
            errors.put("oldPasswordError", "Your old password is incorrect");
        }

        // Checking new password
        if (!valuesValidator.checkBlank(newPassword)) {
            errors.put("newPasswordError", "Password cannot be empty");
        } else if (!passwordIsValid(newPassword)) {
            errors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        }

        // Checking retyped new password
        if (!valuesValidator.checkBlank(retypeNewPassword)) {
            errors.put("retypeNewPasswordError", "Password cannot be empty");
        } else if (!passwordIsValid(retypeNewPassword)) {
            errors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        }

        // Check that the new password and the retyped new password match
        if (!valuesValidator.checkConfirmPasswords(newPassword, retypeNewPassword)) {
            errors.put("passwordConfirmError", "The new passwords do not match");
        }
        return errors;
    }
}
