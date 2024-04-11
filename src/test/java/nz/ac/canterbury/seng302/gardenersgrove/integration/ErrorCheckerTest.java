package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

@DataJpaTest
@Import(ErrorChecker.class)
@WithMockUser(value = "1")

class ErrorCheckerTest {

    ErrorChecker validate = new ErrorChecker();
    UserService userService = mock(UserService.class);
    private boolean userCreated = false;
    User user;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        if (!userCreated) {
            user = new User(
                    "test@domain.net",
                    "Test",
                    "User",
                    "Password1!",
                    "2000-01-01"
            );
            userRepository.save(user);
            userCreated = true;
        }
    }

    @Test
    void gardenFormErrors_ValidInputs_returnsNull() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void gardenFormErrors_NegativeSize_returnsNegativeError() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = -1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenSizeError", "Garden size must be a positive number");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankName_returnBlankError() {
        String name = "";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void gardenFormErrors_blankLocation_returnsBlankError() {
        String name = "Garden 1";
        String location = "";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankSize_returnsNull() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = null;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankNameAndLocation_returnsBlankErrors() {
        String name = "";
        String location = " ";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
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
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
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
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
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
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void registrationFormErrors_allFieldsValid_returnsNoErrors() {
        String firstName = "Jane";
        String lastName = "Doe";
        boolean noSurname = false;
        String email = "jane.doe@gmail.com";
        String password = "a1B2c#de";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean validDate = true;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, password, validDate, dateOfBirth,
                userService);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void registrationFormErrors_allFieldsBlank_returnsBlankErrors() {
        String firstName = "  ";
        String lastName = "";
        boolean noSurname = false;
        String email = "  ";
        String password = "";
        String dateOfBirth = "  ";
        boolean validDate = true;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                                                                    password, password, validDate, dateOfBirth,
                                                                    userService);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First Name cannot be empty");
        correctErrors.put("lastNameError", "Last Name cannot be empty unless box is ticked");
        correctErrors.put("emailError", "Email cannot be empty");
        correctErrors.put("passwordError", "Password cannot be empty");
        correctErrors.put("dateOfBirthError", "Date of Birth cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void registrationFormErrors_allFieldsInvalidChar_returnsInvalidErrors() {
        String firstName = " JA$$";
        String lastName = "";
        boolean noSurname = true;
        String email = "  @doe.nz";
        String password = "password";
        String dateOfBirth = "  ";
        boolean validDate = false;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, password, validDate, dateOfBirth,
                userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        correctErrors.put("emailError", "Email address must be in the form ‘jane@doe.nz");
        correctErrors.put("passwordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        correctErrors.put("plantedDateError", "Date is not in valid format, DD/MM/YYYY");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void registrationFormErrors_InvalidlongFName_returnsTooLongError() {
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklm";
        String lastName = "";
        boolean noSurname = true;
        String email = "jane@doe.nz";
        String password = "passworD!2";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean validDate = true;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, password, validDate, dateOfBirth,
                userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First Name cannot exceed length of 64 characters");
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
        Map<String, String> errors = validate.loginFormErrors(email, password, userService);
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
        Map<String, String> errors = validate.loginFormErrors(email, password, userService);
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
        Map<String, String> errors = validate.loginFormErrors(email, password, userService);
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
        Map<String, String> errors = validate.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("emailError", "Email address must be in the form ‘jane@doe.nz'");
        expected.put("invalidError", "The email address is unknown, or the password is invalid");
        Assertions.assertEquals(expected, errors);
    }
    @Test
    void registrationFormErrors_InvalidDupEmail_returnsAlreadyInUse() {
        String firstName = "Jane";
        String lastName = "";
        boolean noSurname = true;
        String email = "jane@doe.nz";
        String password = "passworD!2";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean validDate = true;
        Mockito.when(userService.getUserByEmail(email)).thenReturn(new User(email,firstName,lastName,password, dateOfBirth));
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, password, validDate, dateOfBirth,
                userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "This email address is already in use");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void registrationFormErrors_notEqualPasswords_returnsNotEqual() {
        String firstName = "Jane";
        String lastName = "Doe";
        boolean noSurname = false;
        String email = "jane.doe@gmail.com";
        String password = "a1B2c#de";
        String otherpass = "abcD32#";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean validDate = true;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, otherpass, validDate, dateOfBirth,
                userService);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordConfirmError", "Passwords do not match");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void registrationFormErrors_dobTooYoung_returnsUnder13() {
        String firstName = "Jane";
        String lastName = "Doe";
        boolean noSurname = false;
        String email = "jane.doe@gmail.com";
        String password = "a1B2c#de";
        String dateOfBirth = LocalDate.now().minusYears(10).toString();
        boolean validDate = true;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, password, validDate, dateOfBirth,
                userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "You must be 13 years or older to create an account");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void registrationFormErrors_dobTooOld_returnsUnder13() {
        String firstName = "Jane";
        String lastName = "Doe";
        boolean noSurname = false;
        String email = "jane.doe@gmail.com";
        String password = "a1B2c#de";
        String dateOfBirth = LocalDate.now().minusYears(121).toString();
        boolean validDate = true;
        Map<String, String> errors = validate.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, password, validDate, dateOfBirth,
                userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "The maximum age allowed is 120 years");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_tooShortOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "short";
        String newPassword = "Password1!new";
        String retypeNewPassword = "Password1!new";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_tooLongOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiop";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_noUpperCaseLetterInOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_noLowerCaseLetterInOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "PASSWORD1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_EmptyOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Password cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_noSpecialCaseInOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "Password1";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_noDigitCaseInOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "Password!";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    void editPasswordFormErrors_incorrectOldPassword_returnsOldInvalidPasswordError() {
        String oldPassword = "Password!1valid";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your old password is incorrect");
        Assertions.assertEquals(correctErrors, errors);
    }
     @Test
     void editPasswordFormErrors_tooShortNewPassword_returnsInvalidNewPasswordError() {
         String oldPassword = "Password1!";
         String newPassword = "short";
         String retypeNewPassword = "Password1!";
         Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
         Map<String, String> correctErrors = new HashMap<>();
         correctErrors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
         Assertions.assertTrue(errors.containsKey("newPasswordError"));
         Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
     }
    @Test
    void editPasswordFormErrors_tooLongNewPassword_returnsInvalidNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiop";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noUpperCaseLetterInNewPassword_returnsInvalidNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noLowerCaseLetterInNewPassword_returnsInvalidNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "PASSWORD1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }
    @Test
    void editPasswordFormErrors_EmptyNewPassword_returnsInvalidNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Password cannot be empty");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noSpecialCaseInNewPassword_returnsInvalidNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noDigitCaseInNewPassword_returnsInvalidNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }
    @Test
    void editPasswordFormErrors_tooShortRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "short";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_tooLongRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiop";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noUpperCaseLetterInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "password1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noLowerCaseLetterInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "PASSWORD1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_EmptyRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Password cannot be empty");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noSpecialCaseInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_noDigitCaseInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String oldPassword = "Password1!";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }
    @Test
    void editPasswordFormErrors_checkNewAndRetypePasswordMatches_returnTrue() {
        String oldPassword = "Password1!";
        String newPassword = "NewPassword1!";
        String retypeNewPassword = "NewPassword1!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertTrue(errors.isEmpty());
    }
    @Test
    void editPasswordFormErrors_checkNewAndRetypePasswordMatches_returnFalse() {
        String oldPassword = "Password1!";
        String newPassword = "NewPassword1!";
        String retypeNewPassword = "BadPassword!";
        Map<String, String> errors = validate.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordConfirmError", "The new passwords do not match");
        Assertions.assertTrue(errors.containsKey("passwordConfirmError"));
        Assertions.assertEquals(correctErrors.get("passwordConfirmError"), errors.get("passwordConfirmError"));
    }
}
