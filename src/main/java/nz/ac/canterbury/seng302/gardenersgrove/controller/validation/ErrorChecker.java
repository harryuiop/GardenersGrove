package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation.*;



/**
 * Checks the validity of the entries into the garden form
 */
@Component
public class ErrorChecker {
    static Logger logger = LoggerFactory.getLogger(ErrorChecker.class);
    private final FormValuesValidator formValuesValidator;

    @Autowired
    public ErrorChecker(FormValuesValidator formValuesValidator) {
        this.formValuesValidator = formValuesValidator;
    }
    /**
     * Checks for valid user entries that meet the given requirements
     *
     * @param gardenName        represents the name given
     * @param gardenSize        represents the size given
     * @param gardenDescription represents the garden description
     * @param country           user entered country
     * @param city              user entered city
     * @param streetAddress     user entered street address
     * @param suburb            user entered suburb
     * @param postcode          user entered postcode
     * @return a mapping of the error labels and messages
     */
    public Map<String, String> gardenFormErrors(
                    String gardenName, Float gardenSize,
                    String gardenDescription,
                    String country,
                    String city,
                    String streetAddress,
                    String suburb,
                    String postcode
    ) {
        Map<String, String> errors = new HashMap<>();

        if (formValuesValidator.checkBlank(gardenName)) {
            errors.put("gardenNameError", "Garden name cannot by empty");
        } else if (!formValuesValidator.checkCharacters(gardenName)) {
            errors.put(
                            "gardenNameError",
                            "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        }

        if (!formValuesValidator.checkDescription(gardenDescription) || !formValuesValidator.checkContainsText(gardenDescription)) {
            errors.put("gardenDescriptionError", "Description must be 512 characters or less and contain some text");
        } else {
            try {
                if (formValuesValidator.checkProfanity(gardenDescription)) {
                    errors.put("gardenDescriptionError", "The description does not match the language standards of the app");
                }
            } catch (Exception e) {
                errors.put("profanityCheckError", "Garden cannot be made public");
            }
        }

        if (!formValuesValidator.checkSize(gardenSize)) {
            errors.put("gardenSizeError", "Garden size must be a positive number");
        }

        if (formValuesValidator.checkBlank(country)) {
            errors.put("countryError", "Country is required");
        } else if (!formValuesValidator.checkCharactersWithForwardSlash(country)) {
            errors.put(
                            "countryError",
                            "Country must only include letters, numbers, spaces, " +
                                            "commas, dots, hyphens, forward slashes or apostrophes");
        }
        validateLocationField(city, "cityError", "City", true, errors);
        validateLocationField(streetAddress, "streetAddressError", "Street Address", false, errors);
        validateLocationField(suburb, "suburbError", "Suburb", false, errors);
        validateLocationField(postcode, "postcodeError", "Postcode", false, errors);

        return errors;
    }

    /**
     * Update error Map for the location-based form fields.
     *
     * @param fieldValue    Text input by the user.
     * @param errorName     Name of the error field to add to the map (to be used to update frontend html)
     * @param fieldName     Field name used for frontend
     * @param fieldRequired If the field is required or not
     * @param errors        Error hashmap to update
     */
    private void validateLocationField(
                    String fieldValue,
                    String errorName,
                    String fieldName,
                    boolean fieldRequired,
                    Map<String, String> errors
    ) {
        if (fieldRequired && formValuesValidator.checkBlank(fieldValue)) {
            errors.put(errorName, String.format("%s is required", fieldName));
        } else if (!formValuesValidator.checkCharacters(fieldValue)) {
            errors.put(
                            errorName,
                            String.format("%s must only include letters, numbers, spaces, " +
                                            "commas, dots, hyphens or apostrophes", fieldName)
            );
        }
    }

    /**
     * Checks the provided information for adding plants to a garden.
     *
     * @param plantName        The string name of the plant.
     * @param plantCount       The integer number of plants to be added.
     * @param plantDescription The string description of the plant.
     * @return A HashMap<String, String> of the errors that have occurred based on the outcome of the error checks.
     */
    public Map<String, String> plantFormErrors(
                    String plantName,
                    String plantCount,
                    String plantDescription,
                    MultipartFile imageFile
    ) {
        HashMap<String, String> errors = new HashMap<>();

        if (formValuesValidator.checkBlank(plantName)
                || !formValuesValidator.checkCharacters(plantName)
                || !formValuesValidator.checkPlantNameLength(plantName)) {
            errors.put(
                            "plantNameError",
                            "A plant name must\n-Cannot be empty\n" +
                                    "-Only includes letters, numbers, spaces, dots, hyphens, or apostrophes\n" +
                                    "-Name cannot exceed " + Plant.NAME_CHARACTER_LIMIT + " characters"

            );
        }

        if (!formValuesValidator.checkValidPlantCount(plantCount)) {
            errors.put("plantCountError", "Plant count must be positive number, and only contain the digits 0-9");
        }

        if (!formValuesValidator.checkDescription(plantDescription)) {
            errors.put("plantDescriptionError", "Plant description must be less than 512 characters");
        }

        ImageValidator imageValidator = new ImageValidator(imageFile);
        if (imageFile != null && !imageFile.isEmpty() && !imageValidator.isValid()) {
            errors.putAll(imageValidator.getErrorMessages());
        }

        return errors;
    }

    /**
     * Takes the user's first name and checks that there is a first name, it is 64 characters or fewer, and
     * it does not\contain characters other than letters, spaces, hyphens or apostrophes. Returns these error messages
     * in a form to place into the model.
     * @param firstName The first name string.
     * @return A Map<String, String> of errors containing  <error model name, error message>.
     */
    public Map<String, String> firstNameErrors(String firstName) {
        Map<String, String> errors = new HashMap<>();
        if (formValuesValidator.checkBlank(firstName)) {
            errors.put("firstNameError", "First name cannot be empty");
        } else {
            if (!formValuesValidator.checkNameLength(firstName) && !formValuesValidator.checkUserName(firstName)) {
                errors.put(
                        "firstNameError",
                        "First name cannot exceed length of 64 characters and " +
                        "first name cannot be empty and must only include letters, spaces, hyphens or apostrophes"
                );
            } else if (!formValuesValidator.checkNameLength(firstName)) {
                errors.put("firstNameError", "First name cannot exceed length of 64 characters");
            } else if (!formValuesValidator.checkUserName(firstName)) {
                errors.put(
                        "firstNameError",
                        "First name cannot be empty and must only include letters, spaces, hyphens or apostrophes"
                        );
            }
        }
        return errors;
    }

    /**
     * Checks if the given last name, if given, is not unintentionally blank, contains 64 characters or fewer, and it
     * only includes letters, spaces, hyphens, or apostrophes. Returns these error messages in a form to place into
     * the model.
     * @param lastName last name string.
     * @param noSurname a boolean value of whether the lastName should be null or not.
     * @return A Map<String, String> of errors containing  <error model name, error message>.
     */
    public Map<String, String> lastNameErrors(String lastName, boolean noSurname) {
        Map<String, String> errors = new HashMap<>();
        if (!noSurname) {
            if (formValuesValidator.checkBlank(lastName)) {
                errors.put("lastNameError", "Last name cannot be empty unless box labelled No Lastname is ticked");
            } else {
                if (!formValuesValidator.checkNameLength(lastName) && !formValuesValidator.checkUserName(lastName)) {
                    errors.put(
                            "lastNameError",
                            "Last name cannot exceed length of 64 characters and "+
                                    "last name cannot be empty and must only include letters, spaces, hyphens or apostrophes"
                    );
                } else if (!formValuesValidator.checkNameLength(lastName)) {
                    errors.put("lastNameError", "Last name cannot exceed length of 64 characters");
                } else if (!formValuesValidator.checkUserName(lastName)) {
                    errors.put(
                            "lastNameError",
                            "Last name cannot be empty and must only include letters, spaces, hyphens or apostrophes"
                    );
                }
            }
        }
        return errors;
    }

    /**
     * Given the users email checks whether the email is present, in valid form and (if the given email is a new email)
     * not already in use. Returns these error messages in a form to place into the model.
     * @param email         The email string.
     * @param oldEmail      A boolean value of whether the email is the old email or a new one.
     * @param userService   The current user service to check whether the new email is in use.
     * @return A Map<String, String> of errors containing  <error model name, error message>.
     */
    public Map<String, String> emailErrors(String email, boolean oldEmail, UserService userService) {
        Map<String, String> errors = new HashMap<>();
        if (formValuesValidator.checkBlank(email)) {
            errors.put("emailError", "Email cannot be empty");
        } else if (!emailIsValid(email)) {
            errors.put("emailError", "Email address must be in the form ‘jane@doe.nz");
        } else if (!formValuesValidator.emailInUse(email, userService) && !oldEmail) {
            errors.put("emailError", "This email address is already in use");
        }
        return errors;
    }

    /**
     * Check email errors for the reset password form.
     * @param email         The email string.
     * @return A Map<String, String> of errors containing  <error model name, error message>.
     */
    public Map<String, String> emailErrorsResetPassword(String email) {
        Map<String, String> errors = new HashMap<>();
        if (formValuesValidator.checkBlank(email) || !emailIsValid(email)) errors.put("emailError", "Email address must be in the form ‘jane@doe.nz");
        return errors;
    }

    /**
     * Given the user's birthdate checks whether the data is present, in valid format, and the user is older than 13 but
     * younger than 120.  Returns these error messages in a form to place into the model.
     * @param dateOfBirth A string the birthdate
     * @param validDate   A boolean value of whether the date is a valid date or not.
     * @return A Map<String, String> of errors containing  <error model name, error message>.
     */
    public Map<String, String> dateOfBirthErrors(String dateOfBirth, boolean validDate) {
        Map<String, String> errors = new HashMap<>();
        if (validDate) {
            if (!formValuesValidator.checkBlank(dateOfBirth)) {
                if (!dobIsValid(dateOfBirth)) {
                    errors.put("dateOfBirthError", "You must be 13 years or older to create an account");
                } else if (!formValuesValidator.checkUnder120(dateOfBirth)) {
                    errors.put("dateOfBirthError", "The maximum age allowed is 120 years");
                }
            }
        } else {
            errors.put("dateOfBirthError", "Date is not in valid format, DD/MM/YYYY");
        }
        return errors;
    }

    /**
     * Checks that the given password was input twice, that it contains at least 8 characters, at least one uppercase
     * letter, at least one lower letter, one number, and a special character.
     * @param password          A string of the password.
     * @param passwordConfirm   A string of the attempted password copy .
     * @return A Map<String, String> of errors containing  <error model name, error message>.
     */
    public Map<String, String> passwordErrors(String password, String passwordConfirm) {
        Map<String, String> errors = new HashMap<>();
        if (formValuesValidator.checkBlank(password)) {
            errors.put("passwordError", "Password cannot be empty");
        } else if (!passwordIsValid(password)) {
            errors.put(
                    "passwordError",
                    "Your password must be at least 8 characters long and include at least one uppercase letter, " +
                            "one lowercase letter, one number, and one special character");
        }else if (!formValuesValidator.checkConfirmPasswords(password, passwordConfirm)) {
            errors.put("passwordConfirmError", "Passwords do not match");
        }
        return errors;
    }

    /**
     * Receives the inputs for the profile (from edit directly but is also used by register below),
     * then creates a map of the fields that have errors and the descriptions of those errors, used to inform the user
     * @param firstName       The first name string.
     * @param lastName        The last name string.
     * @param noSurname       Whether the noLastName box was ticked.
     * @param email           The email string.
     * @param validDate       Whether the date is valid or not.
     * @param dateOfBirth     The date of birth string.
     * @param userService     The userService instance used to query the database for checking whether an account
     *                        exists with the given information.
     * @return A HashMap<String, String> of the errors that have occurred based on the outcome of the error checks.
     */
    public Map<String, String> profileFormErrors(String firstName, String lastName, Boolean noSurname,
                                                        String email, boolean oldEmail, UserService userService,
                                                        boolean validDate, String dateOfBirth ) {
        Map<String, String> errors = firstNameErrors(firstName);
        errors.putAll(lastNameErrors(lastName, noSurname));
        errors.putAll(emailErrors(email, oldEmail, userService));
        errors.putAll(dateOfBirthErrors(dateOfBirth, validDate));
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
    public Map<String, String> registerUserFormErrors(String firstName, String lastName, Boolean noSurname,
                                                             String email, boolean oldEmail, UserService userService,
                                                             String password, String passwordConfirm,
                                                             boolean validDate, String dateOfBirth) {
        Map<String, String> errors = profileFormErrors(
                                                            firstName, lastName, noSurname, email, oldEmail, userService,
                                                            validDate, dateOfBirth
                                                            );
        errors.putAll(passwordErrors(password, passwordConfirm));
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
    public Map<String, String> loginFormErrors(String email, String password, UserService userService) {
        HashMap<String, String> errors = new HashMap<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

        if (!UserValidation.emailIsValid(email)) {
            errors.put("emailError",
                            "Email address must be in the form ‘jane@doe.nz'");
        }
        User user = userService.getUserByEmail(email);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            errors.put("invalidError",
                            "The email address is unknown, or the password is invalid");
        }
        return errors;
    }

    /**
     * Creates a hash map of all the errors that it encounters from FormValuesValidator checks
     *
     * @param oldPassword Users current password
     * @param newPassword Users new password
     * @param retypeNewPassword Users new password retyped
     * @param user The passed in user
     * @param isOldPasswordNeeded If the old password is needed.
     *                            Editing password needs old password, resetting does not.
     *
     * @return The hash map of errors
     */
    public Map<String, String> editPasswordFormErrors(String oldPassword,
                                                      String newPassword,
                                                      String retypeNewPassword,
                                                      User user,
                                                      boolean isOldPasswordNeeded
    ) {
        Map<String, String> errors = new HashMap<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

        // Checking old password
        if (isOldPasswordNeeded) {
            if (formValuesValidator.checkBlank(oldPassword)) {
                errors.put("oldPasswordError", "Password cannot be empty");
            } else if (!encoder.matches(oldPassword, user.getPassword())) {
                errors.put("oldPasswordError", "Your old password is incorrect");
            }
        }

        if (!passwordIsValid(newPassword)) {
            errors.put("newPasswordError", "Your password must be at least 8 characters long and include at least " +
                    "one uppercase letter, one lowercase letter, one number, and one special character");
        }

        // Check that the new password and the retyped new password match
        if (!formValuesValidator.checkConfirmPasswords(newPassword, retypeNewPassword)) {
            errors.put("passwordConfirmError", "The new passwords do not match");
        }

        // Checking new password
        if (formValuesValidator.checkBlank(newPassword)) {
            errors.put("newPasswordError", "Password cannot be empty");
        } else if (newPassword.contains(user.getFirstName()) || newPassword.contains(user.getLastName()) || newPassword.contains(user.getEmail())) {
            errors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one" +
                    " uppercase letter, one lowercase letter, one number, and one special character nor include any current " +
                    "user profile form fields");
        } else if (!passwordIsValid(newPassword)) {
            errors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one" +
                    " uppercase letter, one lowercase letter, one number, and one special character");
        }

        // Checking retyped new password
        if (formValuesValidator.checkBlank(retypeNewPassword)) {
            errors.put("retypeNewPasswordError", "Password cannot be empty");
        } else if (retypeNewPassword.contains(user.getFirstName()) || retypeNewPassword.contains(user.getLastName()) || retypeNewPassword.contains(user.getEmail())) {
            errors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one" +
                    " uppercase letter, one lowercase letter, one number, and one special character nor include any current " +
                    "user profile form fields");
        } else if (!passwordIsValid(retypeNewPassword)) {
            errors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least " +
                    "one uppercase letter, one lowercase letter, one number, and one special character");
        }

        return errors;
    }


    /**
     * Checks provided tag name is less than or equal to 25 characters and has valid characters
     *
     * @param tag tag name user provided
     * @return String of error message
     */
    public String tagNameErrors(String tag) {
        String errors = "";

        if(!formValuesValidator.checkTagName(tag))
            errors += "The tag name must only contain alphanumeric characters, spaces, -, _, ', or \" ";

        if (!formValuesValidator.checkTagNameLength(tag))
            errors += (!errors.isEmpty() ? "\n" : "") + "A tag cannot exceed 25 characters";

        return errors;
    }
}
