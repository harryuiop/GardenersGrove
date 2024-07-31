package nz.ac.canterbury.seng302.gardenersgrove.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cookies")
public class CookiesRestController {

    @PostMapping("/set-rain-popup")
    public void setRainPopup(HttpServletResponse response) {

        Cookie cookie = new Cookie("rainPopupSeen", "true");
        cookie.setSecure(true);
        cookie.setMaxAge(24 * 60 * 60); // 24 hours
        // cookie.setPath("Name of path for garden");
        response.addCookie(cookie);
    }
}
