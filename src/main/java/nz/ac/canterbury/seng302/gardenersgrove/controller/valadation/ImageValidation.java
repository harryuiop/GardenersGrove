package nz.ac.canterbury.seng302.gardenersgrove.controller.valadation;

import java.util.Arrays;
import java.util.List;

/**
 * Test Image is valid
 */
public class ImageValidation {
    private final int MAX_IMAGE_SIZE_KB = 10000;

    private final int BYTES_IN_KBS = 1024;

    private final List<String> REQUIRED_IMAGE_TYPES = Arrays.asList("jpg", "png", "svg");

    /**
     * Check the image is of a suitable image type (jpg, png, svg).
     *
     * @param fileName Name of image file
     * @return If image is correct type
     */
    public boolean checkImageType(String fileName) {
        for (String requiredType : REQUIRED_IMAGE_TYPES) {
            if (fileName.endsWith(requiredType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the image is less than 10MB.
     *
     * @param imageBytes Bytes array representation of image
     * @return If image is less than 10MB
     */
    public boolean checkImageSize(byte[] imageBytes) {
        return imageBytes.length / BYTES_IN_KBS <= MAX_IMAGE_SIZE_KB;
    }
}
