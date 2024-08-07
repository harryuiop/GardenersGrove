package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    public Cookie cookieWeatherRainPopupClose(long gardenId) {

        // setup maxAge time for cookie to survive until end of the day
        // The codes about setting maxAge is based on GitHub copilot suggestion
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).with(LocalTime.MIDNIGHT);
        Duration durationUntilNextMidnight = Duration.between(now, nextMidnight);
        int secondsUntilNextMidnight = (int) durationUntilNextMidnight.getSeconds();

        Cookie cookie = new Cookie("rainPopupSeen", "true");
        cookie.setHttpOnly(true);
        cookie.setPath("/garden/" + gardenId);
        cookie.setMaxAge(secondsUntilNextMidnight); // alive until midnight

        return cookie;
    }
}
