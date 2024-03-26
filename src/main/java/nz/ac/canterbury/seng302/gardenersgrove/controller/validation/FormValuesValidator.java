package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FormValuesValidator {
    static String namePattern = "^[a-zA-Z\\-' ]+$";

    /**
     * Checks the String contains valid characters
     *
     * @param string represents the string being checked for correct characters
     * @return whether it is valid or not.
     */
    public boolean checkCharacters(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Checks whether the name meets the given standards
     *
     * @param string represents the string being checked
     * @return false to show there are no errors
     */
    public boolean checkBlank(String string) {
        return !string.isBlank();
    }

    /**
     * checks that if the size is not empty it is not negative
     *
     * @param size the size being checked
     * @return false if there is no error
     */
    public boolean checkSize(Float size) {
        return size == null || size > 0;
    }

    /**
     * Checks that if the description is not empty then it contains less than 512 characters so it can
     * fit into the database.
     * @param description the description of the plant added by the user in the plant form
     * @return true if the description is shorter than 512 characters otherwise returns false
     */
    public boolean checkDescription(String description) {
        return description == null || description.length() <= 512;
    }

    /**
     * Checks that the number of plants if entered is a positive number.
     * @param count the number entered by the user in the plant form as the number of these plants in said garden
     * @return true if the number is positive or there is no entry. If the plant is negative returns false
     */
    public boolean checkCount(Integer count) {
        return count == null || count > 0;
    }

    /**
     * Checks that the user's name is less than 64 characters
     * @param name the name entered in the user form that is to be checked
     * @return true if the name is less than or equal to 64, false if greater than
     */
    public boolean checkNameLength(String name) {
        return name.length() <= 64;
    }

    /**
     * Checks the users' name contains only valid characters
     * @param name the name inputed by the user in the form
     * @return true if name only include letters and -, otherwise false
     */
    public boolean checkUserName(String name) {
        return name.matches(namePattern);
    }

    /**
     * Checks that the values for password and the confirm password are the same.
     * @param password the password that has been entered by the user and is valid
     * @param confirmer should be the password re-entered by the user
     * @return true if the password and confirmer are the same, otherwise false.
     */
    public boolean checkConfirmPasswords(String password, String confirmer) {
        return confirmer.equals(password);
    }

    /**
     * Checks that the user is under 120 years old
     * @param dob The inputted day the user was born in format YYYY/MM/DD in a string
     * @return true if the user is younger than 100, otherwise false
     */
    public boolean checkUnder120(String dob) {
        try {
            LocalDate dobDate = LocalDate.parse(dob);
            LocalDate currentDate = LocalDate.now();
            LocalDate invalidDate = currentDate.minusYears(120);
            return !dobDate.isBefore(invalidDate);
        } catch (DateTimeParseException e) {
            return false;
        }

    }

    public boolean emailInUse(String email, UserService userService) {
        List<Users> users = userService.getAllUsers();
        for (Users user: users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
