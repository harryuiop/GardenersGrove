package nz.ac.canterbury.seng302.gardenersgrove.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Handles GET requests to the "/" URL.
     * Displays the login page.
     *
     * @return a redirect to the login page
     */
    @GetMapping("/")
    public String getLandingPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // The home page is authorized to any user, logged in or not thus all users are technically authenticated,
        // so we must check that the principle is linked to an actual user (which would return an int if true)
        if (auth.getPrincipal() == "anonymousUser") {
            boolean loggedIn = false;
            model.addAttribute("loggedInStatus", loggedIn);
            return "home";
        }

        boolean loggedIn = true;
        model.addAttribute("loggedInStatus", loggedIn);
        return "home";
    }
}
