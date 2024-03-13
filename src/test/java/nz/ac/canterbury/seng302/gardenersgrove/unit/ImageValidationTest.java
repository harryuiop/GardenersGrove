package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageValidationTest {
    private static ImageValidator imageValidator;

    private final int BYTES_IN_MB = 1_000_000;

    private static MultipartFile mockPng = new MockMultipartFile("plant.png", "plant.png", "image/png", new byte[1]);
    private static MultipartFile mockJpg = new MockMultipartFile("plant.jpg", "plant.jpg", "image/jpeg", new byte[10_000_001]);
    private static MultipartFile mockSvg = new MockMultipartFile("plant.svg", "plant.svg", "image/svg", new byte[1]);
    private static MultipartFile mockGif = new MockMultipartFile("plant.gif", "plant.gif", "image/gif", new byte[1]);

    @Test
    void validateImage_pngType_returnTrue() {
        imageValidator = new ImageValidator(mockPng);
        assertTrue(imageValidator.validImageType());
    }

    @Test
    void validateImage_jpegType_returnTrue() {
        imageValidator = new ImageValidator(mockJpg);
        assertTrue(imageValidator.validImageType());
    }

    @Test
    void validateImage_svgType_returnTrue() {
        imageValidator = new ImageValidator(mockSvg);
        assertTrue(imageValidator.validImageType());
    }

    @Test
    void validateImage_invalidType_returnFalse() {
        imageValidator = new ImageValidator(mockGif);
        assertFalse(imageValidator.validImageType());
    }

    @Test
    void validateImage_validSize_returnTrue() {
        imageValidator = new ImageValidator(mockPng);
        assertTrue(imageValidator.validSize());
    }

    @Test
    void testLargeImageSizeInvalid() {
        ImageValidator imageValidator = new ImageValidator(mockJpg);
        assertFalse(imageValidator.validSize());
    }
}
