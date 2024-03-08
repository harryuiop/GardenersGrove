package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageValidationTest {
    private static ImageValidation imageValidation;

    private final int BYTES_IN_KBS = 1024;

    @BeforeAll
    public static void setup() {
        imageValidation = new ImageValidation();
    }

    @ParameterizedTest
    @ValueSource(strings = {"plant.png", "plant.svg", "plant.jpg"})
    void testImageTypeValid(String filename) {
        assertTrue(imageValidation.checkImageType(filename));
    }

    @Test
    void testImageTypeInvalid() {
        String filename = "plant.gif";
        assertFalse(imageValidation.checkImageType(filename));
    }

    @Test
    void testImageSizeValid() {
        byte[] smallImage = fakeByteArray(1);
        assertTrue(imageValidation.checkImageSize(smallImage));
    }

    @Test
    void testLargeImageSizeInvalid() {
        byte[] largeImage = fakeByteArray(11);
        assertFalse(imageValidation.checkImageSize(largeImage));
    }

    private byte[] fakeByteArray(int sizeInMB) {
        int sizeInBytes = sizeInMB * BYTES_IN_KBS * BYTES_IN_KBS;
        byte[] fakeByteArray = new byte[sizeInBytes];
        Arrays.fill(fakeByteArray, (byte) 0);
        return fakeByteArray;
    }

}
