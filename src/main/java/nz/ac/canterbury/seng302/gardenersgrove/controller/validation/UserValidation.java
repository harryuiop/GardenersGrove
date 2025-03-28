package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UserValidation {
    static String emailPattern = "[\\p{L}0-9]+([.\\-_][\\p{L}0-9]+)*@[\\p{L}0-9-]+(\\.[\\p{L}]{2,}){1,2}";
    static String passwordPattern = "^(?=.*?[\\p{Lu}])(?=.*?[\\p{Ll}])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    static String namePattern = "^[\\p{L}\\-' ]+$";

    /**
     * Check if the email is valid
     *
     * @param email of the user
     * @return true or false depending on whether the email given matches the pattern
     */
    public static boolean emailIsValid(String email) {
        return email.matches(emailPattern);
    }

    /**
     * Check if the password is valid
     *
     * @param password of the user
     * @return true or false depending on whether the password given matches the pattern
     */
    public static boolean passwordIsValid(String password) {
        return password.matches(passwordPattern);
    }

    /**
     * Check if both the first and last names are valid
     *
     * @param fname of the user
     * @param lname of the user
     * @return true or false depending on whether both names given match the pattern
     * (last name is allowed to be blank)
     */
    public static boolean nameIsValid(String fname, String lname) {
        return fname.matches(namePattern) && !fname.isBlank() && (lname == null || lname.isBlank() || lname.matches(namePattern));
    }

    /**
     * Check if the date of birth entered is valid
     *
     * @param dob of the user
     * @return true or false depending on whether the date given is in the correct format
     *         and if it is 13 years before the system date
     */
    public static boolean dobIsValid(String dob) {
        try {
            if (dob.equals("")) {
                return true;
            }
            LocalDate dobDate = LocalDate.parse(dob);
            LocalDate currentDate = LocalDate.now();
            LocalDate validDate = currentDate.minusYears(13);
            return dobDate.isBefore(validDate);
        } catch (DateTimeParseException e) {
            return false;
        }

    }
}
