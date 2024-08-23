package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ErrorCheckerTest {

    static UserService userService = mock(UserService.class);
    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    static User user;

    static FormValuesValidator mockFormValuesValidator;
    static ErrorChecker errorChecker;

    @BeforeAll
    static void setUp() throws ProfanityCheckingException, InterruptedException {

        mockFormValuesValidator = Mockito.spy(new FormValuesValidator());
        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);

        errorChecker = new ErrorChecker(mockFormValuesValidator);

        user = new User(
            "test@domain.net",
            "Test",
            "User",
            encoder.encode("Password1!"),
            "2000-01-01");
        userService.addUsers(user);
    }

    @Test
    void gardenFormErrors_ValidInputs_returnsNull() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_NegativeSize_returnsNegativeError() {
        String name = "Garden 1";
        Float size = -1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenSizeError", "Garden size must be a positive number");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankName_returnBlankError() {
        String name = "";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_noCountry_returnsBlankError() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(name, size, null,
                country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("countryError", "Country is required");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_noCity_returnsBlankError() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("cityError", "City is required");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankSize_returnsNull() {
        String name = "Garden 1";
        Float size = null;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankNameAndNoCountry_returnsBlankErrors() {
        String name = "";
        Float size = 1.5f;
        String country = "";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put("countryError", "Country is required");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidNameInputs_returnsInvalidChar() {
        String name = "This!@$%";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "gardenNameError",
                "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidCountry_returnsInvalidChar() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New %^";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "countryError",
                "Country must only include letters, numbers, spaces, commas, dots, hyphens, forward slashes or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidCity_returnsInvalidChar() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "%^";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "cityError",
                "City must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidStreetAddress_returnsInvalidChar() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam% Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "streetAddressError",
                "Street Address must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidSuburb_returnsInvalidChar() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam&";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "suburbError",
                "Suburb must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_invalidPostcode_returnsInvalidChar() {
        String name = "Garden 1";
        Float size = 1.5f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8&041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "postcodeError",
                "Postcode must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_blankNameInvalidCountry_returnsBlankAndInvalidError() {
        String name = "  ";
        Float size = 1.5f;
        String country = "New $3%^";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, null, country, city, streetAddress, suburb, postcode);
        errors.remove("profanityCheckError");
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put(
                "countryError",
                "Country must only include letters, numbers, spaces, commas, dots, hyphens, forward slashes or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_validFirstName_returnsNoErrors() {
        String firstName = "Jane-Mary's";
        Map<String, String> errors = errorChecker.firstNameErrors(firstName);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_BlankFirstName_returnsBlankError() {
        String firstName = "  ";
        Map<String, String> errors = errorChecker.firstNameErrors(firstName);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First name cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_InvalidLongFirstNameWithInvalidCharacters_returnsTooLongErrorPlusInvalidCharacters() {
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyza%$#bcdefghijklm";
        Map<String, String> errors = errorChecker.firstNameErrors(firstName);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "firstNameError",
                "A valid name must:\n- Contain only letters, numbers, spaces, -, _, ', and \"\n- Start with a letter\n- End with a letter\n- Be at least two characters long");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_InvalidLongFirstName_returnsTooLongError() {
        String firstName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklm";
        Map<String, String> errors = errorChecker.firstNameErrors(firstName);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError", "First name cannot exceed length of 64 characters");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void firstNameErrors_InvalidCharacter_returnsInvalidError() {
        String firstName = " JA$$";
        Map<String, String> errors = errorChecker.firstNameErrors(firstName);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("firstNameError",
                "First name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_validLastName_returnsNoErrors() {
        String lastName = "Doe";
        boolean noSurname = false;
        Map<String, String> errors = errorChecker.lastNameErrors(lastName, noSurname);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_validEmptyLastName_returnsNoErrors() {
        String lastName = null;
        boolean noSurname = true;
        Map<String, String> errors = errorChecker.lastNameErrors(lastName, noSurname);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_emptyLastName_returnsBlankError() {
        String lastName = null;
        boolean noSurname = false;
        Map<String, String> errors = errorChecker.lastNameErrors(lastName, noSurname);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("lastNameError", "Last name cannot be empty unless box labelled No surname is ticked");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_InvalidLongLastNameWithInvalidCharacters_returnsTooLongErrorPlusInvalidCharacters() {
        String lastName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyza%$#bcdefghijklm";
        boolean noSurname = false;
        Map<String, String> errors = errorChecker.lastNameErrors(lastName, noSurname);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put(
                "lastNameError",
                "Last name cannot exceed length of 64 characters and " +
                        "last name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_InvalidLongLastName_returnsTooLongError() {
        String lastName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklm";
        boolean noSurname = false;
        Map<String, String> errors = errorChecker.lastNameErrors(lastName, noSurname);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("lastNameError", "Last name cannot exceed length of 64 characters");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void lastNameErrors_InvalidCharacter_returnsInvalidError() {
        String lastName = "M<>()!@acojlc";
        boolean noSurname = false;
        Map<String, String> errors = errorChecker.lastNameErrors(lastName, noSurname);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("lastNameError",
                "Last name cannot be empty and must only include letters, spaces, hyphens or apostrophes");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_validEmail_returnsNoErrors() {
        String email = "jane@doe.com";
        boolean oldEmail = false;
        Map<String, String> errors = errorChecker.emailErrors(email, oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_blankEmail_returnsBlankError() {
        String email = null;
        boolean oldEmail = false;
        Map<String, String> errors = errorChecker.emailErrors(email, oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "Email cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void emailErrors_invalidEmailForm_returnsInvalidError() {
        String email = "notAnEmail.com";
        boolean oldEmail = false;
        Map<String, String> errors = errorChecker.emailErrors(email, oldEmail, userService);
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
        Mockito.when(userService.getUserByEmail(email))
                .thenReturn(new User(email, firstName, lastName, password, dateOfBirth));
        Map<String, String> errors = errorChecker.emailErrors(email, oldEmail, userService);
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
        Mockito.when(userService.getUserByEmail(email))
                .thenReturn(new User(email, firstName, lastName, password, dateOfBirth));
        Map<String, String> errors = errorChecker.emailErrors(email, oldEmail, userService);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "This email address is already in use");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_validDate_returnsNoErrors() {
        String dateOfBirth = LocalDate.now().minusYears(120).toString();
        boolean validDate = true;
        Map<String, String> errors = errorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_BlankDate_returnsNoErrors() {
        String dateOfBirth = "";
        boolean validDate = true;
        Map<String, String> errors = errorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_dobTooYoung_returnsUnder13() {
        String dateOfBirth = LocalDate.now().minusYears(10).toString();
        boolean validDate = true;
        Map<String, String> errors = errorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "You must be 13 years or older to create an account");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_dobTooOld_returnsOver120() {
        String dateOfBirth = LocalDate.now().minusYears(121).toString();
        boolean validDate = true;
        Map<String, String> errors = errorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "The maximum age allowed is 120 years");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void dateOfBirthErrors_invalidDate_returnsInvalidFormat() {
        String dateOfBirth = "30/02/2000";
        boolean validDate = false;
        Map<String, String> errors = errorChecker.dateOfBirthErrors(dateOfBirth, validDate);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "Date is not in valid format, DD/MM/YYYY");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_validPassword_returnsNoErrors() {
        String password = "Abe123#$";
        Map<String, String> errors = errorChecker.passwordErrors(password, password);
        HashMap<String, String> correctErrors = new HashMap<>();
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_blankPassword_returnsBlankError() {
        String password = null;
        Map<String, String> errors = errorChecker.passwordErrors(password, password);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordError", "Password cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void passwordErrors_doesNotMeetCharacterConditions_returnsInvalidCharacters() {
        String password = "a1b2de";
        Map<String, String> errors = errorChecker.passwordErrors(password, password);
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
        Map<String, String> errors = errorChecker.passwordErrors(password, otherpass);
        HashMap<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordConfirmError", "Passwords do not match");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void loginFormErrors_ValidInputs_ReturnsEmpty() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

        String email = "user@gmail.com";
        String password = "Password1!";

        when(userRepositoryMock.findByEmail(email)).thenReturn(
                new User(email, "fname", "lname", encoder.encode(password), "20/20/2003"));
        Map<String, String> errors = errorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        Assertions.assertEquals(expected, errors);
    }

    @Test
    void loginFormErrors_AllBlank_ReturnsBothErrors() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "";
        String password = "";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(null);
        Map<String, String> errors = errorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("emailError", "Email address must be in the form ‘jane@doe.nz'");
        expected.put("invalidError", "The email address is unknown, or the password is invalid");
        Assertions.assertEquals(expected, errors);
    }

    @Test
    void loginFormErrors_InvalidPassword_ReturnsAccountError() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "user@gmail.com";
        String password = "iAmABadPassword";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(null);
        Map<String, String> errors = errorChecker.loginFormErrors(email, password, userService);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("invalidError", "The email address is unknown, or the password is invalid");
        Assertions.assertEquals(expected, errors);
    }

    @Test
    void loginFormErrors_InvalidEmailFormat_ReturnsBothErrors() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepositoryMock);

        String email = "notAnEmail";
        String password = "Re4!Password";
        when(userRepositoryMock.findByEmailAndPassword(email, password)).thenReturn(null);
        Map<String, String> errors = errorChecker.loginFormErrors(email, password, userService);
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
        boolean oldEmail = false;
        String password = "passworD!2";
        String dateOfBirth = LocalDate.now().minusYears(20).toString();
        boolean validDate = true;
        Mockito.when(userService.getUserByEmail(email))
                .thenReturn(new User(email, firstName, lastName, password, dateOfBirth));
        Map<String, String> errors = errorChecker.registerUserFormErrors(firstName, lastName, noSurname, email,
                oldEmail, userService,
                password, password, validDate, dateOfBirth);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("emailError", "This email address is already in use");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void registrationFormErrors_dobTooYoung_returnsUnder13() {
        String firstName = "Jane";
        String lastName = "Doe";
        boolean noSurname = false;
        String email = "jane.doe@gmail.com";
        boolean oldEmail = false;
        String password = "a1B2c#de";
        String dateOfBirth = LocalDate.now().minusYears(10).toString();
        boolean validDate = true;
        Map<String, String> errors = errorChecker.registerUserFormErrors(firstName, lastName, noSurname, email,
                oldEmail, userService,
                password, password, validDate, dateOfBirth);
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
        boolean oldEmail = false;
        String password = "a1B2c#de";
        String dateOfBirth = LocalDate.now().minusYears(121).toString();
        boolean validDate = true;
        Map<String, String> errors = errorChecker.registerUserFormErrors(firstName, lastName, noSurname, email,
                oldEmail, userService,
                password, password, validDate, dateOfBirth);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("dateOfBirthError", "The maximum age allowed is 120 years");
        Assertions.assertEquals(correctErrors, errors);
    }

    /**
     * Edit password tests
     */

    @Test
    void editPasswordFormErrors_emptyOldPassword_invalid() {
        String oldPassword = "";
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword,
                user, true);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Password cannot be empty");
        Assertions.assertEquals(correctErrors, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Password!1valid", "Password1" })
    void editPasswordFormErrors_InvalidOldPassword_invalid(String oldPassword) {
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword,
                user, true);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("oldPasswordError", "Your old password is incorrect");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void editPasswordFormErrors_emptyNewPassword_invalid() {
        String oldPassword = "Password1!";
        String newPassword = "";
        String retypeNewPassword = "";
        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword,
                user, true);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError", "Password cannot be empty");
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "short",
            "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiop",
            "password1!", "PASSWORD1!", "Password1", "Password!" })
    void editPasswordFormErrors_InvalidNewPassword_invalid(String newPassword) {
        String oldPassword = "Password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword,
                user, true);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @Test
    void editPasswordFormErrors_checkNewAndRetypePasswordMatches_returnTrue() {
        String oldPassword = "Password1!";
        String newPassword = "NewPassword1!";
        String retypeNewPassword = "NewPassword1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword,
                user, true);
        Assertions.assertTrue(errors.isEmpty());
    }

    @Test
    void editPasswordFormErrors_checkNewAndRetypePasswordNotMatches_returnFalse() {
        String oldPassword = "Password1!";
        String newPassword = "NewPassword1!";
        String retypeNewPassword = "BadPassword!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword,
                user, true);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordConfirmError", "The new passwords do not match");
        Assertions.assertTrue(errors.containsKey("passwordConfirmError"));
        Assertions.assertEquals(correctErrors.get("passwordConfirmError"), errors.get("passwordConfirmError"));
    }

    /**
     * Reset Password Tests.
     */
    @Test
    void resetPasswordFormErrors_tooShortNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "short";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_tooLongNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiop";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_noUpperCaseLetterInNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "password1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_noLowerCaseLetterInNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "PASSWORD1!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_EmptyNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertTrue(errors.containsValue("Password cannot be empty"));
    }

    @Test
    void resetPasswordFormErrors_noSpecialCaseInNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "Password1";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertTrue(errors.containsValue(
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character"));

    }

    @Test
    void resetPasswordFormErrors_noDigitCaseInNewPassword_returnsInvalidNewPasswordError() {
        String newPassword = "Password!";
        String retypeNewPassword = "Password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("newPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("newPasswordError"));
        Assertions.assertEquals(correctErrors.get("newPasswordError"), errors.get("newPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_tooShortRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "short";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertTrue(errors.containsValue(
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character"));

    }

    @Test
    void resetPasswordFormErrors_tooLongRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiop";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_noUpperCaseLetterInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "password1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_EmptyRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError", "Password cannot be empty");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_noLowerCaseLetterInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "PASSWORD1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_noSpecialCaseInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "Password1";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_noDigitCaseInRetypeNewPassword_returnsInvalidRetypeNewPasswordError() {
        String newPassword = "Password1!";
        String retypeNewPassword = "Password!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("retypeNewPasswordError",
                "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("retypeNewPasswordError"));
        Assertions.assertEquals(correctErrors.get("retypeNewPasswordError"), errors.get("retypeNewPasswordError"));
    }

    @Test
    void resetPasswordFormErrors_checkNewAndRetypePasswordMatches_returnTrue() {
        String newPassword = "NewPassword1!";
        String retypeNewPassword = "NewPassword1!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Assertions.assertTrue(errors.isEmpty());
    }

    @Test
    void resetPasswordFormErrors_checkNewAndRetypePasswordMatches_returnFalse() {
        String newPassword = "NewPassword1!";
        String retypeNewPassword = "BadPassword!";
        Map<String, String> errors = errorChecker.editPasswordFormErrors("", newPassword, retypeNewPassword, user,
                false);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("passwordConfirmError", "The new passwords do not match");
        Assertions.assertFalse(errors.containsKey("oldPasswordError"));
        Assertions.assertTrue(errors.containsKey("passwordConfirmError"));
        Assertions.assertEquals(correctErrors.get("passwordConfirmError"), errors.get("passwordConfirmError"));
    }

    @Test
    void resetPasswordEmailErrors_incorrectFormat_showError() {
        String email = "jane@doe";
        Map<String, String> errors = errorChecker.emailErrorsResetPassword(email);
        Assertions.assertTrue(errors.containsKey("emailError"));
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void resetPasswordEmailErrors_emptyEmail_showError() {
        String email = "";
        Map<String, String> errors = errorChecker.emailErrorsResetPassword(email);
        Assertions.assertTrue(errors.containsKey("emailError"));
        Assertions.assertEquals(1, errors.size());
    }

    @Test
    void resetPasswordEmailErrors_validEmail_showError() {
        String email = "jane@doe.com";
        Map<String, String> errors = errorChecker.emailErrorsResetPassword(email);
        Assertions.assertEquals(0, errors.size());
    }

    @Test
    void gardenFormErrors_Inappropriate_returnsDescriptionError()
            throws ProfanityCheckingException, InterruptedException {
        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(true);
        String name = "Garden 1";
        Float size = 1.5f;
        String description = "Shithead";
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, description, country, city, streetAddress, suburb, postcode);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenDescriptionError", "The description does not match the language standards of the app");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_ProfanityCheckerError_returnsDescriptionError()
            throws ProfanityCheckingException, InterruptedException {
        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString()))
                .thenThrow(new ProfanityCheckingException("Failed to check for profanity"));
        String name = "Garden 1";
        Float size = 1.5f;
        String description = "Shithead";
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, description, country, city, streetAddress, suburb, postcode);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("profanityCheckError", "Garden cannot be made public");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_NoTextDescription_returnsDescriptionError() {
        String name = "Garden 1";
        Float size = 1.5f;
        String description = "$%^&*(";
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, description, country, city, streetAddress, suburb, postcode);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenDescriptionError", "Description must be 512 characters or less and contain some text");
        Assertions.assertEquals(correctErrors, errors);
    }

    @Test
    void gardenFormErrors_LongDescription_returnsDescriptionError() {
        String name = "Garden 1";
        Float size = 1.5f;
        String description = "vikbyyigjgnxbfpofwuziotuihtetvqubddfoicvrdxgtgflixdgoqkgwfwvlxxoqrmqbithgjxizgpyali" +
                "hbbopjkuvwedepvvdlzflotpsgdffexfrdhrenbvmyinlvmdrrlymywwhszlzijdbvbktzbpqcbusuaowzwvozbaeswdjbgmaswhwb"
                +
                "rbkqccmetbwgdnymvtcksubenexrltbpvwziscmvacmanceytclzghurliqaumlttukelwdpdlageimoviqtlezbciyksfufnxocrh"
                +
                "blmtmtijopubgkpujmagotgdfinwtpmxrjhdevqrlzpmnofkypuisfkbovqfdwmznbjeasfwfbyhhfizjxjzntihbzgrmgkudkdtjr"
                +
                "ranlhzohychbxshgniecquyviibvuqozlwhhiziskuungizxznbfezhvuilrvgafmnfhuowibbqppsjbcmklvfzneesaohqbrjkxkr"
                +
                "gggdbozsruiapzizkpfhvv";
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";
        Map<String, String> errors = errorChecker.gardenFormErrors(
                name, size, description, country, city, streetAddress, suburb, postcode);
        Map<String, String> correctErrors = new HashMap<>();
        correctErrors.put("gardenDescriptionError", "Description must be 512 characters or less and contain some text");
        Assertions.assertEquals(correctErrors, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a!b", "c@d", "e#f", "g$h", "i%j", "k^l", "m&n", "o*p", "q(r", "s)t", "u+v", "w=x", "y:z", "a", "b-", "_c"
    })
    void viewGardenFormErrors_checkNewTagNameIsInvalid_returnsErrorMessage(String tagName) {
        String error = errorChecker.tagNameErrors(tagName);
        Assertions.assertTrue(error.contains(
                "A valid tag name must:\n- Contain only letters, numbers, spaces, -, _, ', and \"\n- Start with a letter\n- End with a letter\n- Be at least two characters long"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abcdefghijklmnopqrtsuvwxy", "johns-tag", "johns_tag", "johns tag", "john's tag", "john\"s-tag", "it"
    })
    void viewGardenFormErrors_checkNewTagNameIsValid_returnsNoErrorMessage(String tagName) {
        String error = errorChecker.tagNameErrors(tagName);
        Assertions.assertEquals("", error);
    }

    @ParameterizedTest
    @ValueSource(strings = { "abcedfghijklmnopqrstuvwxyz", "abc------'''''''''''kjdshfjdskahflk765417653321764a" })
    void viewGardenFromErrors_checkNewTagNameExceed25Characters_returnsErrorMessage(String tagName) {
        String error = errorChecker.tagNameErrors(tagName);
        Assertions.assertEquals("A tag cannot exceed 25 characters", error);
    }
}
