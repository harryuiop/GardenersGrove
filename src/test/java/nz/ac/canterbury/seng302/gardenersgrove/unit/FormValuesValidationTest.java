package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

@DataJpaTest
@Import(FormValuesValidator.class)

public class FormValuesValidationTest {
    FormValuesValidator valuesValidator = new FormValuesValidator();
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void setUP() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }
    @Test
    void checkString_validString_returnTrue() {
        String string = "qwertyuiopasdfghjklzxcvbnmABC -'";
        Assertions.assertTrue(valuesValidator.checkCharacters(string));
    }

    @Test
    void checkString_invalidString_returnFalse() {
        String string = "!@#$%{}";
        Assertions.assertFalse(valuesValidator.checkCharacters(string));
    }

    @Test
    void checkName_validName_returnTrue() {
        String string = "Garden 1";
        Assertions.assertTrue(valuesValidator.checkBlank(string));
        Assertions.assertTrue(valuesValidator.checkCharacters(string));
    }

    @Test
    void checkName_blankName_returnFalse() {
        String string = " ";
        Assertions.assertFalse(valuesValidator.checkBlank(string));
    }

    @Test
    void checkSize_ValidSize_returnTrue() {
        Float size = null;
        Assertions.assertTrue(valuesValidator.checkSize(size));
    }

    @Test
    void checkSize_InvalidSize_returnFalse() {
        Float size = -1f;
        Assertions.assertFalse(valuesValidator.checkSize(size));
    }

    @Test
    void checkCount_InvalidCount_returnFalse() {
        Integer count = -1;
        Assertions.assertFalse(valuesValidator.checkCount(count));
    }

    @Test
    void checkCount_ValidCount_returnTrue() {
        Integer count = 100;
        Assertions.assertTrue(valuesValidator.checkCount(count));
    }

    @Test
    void checkDesc_InvalidDesc_returnFalse() {
        String desc = "QUOBSl9rXre7kohXEjS83RTWtw69gHoyCYLj9SLb2CvVgQcdp6xgm7mgTW3WhRkabgyZ60AyhLkSo8zEGlkUW13y 390JTF8nnrPBT0GOxYzW5OrkeYqQLg8vubaMfgESUNudCR1Px0GadxfmNhGl9qyEAoRKksJO3n3HJ6ihPVjtFHbOuxbJ PTjKrNBUoBtKEAXIzKsK3jgmKP4WKTnbimPbAhfs0FbJqrzTPoBH 61smRFEcRUG6NRp61hqWtlFYxaIeSoxY7 Z68qwOvZFWAdhMdm7eedmDXvJYYJSTb1QVjAImuflA Rtblxom3sthvIX9vVs70I8pPlLcxaIFcy0POUPSCkcmLP7 U99cb0zplM5UX5cSKbHdHL 4uzDvpdHtV1vioiuIhm2olRJwTXJARB011sGyr55g0E05e4v bVCSDy0MuYDGQ770yiXyXawAEFm5jv2rIdoFBJEupmTmaErM KwmDbLr5oNyV1JreejyEIBpc6s7JHmUj1234567890";
        Assertions.assertFalse(valuesValidator.checkDescription(desc));
    }

    @Test
    void checkDesc_ValidDesc_returnTrue() {
        String desc = "wdfghjio(*&^%$#f5678";
        Assertions.assertTrue(valuesValidator.checkDescription(desc));
    }

    @Test
    void checkValidName_returnTrue() {
        String name = "Jane";
        Assertions.assertTrue(valuesValidator.checkUserName(name));
    }

    @Test
    void checkInvalidName_returnFalse() {
        String name = "4eva!";
        Assertions.assertFalse(valuesValidator.checkUserName((name)));
    }

    @Test
    void checkValidNameLength_returnTrue() {
        String name = "Jane";
        Assertions.assertTrue(valuesValidator.checkNameLength(name));
    }

    @Test
    void checkInvalidNameLength_returnFalse() {
        String name = "AaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaA";
        Assertions.assertFalse(valuesValidator.checkNameLength(name));
    }

    @Test
    void checkSamePassword_returnTrue() {
        String password = "abE123!!";
        Assertions.assertTrue(valuesValidator.checkConfirmPasswords(password, password));
    }

    @Test
    void checkDiffPassword_returnFalse() {
        String pass1 = "Passw0rd$";
        String pass2 = "pA$$woe4";
        Assertions.assertFalse(valuesValidator.checkConfirmPasswords(pass1, pass2));
    }

    @Test
    void checkUnder120Valid_returnTrue() {
        int age = 20;
        String dob = LocalDate.now().minusYears(age).toString();
        Assertions.assertTrue(valuesValidator.checkUnder120(dob));
    }

    @Test
    void checkOver120Valid_returnFalse() {
        int age = 121;
        String dob = LocalDate.now().minusYears(age).toString();
        Assertions.assertFalse(valuesValidator.checkUnder120(dob));
    }

    @Test
    void checkEmailNotInUse_returnTrue() {
        String email = "jane@doe.nz";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        Assertions.assertTrue(valuesValidator.emailInUse(email, userService));
    }

    @Test
    void checkEmailInUse_returnFalse() {
        String email = "jane@doe.nz";
        String firstName = "Jane";
        String lastName = "Doe";
        String password = "abc!1E";
        String dob = LocalDate.now().minusYears(20).toString();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(new User(email, firstName, lastName,password,dob));
        Assertions.assertFalse(valuesValidator.emailInUse(email, userService));
    }
}
