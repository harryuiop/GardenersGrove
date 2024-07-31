package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

/**
 * Service to set up cookies for client
 */
@Service
public class CookiesService {

    /**
     * setup cookies for controlling rain advice popup close
     *
     * @return cookies for advice popup close
     */
    public Cookie cookieWeatherRainPopupClose() {
        Cookie cookie = new Cookie("rainPopupSeen", "true");
        cookie.setSecure(true);
        cookie.setMaxAge(24 * 60 * 60); // 24 hours
        // cookie.setPath("Name of path for garden");

        return cookie;
    }
}
