package nz.ac.canterbury.seng302.gardenersgrove.config;

import org.springframework.web.util.UriTemplate;

import java.net.URI;

public class UriConfig {
    public static final String NEW_GARDEN_URI_STRING = "/garden/new";
    public static final String VIEW_GARDEN_URI_STRING = "/garden/{gardenId}";
    public static final String EDIT_GARDEN_URI_STRING = "/garden/{gardenId}/edit";
    public static final String NEW_PLANT_URI_STRING = "/garden/{gardenId}/plant/new";
    public static final String EDIT_PLANT_URI_STRING = "/garden/{gardenId}/plant/{plantId}";
    public static final String UPLOAD_PLANT_IMAGE_URI_STRING = "/garden/{gardenId}/plant/{plantId}/image";
    public static final String VIEW_ALL_GARDENS_URI_STRING = "/gardens";

    private static final UriTemplate VIEW_GARDEN_URI = new UriTemplate(VIEW_GARDEN_URI_STRING);
    private static final UriTemplate EDIT_GARDEN_URI = new UriTemplate(EDIT_GARDEN_URI_STRING);
    private static final UriTemplate NEW_PLANT_URI = new UriTemplate(NEW_PLANT_URI_STRING);
    private static final UriTemplate EDIT_PLANT_URI = new UriTemplate(EDIT_PLANT_URI_STRING);
    private static final UriTemplate UPLOAD_PLANT_IMAGE_URI = new UriTemplate(UPLOAD_PLANT_IMAGE_URI_STRING);

    public static URI newGardenUri() {
        return URI.create(NEW_GARDEN_URI_STRING);
    }

    public static URI viewGardenUri(String gardenId) {
        return VIEW_GARDEN_URI.expand(gardenId);
    }

    public static URI editGardenUri(String gardenId) {
        return EDIT_GARDEN_URI.expand(gardenId);
    }

    public static URI newPlantUri(String gardenId) {
        return NEW_PLANT_URI.expand(gardenId);
    }

    public static URI editPlantUri(String gardenId, String plantId) {
        return EDIT_PLANT_URI.expand(gardenId, plantId);
    }

    public static URI uploadPlantImageUri(String gardenId, String plantId) {
        return UPLOAD_PLANT_IMAGE_URI.expand(gardenId, plantId);
    }

    public static URI viewAllGardensUri() {
        return URI.create(VIEW_ALL_GARDENS_URI_STRING);
    }
}