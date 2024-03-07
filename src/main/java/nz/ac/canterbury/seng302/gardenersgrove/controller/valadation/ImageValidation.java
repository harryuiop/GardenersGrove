package nz.ac.canterbury.seng302.gardenersgrove.controller.valadation;

import java.util.Arrays;
import java.util.List;

/**
 * Test Image is valid
 */
public class ImageValidation {
    private final int MAX_IMAGE_SIZE_KB = 10000;

    private final int BYTES_IN_KBS = 1024;

    private final List<String> requiredImageTypes = Arrays.asList("jpg", "png", "svg");

    public boolean checkImageType(String fileName) {
        for (String requiredType : requiredImageTypes) {
            if (fileName.endsWith(requiredType)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkImageSize(byte[] imageBytes) {
        return imageBytes.length / BYTES_IN_KBS <= MAX_IMAGE_SIZE_KB;
    }
}
