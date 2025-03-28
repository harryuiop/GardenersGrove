package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Service to set up cookies for client
 */
@Service
public class CookiesService {

    /**
     * setup cookies for controlling rain advice popup close
     *
     * @param gardenId garden id
     * @param deployed deployed environment
     * @return cookies for advice popup close
     */
    public Cookie cookieWeatherRainPopupClose(long gardenId, int deployed) {

        // setup maxAge time for cookie to survive until end of the day
        // The codes about setting maxAge is based on GitHub copilot suggestion
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).with(LocalTime.MIDNIGHT);
        Duration durationUntilNextMidnight = Duration.between(now, nextMidnight);

        Cookie cookie = new Cookie("rainPopupSeen", "true");
        cookie.setHttpOnly(true);
        String path = deployed == 1 ? String.format("/test/garden/%d", gardenId)
                                    : deployed == 2 ? String.format("/prod/garden/%d", gardenId)
                                    : String.format("/garden/%d", gardenId);
        cookie.setPath(path);
        cookie.setMaxAge((int) durationUntilNextMidnight.getSeconds()); // alive until midnight

        return cookie;
    }
}
