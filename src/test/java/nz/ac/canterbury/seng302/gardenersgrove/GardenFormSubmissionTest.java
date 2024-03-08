package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.components.FormSubmission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.HashMap;

@DataJpaTest
@Import(FormSubmission.class)

public class GardenFormSubmissionTest {
    FormSubmission formSubmission = Mockito.spy(FormSubmission.class);

    @Test
    public void testValidString() {
        String string = "qwertyuiopasdfghjklzxcvbnmABC -'";
        boolean answer = formSubmission.checkString(string);
        Assertions.assertTrue(answer);
    }
    @Test
    public void testInvalidString() {
        String string = "!@#$%{}";
        boolean answer = formSubmission.checkString(string);
        Assertions.assertFalse(answer);
    }
    @Test
    public void testAllValuesEnteredValid() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        Assertions.assertEquals(errors, new HashMap<String, String>());
    }
    @Test
    public void testEnteredNegativeSize() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = -1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenSizeError", "Garden size must be a positive number");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void testEnteredBlankName() {
        String name = "";
        String location = "Christchurch";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        Assertions.assertEquals(errors, correctErrors);
    }
    @Test
    public void testEnteredBlankLocation() {
        String name = "Garden 1";
        String location = "";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void testEnteredBlankSize() {
        String name = "Garden 1";
        String location = "Christchurch";
        Float size = null;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void testEnteredBlankNameAndLocation() {
        String name = "";
        String location = " ";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put("gardenLocationError", "Location cannot be empty");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void testEnteredInvalidName() {
        String name = "This!@$%";
        String location = "Christchurch";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        Mockito.when(formSubmission.checkString(Mockito.any())).thenReturn(false);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put(
                "gardenNameError",
                "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void testEnteredInvalidLocation() {
        String name = "Garden 1";
        String location = "#1";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        Mockito.when(formSubmission.checkString(Mockito.any())).thenReturn(false);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(errors, correctErrors);
    }

    @Test
    public void testEnteredBlankNameInvalidLocation() {
        String name = "  ";
        String location = "#1";
        Float size = 1.5f;
        HashMap<String, String> errors = formSubmission.formErrors(name, location, size);
        Mockito.when(formSubmission.checkString(Mockito.any())).thenReturn(false);
        HashMap<String, String> correctErrors = new HashMap<String, String>();
        correctErrors.put("gardenNameError", "Garden name cannot by empty");
        correctErrors.put(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
        );
        Assertions.assertEquals(errors, correctErrors);
    }
}
