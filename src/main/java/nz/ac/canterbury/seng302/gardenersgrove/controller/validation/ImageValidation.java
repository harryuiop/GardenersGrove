package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
            if (fileName.toLowerCase().endsWith(requiredType.toLowerCase())) {
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

    /**
     * Get Image object which contains the image bytes, type of file and validation results given
     * the imageFile submitted by a form.
     *
     * @param imageFile Image file submitted by form.
     * @return Image object.
     * @throws IOException
     */
    public Image getImageResults(MultipartFile imageFile) throws IOException {
        boolean imageIsValid;
        boolean imageIsValidType = false;
        boolean imageIsValidSize = false;
        byte[] imageBytes = null;
        String imageType = null;

        if (imageFile == null) {
            imageIsValid = true;
        } else {
            imageBytes = imageFile.getBytes(); // Convert MultipartFile to byte[] to be saved in database
            imageType = imageFile.getContentType();
            imageIsValidType = this.checkImageType(imageFile.getOriginalFilename());
            imageIsValidSize = this.checkImageSize(imageFile.getBytes());
            imageIsValid = imageIsValidType && imageIsValidSize;
        }

        return new Image(imageIsValidType, imageIsValidSize, imageIsValid, imageType, imageBytes);
    }
}
