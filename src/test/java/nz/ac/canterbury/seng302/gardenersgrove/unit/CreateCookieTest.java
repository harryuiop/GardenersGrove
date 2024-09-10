package nz.ac.canterbury.seng302.gardenersgrove.unit;

import jakarta.servlet.http.Cookie;
import nz.ac.canterbury.seng302.gardenersgrove.service.CookiesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateCookieTest {

    CookiesService cookiesService = new CookiesService();

    @Test
    void createCookieForWeatherPopupMessage() {

        long tempGardenId = 1;
        int tempBaseUri = 0;
        Cookie cookie = cookiesService.cookieWeatherRainPopupClose(tempGardenId, tempBaseUri);

        Assertions.assertTrue(cookie.isHttpOnly());
        Assertions.assertEquals("/garden/"+tempGardenId, cookie.getPath());
    }
}
