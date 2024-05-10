package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import nz.ac.canterbury.seng302.gardenersgrove.controller.GardenController;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * This class is used to provide static methods for validation of fields.
 */
public class FormValuesValidator {
    // Matches letters, hyphens, apostrophes and spaces, with at least one character.
    static String namePattern = "^[a-zA-Z\\-' ]+$";
    static Logger logger = LoggerFactory.getLogger(GardenController.class);

    /**
     * Checks if a given string contains inappropriate language
     *
     * @param string is the test that needs checking
     * @return true if contains profanity, false otherwise
     */
    public static boolean checkProfanity(String string) {
        try {
            string = string.replace(" ", "%20");
            URL url = new URL("https://www.purgomalum.com/service/containsprofanity?text=" + string);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return Objects.equals(String.valueOf(content), "true");
        } catch (Exception e) {
            logger.info(e.toString());
            return false;
        }
    }

    /**
     * Checks the String contains valid characters
     *
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public static boolean checkContainsText(String string) {
        return string == null || string.isEmpty() || string.chars().anyMatch(Character::isAlphabetic);
    }

    /**
     * Checks the String contains valid characters
     *
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public static boolean checkCharacters(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Checks the String contains valid characters including a forward slash.
     *
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public static boolean checkCharactersWithForwardSlash(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-'/]*");
    }

    /**
     * Checks whether the name meets the given standards
     *
     * @param string represents the string being checked
     * @return false to show there are no errors
     */
    public static boolean checkBlank(String string) {
        if (string != null) {
            return string.isBlank();
        }
        return true;
    }

    /**
     * checks that if the size is not empty it is not negative
     *
     * @param size the size being checked
     * @return false if there is no error
     */
    public static boolean checkSize(Float size) {
        return size == null || size > 0;
    }

    /**
     * Checks that if the description is not empty then it contains less than 512 characters so it can
     * fit into the database.
     *
     * @param description the description of the plant added by the user in the plant form
     * @return true if the description is shorter than 512 characters otherwise returns false
     */
    public static boolean checkDescription(String description) {
        return description == null || description.length() <= 512;
    }

    /**
     * Checks that the number of plants if entered is a positive number.
     *
     * @param count the number entered by the user in the plant form as the number of these plants in said garden
     * @return true if the number is positive or there is no entry. If the plant is negative returns false
     */
    public static boolean checkCount(Integer count) {
        return count == null || count > 0;
    }

    /**
     * Checks that the user's name is less than 64 characters.
     *
     * @param name the name entered in the user form that is to be checked
     * @return true if the name is less than or equal to 64, false if greater than
     */
    public static boolean checkNameLength(String name) {
        return name.length() <= 64;
    }

    /**
     * Checks the users' name contains only valid characters.
     *
     * @param name the name inputed by the user in the form
     * @return true if name only include letters and -, otherwise false
     */
    public static boolean checkUserName(String name) {
        return name.matches(namePattern);
    }

    /**
     * Checks that the values for password and the confirm password are the same.
     *
     * @param password  the password that has been entered by the user and is valid
     * @param confirmer should be the password re-entered by the user
     * @return true if the password and confirmer are the same, otherwise false.
     */
    public static boolean checkConfirmPasswords(String password, String confirmer) {
        return password.equals(confirmer);
    }

    /**
     * Checks that the user is under 120 years old.
     *
     * @param dob The inputted day the user was born in format YYYY/MM/DD in a string
     * @return true if the user is younger than 100, otherwise false
     */
    public static boolean checkUnder120(String dob) {
        try {
            LocalDate dobDate = LocalDate.parse(dob);
            LocalDate currentDate = LocalDate.now();
            LocalDate invalidDate = currentDate.minusYears(120);
            return !dobDate.isBefore(invalidDate);
        } catch (DateTimeParseException e) {
            return false;
        }

    }

    /**
     * Checks whether an email is already in use by a user.
     *
     * @param email       The email to be checked.
     * @param userService An instance of user service, which is used to check if there is a user with the
     *                    provided email.
     * @return bool: Whether the email is in use or not.
     */
    public static boolean emailInUse(String email, UserService userService) {
        return userService.getUserByEmail(email) == null;
    }
}
