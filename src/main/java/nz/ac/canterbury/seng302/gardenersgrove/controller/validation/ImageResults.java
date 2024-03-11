package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

/**
 * Get image results object which includes if the image is valid, the image type, and the bytes
 * array representation of the image.
 */
public class ImageResults {

    private boolean imageIsValidType;
    private boolean imageIsValidSize;

    private boolean imageIsValid;

    private String imageType;

    private byte[] imageBytes;

    /**
     * Image results initializer.
     *
     * @param imageIsValidType If image is of a valid type.
     * @param imageIsValidSize If image is not too large.
     * @param imageIsValid If image is valid or not set.
     * @param imageType File extension of image.
     * @param imageBytes Bytes array representation of image.
     */
    public ImageResults(boolean imageIsValidType, boolean imageIsValidSize, boolean imageIsValid, String imageType, byte[] imageBytes) {
        this.imageIsValidType = imageIsValidType;
        this.imageIsValidSize = imageIsValidSize;
        this.imageIsValid = imageIsValid;
        this.imageType = imageType;
        this.imageBytes = imageBytes;
    }

    public boolean getImageIsValidType() {
        return imageIsValidType;
    }

    public boolean getImageIsValidSize() {
        return imageIsValidSize;
    }

    public boolean getImageIsValid() {
        return imageIsValid;
    }

    public String getImageType() {
        return imageType;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    /**
     * Check if image has been set.
     *
     * @return If image is set.
     */
    public boolean isImageSet() {
        return imageBytes.length > 0;
    }
}
