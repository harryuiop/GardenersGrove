package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

public class Image {

    private boolean imageIsValidType;
    private boolean imageIsValidSize;

    private boolean imageIsValid;

    private String imageType;

    private byte[] imageBytes;

    public Image(boolean imageIsValidType, boolean imageIsValidSize, boolean imageIsValid, String imageType, byte[] imageBytes) {
        this.imageIsValidType = imageIsValidType;
        this.imageIsValidSize = imageIsValidSize;
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
}
