package nz.ac.canterbury.seng302.gardenersgrove.controller;


import jakarta.servlet.http.HttpServletResponse;
import nz.ac.canterbury.seng302.gardenersgrove.service.CookiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Called through javascript for controlling setting up cookies through cookiesService
 */
@RestController
public class CookiesRestController {

    private final CookiesService cookiesService;

    /**
     * Constructor of this Class to autowiring CookiesService
     *
     * @param cookiesService Service that provides all functions to setting up cookies
     */
    @Autowired
    public CookiesRestController (CookiesService cookiesService){
        this.cookiesService = cookiesService;
    }

    Logger logger = LoggerFactory.getLogger(CookiesRestController.class);

    /**
     * control cookies setup Rain popup message close for 24 hours
     *
     * @param response httpServletResponse
     */
    @GetMapping(SET_WEATHER_POPUP_ALERT_COOKIES)
    public void setRainPopup(HttpServletResponse response,
                             @PathVariable(name = "gardenId") long gardenId,
                             @PathVariable(name = "deployed") int deployed) {
        logger.info("GET {}", sendCookiesForWeatherAdvicePopup(gardenId, deployed));

        response.addCookie(cookiesService.cookieWeatherRainPopupClose(gardenId, deployed));
    }
}
