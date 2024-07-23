package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.UserValidation.*;

@Import(UserValidation.class)
public class UserValidationTest {
    // Tests for email validation
    @ParameterizedTest
    @ValueSource(strings = {"abc-d@mail.com", "abc.def@mail.com", "abc@mail.com", "abc_def@mail.com",
            "abc.def@mail.cc", "abc.def@mail-archive.com", "abc.def@mail.org", "abc.def@mail.com", "john@smith.co.nz"})
    void checkEmail_allValid_returnTrue(String email) {
        Assertions.assertTrue(emailIsValid(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "##@a.com", "aa@$$.com", "aa@aa.##", "onetwogmail.com", "onetwo@gmailcom",
            "abc-@mail.com", "abc..def@mail.com", ".Abc@mail.com", "abc#def@mail.com", "abc.def@mail.c",
            "abc.def@mail#archive.com", "abc.def@mail..com", "JOHN@smith.co.nz.ab", "john@smith.co-nz"})
    void checkEmail_invalid_returnFalse(String email) {
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
