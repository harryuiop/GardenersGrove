package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(ErrorChecker.class)

class ErrorCheckerTest {

    UserService userService = mock(UserService.class);

    @Test
    void gardenFormErrors_ValidInputs_returnsNull() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void gardenFormErrors_NegativeSize_returnsNegativeError() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = -1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenSizeError", "Garden size must be a positive number");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankName_returnBlankError() {
        String name = "";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void gardenFormErrors_blankLocation_returnsBlankError() {
        String name = "Garden 1";
        String location = "";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankSize_returnsNull() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = null;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankNameAndLocation_returnsBlankErrors() {
        String name = "";
        String location = " ";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidNameInputs_returnsInvalidChar() {
        String name = "This!@$%";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "gardenNameError",
                "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidLocationInputs_returnsInvalidChar() {
        String name = "Garden 1";
        String location = "#1";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankNameInvalidLocation_returnsBlankAndInvalidError() {
        String name = "  ";
        String location = "#1";
        Float size = 1.5f;
        Map<String, String> errors = ErrorChecker.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_validFirstName_returnsNoErrors() {
        String firstName = "Jane-Mary's ";
        Map<String, String> errors = ErrorChecker.firstNameErrors(firstName);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_BlankFirstName_returnsBlankError() {
        String firstName = "  ";
        Map<String, String> errors = ErrorChecker.firstNameErrors(firstName);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First name cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_InvalidLongFirstNameWithInvalidCharacters_returnsTooLongErrorPlusInvalidCharacters() {
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyza%$#bcdefghijklm";
        Map<String, String> errors = ErrorChecker.firstNameErrors(firstName);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "firstNameError",
                "First name cannot exceed length of 64 characters and " +
                        "first name cannot be empty and must only include letters, spaces, hyphens or apostrophes"
        );
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_InvalidLongFirstName_returnsTooLongError() {
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklm";
        Map<String, String> errors = ErrorChecker.firstNameErrors(firstName);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First name cannot exceed length of 64 characters");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_InvalidCharacter_returnsInvalidError() {
        String firstName = " JA$$";
        Map<String, String> errors = ErrorChecker.firstNameErrors(firstName);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_validLastName_returnsNoErrors() {
        String lastName = "Doe";
        boolean noSurname = false;
        Map<String, String> errors = ErrorChecker.lastNameErrors(lastName, noSurname);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_validEmptyLastName_returnsNoErrors() {
        String lastName = null;
        boolean noSurname = true;
        Map<String, String> errors = ErrorChecker.lastNameErrors(lastName, noSurname);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_emptyLastName_returnsBlankError() {
        String lastName = null;
        boolean noSurname = false;
        Map<String, String> errors = ErrorChecker.lastNameErrors(lastName, noSurname);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("lastNameError", "Last name cannot be empty unless box is ticked");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_InvalidLongLastNameWithInvalidCharacters_returnsTooLongErrorPlusInvalidCharacters() {
        String lastName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyza%$#bcdefghijklm";
        boolean noSurname = false;
        Map<String, String> errors = ErrorChecker.lastNameErrors(lastName, noSurname);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "lastNameError",
                "Last name cannot exceed length of 64 characters and " +
                        "last name cannot be empty and must only include letters, spaces, hyphens or apostrophes"
        );
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_InvalidLongLastName_returnsTooLongError() {
        String lastName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklm";
        boolean noSurname = false;
        Map<String, String> errors = ErrorChecker.lastNameErrors(lastName, noSurname);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("lastNameError", "Last name cannot exceed length of 64 characters");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_InvalidCharacter_returnsInvalidError() {
        String lastName = "M<>()!@acojlc";
        boolean noSurname = false;
        Map<String, String> errors = ErrorChecker.lastNameErrors(lastName, noSurname);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("lastNameError", "Last name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_validEmail_returnsNoErrors() {
        String email = "jane@doe.com";
        boolean oldEmail = false;
        Map<String, String> errors = ErrorChecker.emailErrors(email,oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_blankEmail_returnsBlankError() {
        String email = null;
        boolean oldEmail = false;
        Map<String, String> errors = ErrorChecker.emailErrors(email,oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "Email cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_invalidEmailForm_returnsInvalidError() {
        String email = "notAnEmail.com";
        boolean oldEmail = false;
        Map<String, String> errors = ErrorChecker.emailErrors(email,oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "Email address must be in the form ‘jane@doe.nz");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_oldEmail_returnsNoErrors() {
        String firstName = "Jane";
        String lastName = "";
        String email = "jane@doe.nz";
        String password = "passworD!2";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean oldEmail = true;
        Mockito.when(userService.getUserByEmail(email)).thenReturn(new User(email,firstName,lastName,password, dateOfBirth));
        Map<String, String> errors = ErrorChecker.emailErrors(email,oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_InvalidDupEmail_returnsAlreadyInUse() {
        String firstName = "Jane";
        String lastName = "";
        String email = "jane@doe.nz";
        String password = "passworD!2";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean oldEmail = false;
        Mockito.when(userService.getUserByEmail(email)).thenReturn(new User(email,firstName,lastName,password, dateOfBirth));
        Map<String, String> errors = ErrorChecker.emailErrors(email,oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "This email address is already in use");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_validDate_returnsNoErrors() {
        String dateOfBirth = LocalDate.now().minusYears(120).toString();
        boolean validDate = true;
        Map<String, String> errors = ErrorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_BlankDate_returnsNoErrors() {
        String dateOfBirth = "";
        boolean validDate = true;
        Map<String, String> errors = ErrorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_dobTooYoung_returnsUnder13() {
        String dateOfBirth = LocalDate.now().minusYears(10).toString();
        boolean validDate = true;
        Map<String, String> errors = ErrorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "You must be 13 years or older to create an account");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_dobTooOld_returnsOver120() {
        String dateOfBirth = LocalDate.now().minusYears(121).toString();
        boolean validDate = true;
        Map<String, String> errors = ErrorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "The maximum age allowed is 120 years");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_invalidDate_returnsInvalidFormat() {
        String dateOfBirth = "30/02/2000";
        boolean validDate = false;
        Map<String, String> errors = ErrorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "Date is not in valid format, DD/MM/YYYY");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_validPassword_returnsNoErrors() {
        String password = "Abe123#$";
        Map<String, String> errors = ErrorChecker.passwordErrors(password, password);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_blankPassword_returnsBlankError() {
        String password = null;
        Map<String, String> errors = ErrorChecker.passwordErrors(password, password);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordError", "Password cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_doesNotMeetCharacterConditions_returnsInvaildCharacters() {
        String password = "a1b2de";
        Map<String, String> errors = ErrorChecker.passwordErrors(password, password);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "passwordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, " +
                        "one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_notEqualPasswords_returnsNotEqual() {
        String password = "a1B2c#de";
        String otherpass = "abcD32#";
        Map<String, String> errors = ErrorChecker.passwordErrors(password, otherpass);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordConfirmError", "Passwords do not match");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    public void loginFormErrors_ValidInputs_ReturnsEmpty() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "user@gmail.com";
        String password = "Password1!";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(
                new User(email, "fname", "lname", password, "20/20/2003"));
        Map<String, String> errors = ErrorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        Assertions.assertEquals(expected, errors);
    }

    @Test
    public void loginFormErrors_AllBlank_ReturnsBothErrors() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "";
        String password = "";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(null);
        Map<String, String> errors = ErrorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("emailError", "Email address must be in the form ‘jane@doe.nz'");
        expected.put("invalidError", "The email address is unknown, or the password is invalid");
        Assertions.assertEquals(expected, errors);
    }

    @Test
    public void loginFormErrors_InvalidPassword_ReturnsAccountError() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "user@gmail.com";
        String password = "iAmABadPassword";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(null);
        Map<String, String> errors = ErrorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("invalidError", "The email address is unknown, or the password is invalid");
        Assertions.assertEquals(expected, errors);
    }

    @Test
    public void loginFormErrors_InvalidEmailFormat_ReturnsBothErrors() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "notAnEmail";
        String password = "Re4!Password";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(null);
        Map<String, String> errors = ErrorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("emailError", "Email address must be in the form ‘jane@doe.nz'");
        expected.put("invalidError", "The email address is unknown, or the password is invalid");
        Assertions.assertEquals(expected, errors);
    }
}
