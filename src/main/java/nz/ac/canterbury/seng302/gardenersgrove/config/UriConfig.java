package nz.ac.canterbury.seng302.gardenersgrove.config;

import org.springframework.web.util.UriTemplate;

import java.net.URI;

public class UriConfig {
    public static final String HOME_URI_STRING = "/";
    public static final String LOGIN_URI_STRING = "/login";
    public static final String LOGOUT_URI_STRING = "/logout";
    public static final String REGISTER_URI_STRING = "/register";
    public static final String VERIFY_EMAIL_URI_STRING = "/register/verify";
    public static final String RESET_PASSWORD_URI_STRING = "/login/reset-password/verify/{token}/{userId}";
    public static final String RESET_PASSWORD_EMAIL_URI_STRING = "/login/reset-password/email";
    public static final String VIEW_PROFILE_URI_STRING = "/profile";
    public static final String EDIT_PROFILE_URI_STRING = "/profile/edit";
    public static final String EDIT_PASSWORD_URI_STRING = "/profile/edit/password";
    public static final String UPLOAD_PROFILE_IMAGE_URI_STRING = "/profile/image";
    public static final String NEW_GARDEN_URI_STRING = "/garden/new";
    public static final String VIEW_GARDEN_URI_STRING = "/garden/{gardenId}";
    public static final String MONITOR_GARDEN_URI_STRING = "/garden/{gardenId}/monitor";
    public static final String NEW_GARDEN_TAG_URI_STRING = "/garden/{gardenId}/tag/new";
    public static final String EDIT_GARDEN_URI_STRING = "/garden/{gardenId}/edit";
    public static final String NEW_PLANT_URI_STRING = "/garden/{gardenId}/plant/new";
    public static final String EDIT_PLANT_URI_STRING = "/garden/{gardenId}/plant/{plantId}";
    public static final String UPLOAD_PLANT_IMAGE_URI_STRING = "/garden/{gardenId}/plant/{plantId}/image";
    public static final String VIEW_ALL_GARDENS_URI_STRING = "/gardens";
    public static final String MAKE_GARDEN_PUBLIC_STRING = "/public/{gardenId}";
    public static final String BROWSE_PUBLIC_GARDENS_URI_STRING = "/browse";
    public static final String MANAGE_FRIENDS_URI_STRING = "/friends";
    public static final String VIEW_ALL_FRIENDS_GARDENS_URI_STRING = "/friends/{friendId}/gardens";
    public static final String VIEW_FRIENDS_PROFILE_URI_STRING = "/friends/{friendId}/profile";
    public static final String SEARCH_RESULTS_STRING = "/search/results";
    public static final String SEND_FREIND_REQUEST_STRING = "/search/results/send";
    public static final String SET_WEATHER_POPUP_ALERT_COOKIES = "/cookies/set-rain-popup/{gardenId}/{deployed}";
    public static final String ARDUINO_SENSOR_DATA = "/sensor-data";
    private static final UriTemplate RESET_PASSWORD_URI = new UriTemplate(RESET_PASSWORD_URI_STRING);
    private static final UriTemplate VIEW_GARDEN_URI = new UriTemplate(VIEW_GARDEN_URI_STRING);
    private static final UriTemplate MONITOR_GARDEN_URI = new UriTemplate(MONITOR_GARDEN_URI_STRING);
    private static final UriTemplate NEW_GARDEN_TAG_URI = new UriTemplate(NEW_GARDEN_TAG_URI_STRING);
    private static final UriTemplate VIEW_ALL_FRIENDS_GARDENS_URI = new UriTemplate(VIEW_ALL_FRIENDS_GARDENS_URI_STRING);
    private static final UriTemplate EDIT_GARDEN_URI = new UriTemplate(EDIT_GARDEN_URI_STRING);
    private static final UriTemplate NEW_PLANT_URI = new UriTemplate(NEW_PLANT_URI_STRING);
    private static final UriTemplate EDIT_PLANT_URI = new UriTemplate(EDIT_PLANT_URI_STRING);
    private static final UriTemplate UPLOAD_PLANT_IMAGE_URI = new UriTemplate(UPLOAD_PLANT_IMAGE_URI_STRING);
    private static final UriTemplate MAKE_GARDEN_PUBLIC_URI = new UriTemplate(MAKE_GARDEN_PUBLIC_STRING);
    private static final UriTemplate SET_WEATHER_POPUP_ALERT_COOKIES_URI = new UriTemplate(SET_WEATHER_POPUP_ALERT_COOKIES);

