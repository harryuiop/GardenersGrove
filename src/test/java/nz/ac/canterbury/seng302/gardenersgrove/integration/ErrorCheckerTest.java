package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@DataJpaTest
@Import(ErrorChecker.class)

class ErrorCheckerTest {
    ErrorChecker validate = new ErrorChecker();

    @Test
    public void gardenFormErrors_ValidInputs_returnsNull() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        Assertions.assertEquals(correctErrors, errors);
    }
    @Test
    public void gardenFormErrors_NegativeSize_returnsNegativeError() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = -1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenSizeError", "Garden size must be a positive number");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void gardenFormErrors_blankName_returnBlankError() {
        String name = "";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        Assertions.assertEquals(errors, correctErrors);
    }
    @Test
    public void gardenFormErrors_blankLocation_returnsBlankError() {
        String name = "Garden 1";
        String location = "";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void gardenFormErrors_blankSize_returnsNull() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = null;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<String, String>();
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void gardenFormErrors_blankNameAndLocation_returnsBlankErrors() {
        String name = "";
        String location = " ";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void gardenFormErrors_invalidNameInputs_returnsInvalidChar() {
        String name = "This!@$%";
        String location = "Christchurch";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        Map<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put(
                "gardenNameError",
                "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void gardenFormErrors_invalidLocationInputs_returnsInvalidChar() {
        String name = "Garden 1";
        String location = "#1";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void gardenFormErrors_blankNameInvalidLocation_returnsBlankAndInvalidError() {
        String name = "  ";
        String location = "#1";
        Float size = 1.5f;
        Map<String, String> errors = validate.gardenFormErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void loginFormErrors_ValidInputs_ReturnsEmpty() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "user@gmail.com";
        String password = "Password1!";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(
                new Users(email, "fname", "lname","address", password, "20/20/2003"));
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

}
