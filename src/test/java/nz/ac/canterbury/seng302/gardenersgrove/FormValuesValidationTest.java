package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(FormValuesValidator.class)

public class FormValuesValidationTest {
    FormValuesValidator valuesValidator = new FormValuesValidator();

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
}
