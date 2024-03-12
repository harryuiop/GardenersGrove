package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(FormValuesValidator.class)

public class FormValuesValidationTest {
    FormValuesValidator valuesValidator = new FormValuesValidator();

    @Test
    void checkString_validString_returnTrue() {
        String string = "qwertyuiopasdfghjklzxcvbnmABC -'";
        Assertions.assertTrue(valuesValidator.checkString(string));
    }

    @Test
    void checkString_invalidString_returnFalse() {
        String string = "!@#$%{}";
        Assertions.assertFalse(valuesValidator.checkString(string));
    }

    @Test
    void checkName_validName_returnTrue() {
        String string = "Garden 1";
        Assertions.assertEquals(null, valuesValidator.checkName(string));
    }

    @Test
    void checkName_blankName_returnBlank() {
        String string = " ";
        Assertions.assertEquals("Blank", valuesValidator.checkName(string));
    }

    @Test
    void checkName_InvalidName_returnInvalidChar() {
        String string = "#$%^&*";
        Assertions.assertEquals("InvalidChar", valuesValidator.checkName(string));
    }

    @Test
    void checkSize_ValidSize_returnNull() {
        Float size = null;
        Assertions.assertEquals(null, valuesValidator.checkSize(size));
    }

    @Test
    void checkSize_InvalidSize_returnNegative() {
        Float size = -1f;
        Assertions.assertEquals("Negative", valuesValidator.checkSize(size));
    }
}
