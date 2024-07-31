package nz.ac.canterbury.seng302.gardenersgrove.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import nz.ac.canterbury.seng302.gardenersgrove.service.CookiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cookies")
public class CookiesRestController {

    private final CookiesService cookiesService;

    @Autowired
    public CookiesRestController (CookiesService cookiesService){
        this.cookiesService = cookiesService;
    }

    Logger logger = LoggerFactory.getLogger(CookiesRestController.class);
    @PostMapping("/set-rain-popup")
    public void setRainPopup(HttpServletResponse response) {
        logger.info("POST: /cookies/set-rain-popup");

        response.addCookie(cookiesService.cookieWeatherRainPopupClose());
    }
}
