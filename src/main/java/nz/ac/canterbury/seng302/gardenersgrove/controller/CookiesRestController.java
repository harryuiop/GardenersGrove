package nz.ac.canterbury.seng302.gardenersgrove.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import nz.ac.canterbury.seng302.gardenersgrove.service.CookiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/cookies/set-rain-popup")
    public void setRainPopup(HttpServletResponse response) {
        logger.info("GET: /cookies/set-rain-popup");

        response.addCookie(cookiesService.cookieWeatherRainPopupClose());
    }
}
