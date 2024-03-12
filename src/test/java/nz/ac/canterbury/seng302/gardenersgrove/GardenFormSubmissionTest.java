package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.GardenFormSubmission;
import org.assertj.core.internal.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(GardenFormSubmission.class)

class GardenFormSubmissionTest {
    GardenFormSubmission gardenFormSubmission = Mockito.spy(GardenFormSubmission.class);

    @Test
    void testValidString() {
        String string = "qwertyuiopasdfghjklzxcvbnmABC -'";
        boolean answer = gardenFormSubmission.checkString(string);
        Assertions.assertTrue(answer);
    }
    @Test
    void testInvalidString() {
        String string = "!@#$%{}";
        boolean answer = gardenFormSubmission.checkString(string);
        Assertions.assertFalse(answer);
    }
    @Test
    void testValidName() {
        String string = "Garden 1";
        Assertions.assertFalse(gardenFormSubmission.checkName(string));
    }
    @Test
    void testBlankString() {
        String string = " ";
        Mockito.when(gardenFormSubmission.checkString(Mockito.any()));
        Assertions.assertThrows(IllegalArgumentException, gardenFormSubmission::checkName, string);
    }
}