    private UriConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static URI homeUri() {
        return URI.create(HOME_URI_STRING);
    }

    public static URI loginUri() {
        return URI.create(LOGIN_URI_STRING);
    }

    public static URI logoutUri() {
        return URI.create(LOGOUT_URI_STRING);
    }

    public static URI registerUri() {
        return URI.create(REGISTER_URI_STRING);
    }

    public static URI resetPasswordEmailUri() {
        return URI.create(RESET_PASSWORD_EMAIL_URI_STRING);
    }

    public static URI verifyEmailUri() {
        return URI.create(VERIFY_EMAIL_URI_STRING);
    }

    public static URI viewProfileUri() {
        return URI.create(VIEW_PROFILE_URI_STRING);
    }

    public static URI viewFriendsUri() {
        return URI.create(MANAGE_FRIENDS_URI_STRING);
    }

    public static URI editProfileUri() {
        return URI.create(EDIT_PROFILE_URI_STRING);
    }

    public static URI editPasswordUri() {
        return URI.create(EDIT_PASSWORD_URI_STRING);
    }

    public static URI uploadProfileImageUri() {
        return URI.create(UPLOAD_PROFILE_IMAGE_URI_STRING);
    }

    public static URI resetPasswordUri(String token, long userId) {
        return RESET_PASSWORD_URI.expand(token, userId);
    }
    public static URI newGardenUri() {
        return URI.create(NEW_GARDEN_URI_STRING);
    }

    public static URI viewGardenUri(long gardenId) {
        return VIEW_GARDEN_URI.expand(gardenId);
    }
    public static URI monitorGardenUri(long gardenId) {
        return MONITOR_GARDEN_URI.expand(gardenId);
    }
    public static URI newGardenTagUri(long gardenId) {
        return NEW_GARDEN_TAG_URI.expand(gardenId);
    }

    public static URI viewAllFriendsGardensUri(long friendId) {
        return VIEW_ALL_FRIENDS_GARDENS_URI.expand(friendId);
    }

    public static URI editGardenUri(long gardenId) {
        return EDIT_GARDEN_URI.expand(gardenId);
    }

    public static URI newPlantUri(long gardenId) {
        return NEW_PLANT_URI.expand(gardenId);
    }

    public static URI editPlantUri(long gardenId, long plantId) {
        return EDIT_PLANT_URI.expand(gardenId, plantId);
    }

    public static URI uploadPlantImageUri(long gardenId, long plantId) {
        return UPLOAD_PLANT_IMAGE_URI.expand(gardenId, plantId);
    }
    public static URI sendCookiesForWeatherAdvicePopup(long gardenId, int deployed) {
        return SET_WEATHER_POPUP_ALERT_COOKIES_URI.expand(gardenId, deployed);
    }
    public static URI viewAllGardensUri() {
        return URI.create(VIEW_ALL_GARDENS_URI_STRING);
    }
    public static URI makeGardenPublicUri(long gardenId) {
        return MAKE_GARDEN_PUBLIC_URI.expand(gardenId);
    }
    public static URI browsePublicGardensUri() {
        return URI.create(BROWSE_PUBLIC_GARDENS_URI_STRING);
    }
    public static URI searchResultsUri() {return URI.create(SEARCH_RESULTS_STRING);}
    public static URI sendFriendRequestUri() {return URI.create(SEND_FREIND_REQUEST_STRING);}
}