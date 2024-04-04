package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(LogInController.class);



    /**
     * Constructor for ProfileController.
     *
     * @param userService           The UserService responsible for user-related operations.
     * @param authenticationManager The AuthenticationManager for managing user authentication.
     */
    @Autowired
    public HomeController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

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
