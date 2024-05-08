package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller class handling login-related requests and actions.
 * Responsible for displaying login page, processing login form submission, and initializing sample user data.
 */
@Controller
public class LogInController {

    private static final Logger logger = LoggerFactory.getLogger(LogInController.class);

    private final UserService userService;

    /**
    * Constructor for LogInController.
    *
    * @param userService The UserService responsible for user-related operations.
    */
    @Autowired
    public LogInController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles GET requests to the "/login" URL.
     * Displays the login page. Will display the appropriate errors passed in through the params on
     * authentication failure.
     * @param error The authentication error.
     * @param model The th model.
     * @return The name of the login view template.
     */
    @GetMapping(LOGIN_URI_STRING)
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        logger.info("GET {}", loginUri());

        if (error != null) {
            if (error.equals("Authentication_Failed") || error.equals("Invalid_Password")) {
                model.addAttribute("invalidError", "The email address is unknown, or the password is invalid");
            } else if (error.equals("Invalid_Email")) {
                model.addAttribute("emailError", "Email address must be in the form ‘jane@doe.nz’");
            }
        }

        model.addAttribute("loginUri", loginUri());
        model.addAttribute("homeUri", homeUri());
        model.addAttribute("registerUri", registerUri());
        return "login";
    }
}
