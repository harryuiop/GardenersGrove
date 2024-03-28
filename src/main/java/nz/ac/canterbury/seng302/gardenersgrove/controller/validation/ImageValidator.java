package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test Image is valid.
 */
public class ImageValidator {
    protected static final int MAX_IMAGE_SIZE_BYTES = 10_000_000;

    protected static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/svg+xml");

    private final MultipartFile image;

    private final Map<String, String> errorMessages = new HashMap<>();

    /**
     * Create a new image validator.
     * Call `isValid` to check the image is valid.
     *
     * @param image The image to validate.
     */
    public ImageValidator(MultipartFile image) {
        this.image = image;
    }

    /**
     * Check the image is of an allowable image type (jpg, png, svg).
     *
     * @return `true` if the image is of an allowable type, `false` otherwise.
     */
    public boolean validImageType() {
        String contentType = this.image.getContentType();
        if (contentType == null) {
            return false;
        }
        System.out.println("contentType = " + contentType);
        return ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }

    /**
     * Check the image is less than the maximum allowable size (10MB).
     *
     * @return `true` if the image is less than the maximum allowable size, `false` otherwise.
     */
    public boolean validSize() {
        return this.image.getSize() <= MAX_IMAGE_SIZE_BYTES;
    }

    /**
     * Check if the image is valid.
     * Call `getErrorMessages` to get any error messages if this returns `false`.
     *
     * @return `true` if the image is valid, `false` otherwise.
     */
    public boolean isValid() {
        boolean validType = validImageType();
        boolean validSize = validSize();
        if (!validType) {
            errorMessages.put("plantImageTypeError", "Image must be of type png, jpg or svg.");
        }
        if (!validSize) {
            errorMessages.put("plantImageSizeError", "Image must be less than 10MB.");
        }
        return validType && validSize;
    }

    /**
     * Get any error messages from the image validation.
     * Only call this if `isValid` returns `false`.
     *
     * @return A list of error messages.
     */
    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }
}
