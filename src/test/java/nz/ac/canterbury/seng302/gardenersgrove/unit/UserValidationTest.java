package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation.*;

@Import(UserValidation.class)
public class UserValidationTest {
    // Tests for email validation
    @Test
    void checkEmail_allValid_returnTrue() {
        String email = "onetwo@gmail.com";
        Assertions.assertTrue(emailIsValid(email));
    }

    @Test
    void checkEmail_emptyInput_returnFalse() {
        String email = "";
        Assertions.assertFalse(emailIsValid(email));
    }

    @Test
    void checkEmail_onlyWhiteSpace_returnFalse() {
        String email = "     ";
        Assertions.assertFalse(emailIsValid(email));
    }

    @Test
    void checkEmail_invalidChars_returnFalse() {
        String email = "one!@#$%^&*()+='';|?><,/two@gmail.com";
        Assertions.assertFalse(emailIsValid(email));
    }

    @Test
    void checkEmail_noAt_returnFalse() {
        String email = "onetwogmail.com";
        Assertions.assertFalse(emailIsValid(email));
    }

    @Test
    void checkEmail_noDotAfterAt_returnFalse() {
        String email = "onetwo@gmailcom";
        Assertions.assertFalse(emailIsValid(email));
    }

    // Tests for password validation
    @Test
    void checkPassword_allValid_returnTrue() {
        String password = "Password123!";
        Assertions.assertTrue(passwordIsValid(password));
    }

    @Test
    void checkPassword_NoCaps_returnFalse() {
        String password = "password123!";
        Assertions.assertFalse(passwordIsValid(password));
    }

    @Test
    void checkPassword_tooShort_returnFalse() {
        String password = "Pass1!";
        Assertions.assertFalse(passwordIsValid(password));
    }

    @Test
    void checkPassword_noSpecialChar_returnFalse() {
        String password = "Password123";
        Assertions.assertFalse(passwordIsValid(password));
    }

    @Test
    void checkPassword_isEmpty_returnFalse() {
        String password = "";
        Assertions.assertFalse(passwordIsValid(password));
    }

    @Test
    void checkPassword_hasSpace_returnFalse() {
        String password = "Pass 1!";
        Assertions.assertFalse(passwordIsValid(password));
    }

    // Test for first & last name validation
    @Test
    void checkName_allValid_returnTrue() {
        String fname = "John-Mark Jacob";
        String lname = "Leo Smith-jones";
        Assertions.assertTrue(nameIsValid(fname, lname));
    }

    @Test
    void checkName_fnameAllSpace_returnFalse() {
        String fname = "    ";
        String lname = "Smith";
        Assertions.assertFalse(nameIsValid(fname, lname));
    }

    @Test
    void checkName_lnameAllSpace_returnTrue() {
        String fname = "John";
        String lname = "   ";
        Assertions.assertTrue(nameIsValid(fname, lname));
    }

    @Test
    void checkName_fnameEmpty_returnFalse() {
        String fname = "";
        String lname = "Smith";
        Assertions.assertFalse(nameIsValid(fname, lname));
    }

    @Test
    void checkName_lnameEmpty_returnTrue() {
        String fname = "John";
        String lname = "";
        Assertions.assertTrue(nameIsValid(fname, lname));
    }

    @Test
    void checkName_fnameInvalidChar_returnFalse() {
        String fname = "John;*()";
        String lname = "Smith";
        Assertions.assertFalse(nameIsValid(fname, lname));
    }

    @Test
    void checkName_lnameInvalidChar_returnFalse() {
        String fname = "John";
        String lname = "Smith;'*&^%$";
        Assertions.assertFalse(nameIsValid(fname, lname));
    }

    // Tests for date of birth validation
    @Test
    void checkDob_allValid_returnTrue() {
        String dob = "2000-01-01";
        Assertions.assertTrue(dobIsValid(dob));
    }

    @Test
    void checkDob_charInvalid_returnFalse() {
        String dob = "2000 abc/01/01";
        Assertions.assertFalse(dobIsValid(dob));
    }

    @Test
    void checkDob_isEmpty_returnTrue() {
        String dob = "";
        Assertions.assertTrue(dobIsValid(dob));
    }

    @Test
    void checkDob_ageInvalid_returnFalse() {
        String dob = LocalDate.now().toString();
        Assertions.assertFalse(dobIsValid(dob));
    }
}
