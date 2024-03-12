package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.GardenFormSubmission;
import org.assertj.core.internal.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
    void checkName_validName_returnFalse() {
        String string = "Garden 1";
        Assertions.assertFalse(gardenFormSubmission.checkName(string));
    }
    @Test
    void checkString_validName_returnTrue() {
        String string = " ";
        Throwable error = Assertions.assertThrows(IllegalArgumentException.class, () -> gardenFormSubmission.checkName(string));
        Assertions.assertEquals("Blank", error.getMessage());
    }
    @Test
    void testInvalidName() {
        String string = "#$%^&*";
        Throwable error = Assertions.assertThrows(IllegalArgumentException.class, () -> gardenFormSubmission.checkName(string));
        Assertions.assertEquals("InvalidChar", error.getMessage());
    }
}
