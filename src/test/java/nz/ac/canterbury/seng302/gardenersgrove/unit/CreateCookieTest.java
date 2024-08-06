package nz.ac.canterbury.seng302.gardenersgrove.unit;

import jakarta.servlet.http.Cookie;
import nz.ac.canterbury.seng302.gardenersgrove.service.CookiesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateCookieTest {

    CookiesService cookiesService = new CookiesService();

    @Test
    void createCookieForWeatherPopupMessage() {

        Cookie cookie = cookiesService.cookieWeatherRainPopupClose();

        Assertions.assertTrue(cookie.isHttpOnly());
        Assertions.assertEquals("/garden", cookie.getPath());
    }
}
