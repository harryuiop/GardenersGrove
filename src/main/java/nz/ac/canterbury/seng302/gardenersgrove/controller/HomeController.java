package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static java.lang.Integer.parseInt;

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
        boolean loggedIn;
        if (auth != null && auth.isAuthenticated()) {
            int currentPrincipalName = parseInt(auth.getName());
            User user = userService.getUserById(currentPrincipalName);
            loggedIn = user != null;
        } else {
            loggedIn = false;
        }
        model.addAttribute("loggedInStatus", loggedIn);
        return "index";
    }
}
