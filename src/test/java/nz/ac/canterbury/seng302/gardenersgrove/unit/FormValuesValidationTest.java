package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.text.Normalizer;
import java.time.LocalDate;

@DataJpaTest
public class FormValuesValidationTest {
    FormValuesValidator formValuesValidator;
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void setUP() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
        formValuesValidator = new FormValuesValidator();
    }
    @Test
    void checkString_validString_returnTrue() {
        String string = "qwertyuiopasdfghjklzxcvbnmABC -'";
        Assertions.assertTrue(formValuesValidator.checkCharacters(string));
    }

    @Test
    void checkString_invalidString_returnFalse() {
        String string = "!@#$%{}";
        Assertions.assertFalse(formValuesValidator.checkCharacters(string));
    }

    @Test
    void checkName_validName_returnFalse() {
        String string = "Garden 1";
        Assertions.assertFalse(formValuesValidator.checkBlank(string));
        Assertions.assertTrue(formValuesValidator.checkCharacters(string));
    }

    @Test
    void checkName_blankName_returnTrue() {
        String string = " ";
        Assertions.assertTrue(formValuesValidator.checkBlank(string));
    }

    @Test
    void checkSize_ValidSize_returnTrue() {
        Float size = null;
        Assertions.assertTrue(formValuesValidator.checkSize(size));
    }

    @Test
    void checkSize_InvalidSize_returnFalse() {
        Float size = -1f;
        Assertions.assertFalse(formValuesValidator.checkSize(size));
    }

    @Test
    void checkCount_InvalidCount_returnFalse() {
        Integer count = -1;
        Assertions.assertFalse(formValuesValidator.checkCount(count));
    }

    @Test
    void checkCount_ValidCount_returnTrue() {
        Integer count = 100;
        Assertions.assertTrue(formValuesValidator.checkCount(count));
    }

    @Test
    void checkDesc_InvalidDesc_returnFalse() {
        String desc = "QUOBSl9rXre7kohXEjS83RTWtw69gHoyCYLj9SLb2CvVgQcdp6xgm7mgTW3WhRkabgyZ60AyhLkSo8zEGlkUW13y 390JTF8nnrPBT0GOxYzW5OrkeYqQLg8vubaMfgESUNudCR1Px0GadxfmNhGl9qyEAoRKksJO3n3HJ6ihPVjtFHbOuxbJ PTjKrNBUoBtKEAXIzKsK3jgmKP4WKTnbimPbAhfs0FbJqrzTPoBH 61smRFEcRUG6NRp61hqWtlFYxaIeSoxY7 Z68qwOvZFWAdhMdm7eedmDXvJYYJSTb1QVjAImuflA Rtblxom3sthvIX9vVs70I8pPlLcxaIFcy0POUPSCkcmLP7 U99cb0zplM5UX5cSKbHdHL 4uzDvpdHtV1vioiuIhm2olRJwTXJARB011sGyr55g0E05e4v bVCSDy0MuYDGQ770yiXyXawAEFm5jv2rIdoFBJEupmTmaErM KwmDbLr5oNyV1JreejyEIBpc6s7JHmUj1234567890";
        Assertions.assertFalse(formValuesValidator.checkDescription(desc));
    }

    @Test
    void checkDesc_ValidDesc_returnTrue() {
    String desc = "wdfghjio(*&^%$#f5678";
        Assertions.assertTrue(formValuesValidator.checkDescription(desc));
    }

    @Test
    void checkValidName_returnTrue() {
        String name = "Jane";
        Assertions.assertTrue(formValuesValidator.checkUserName(name));
    }

    @Test
    void checkInvalidName_returnFalse() {
        String name = "4eva!";
        Assertions.assertFalse(formValuesValidator.checkUserName((name)));
    }

    @Test
    void checkValidNameLength_returnTrue() {
        String name = "Jane";
        Assertions.assertTrue(formValuesValidator.checkNameLength(name));
    }

    @Test
    void checkInvalidNameLength_returnFalse() {
        String name = "AaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaA";
        Assertions.assertFalse(formValuesValidator.checkNameLength(name));
    }

    @Test
    void checkSamePassword_returnTrue() {
        String password = "abE123!!";
        Assertions.assertTrue(formValuesValidator.checkConfirmPasswords(password, password));
    }

    @Test
    void checkDiffPassword_returnFalse() {
        String pass1 = "Passw0rd$";
        String pass2 = "pA$$woe4";
        Assertions.assertFalse(formValuesValidator.checkConfirmPasswords(pass1, pass2));
    }

    @Test
    void checkUnder120Valid_returnTrue() {
        int age = 20;
        String dob = LocalDate.now().minusYears(age).toString();
        Assertions.assertTrue(formValuesValidator.checkUnder120(dob));
    }

    @Test
    void checkOver120Valid_returnFalse() {
        int age = 121;
        String dob = LocalDate.now().minusYears(age).toString();
        Assertions.assertFalse(formValuesValidator.checkUnder120(dob));
    }

    @Test
    void checkEmailNotInUse_returnTrue() {
        String email = "jane@doe.nz";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        Assertions.assertTrue(formValuesValidator.emailInUse(email, userService));
    }

    @Test
    void checkEmailInUse_returnFalse() {
        String email = "jane@doe.nz";
        String firstName = "Jane";
        String lastName = "Doe";
        String password = "abc!1E";
        String dob = LocalDate.now().minusYears(20).toString();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(new User(email, firstName, lastName,password,dob));
        Assertions.assertFalse(formValuesValidator.emailInUse(email, userService));
    }
    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "9923", "999999999", "01", ""})
    void checkValidPlantCount_returnTrue(String value) {
       Assertions.assertTrue(formValuesValidator.checkValidPlantCount(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.0", "abc", "#!12", "9999999999", "0", "-1"})
    void checkValidPlantCount_returnFalse(String value) {
        Assertions.assertFalse(formValuesValidator.checkValidPlantCount(value));
    }
}
