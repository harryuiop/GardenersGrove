package nz.ac.canterbury.seng302.gardenersgrove.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageStore {
    private static final String UPLOAD_DIR = "storage/uploads/";

    private ImageStore() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Saves an image to the server and returns the filename
     *
     * @param image The image to save
     * @return The filename of the saved image
     * @throws IOException If the image could not be saved
     */
    public static String storeImage(MultipartFile image) throws IOException {
        // Check if the directory exists, create if not
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory");
        }
        String contentType = image.getContentType();
        if (contentType == null) {
            throw new IOException("No content type");
        }
        String extension = contentType.split("/")[1];
        if (extension.equals("svg+xml")) {
            extension = "svg";
        }
        String newFilename = UUID.randomUUID() + "." + extension;

        Path path = Paths.get(UPLOAD_DIR + newFilename);
        Files.write(path, image.getBytes());

        return newFilename;
    }
}
