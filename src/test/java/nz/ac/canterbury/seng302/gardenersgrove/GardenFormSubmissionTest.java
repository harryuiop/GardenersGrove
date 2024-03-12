package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.GardenFormSubmission;
import org.assertj.core.internal.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

import java.lang.reflect.Executable;

@DataJpaTest
@Import(GardenFormSubmission.class)

class GardenFormSubmissionTest {
    GardenFormSubmission gardenFormSubmission = Mockito.spy(GardenFormSubmission.class);

    @Test
    void checkString_validString_returnTrue() {
        String string = "qwertyuiopasdfghjklzxcvbnmABC -'";
        boolean answer = gardenFormSubmission.checkString(string);
        Assertions.assertTrue(answer);
    }
    @Test
    void checkString_invalidString_returnFalse() {
        String string = "!@#$%{}";
        boolean answer = gardenFormSubmission.checkString(string);
        Assertions.assertFalse(answer);
    }
    @Test
    void checkName_validName_returnTrue() {
        String string = "Garden 1";
        Assertions.assertTrue(gardenFormSubmission.checkName(string));
    }
    @Test
    void checkName_blankName_throwErrorBlank() {
        String string = " ";
        Throwable error = Assertions.assertThrows(IllegalArgumentException.class, () -> gardenFormSubmission.checkName(string));
        Assertions.assertEquals("Blank", error.getMessage());
    }
    @Test
    void checkName_InvalidName_throwErrorInvalidChar() {
        String string = "#$%^&*";
        Throwable error = Assertions.assertThrows(IllegalArgumentException.class, () -> gardenFormSubmission.checkName(string));
        Assertions.assertEquals("InvalidChar", error.getMessage());
    }
    @Test
    void checkSize_ValidSize_throwErrorInvalidChar() {
        Float size = null;
        Assertions.assertTrue(gardenFormSubmission.checkSize(size));
    }
    @Test
    void checkSize_InvalidSize_throwErrorInvalidChar() {
        Float size = -1f;
        Throwable error = Assertions.assertThrows(IllegalArgumentException.class, () -> gardenFormSubmission.checkSize(size));
        Assertions.assertEquals("Negative", error.getMessage());
    }

}
