package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.valadation.ImageValidation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageValidationTest {
    private static ImageValidation imageValidation;

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
        int sizeInBytes = sizeInMB * 1024 * 1024; // 1 MB = 1024 * 1024 bytes
        byte[] fakeByteArray = new byte[sizeInBytes];
        Arrays.fill(fakeByteArray, (byte) 0); // Fill the array with zeros
        return fakeByteArray;
    }

}
