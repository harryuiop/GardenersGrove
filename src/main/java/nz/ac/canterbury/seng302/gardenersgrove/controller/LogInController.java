package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;

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
    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, Model model
    ) {
        if (error != null && error.equals("Invalid")) {
            model.addAttribute("invalidError", "The email address is unknown, or the password is invalid");
        }
        if (error != null && error.equals("Bad_Credentials")) {
            model.addAttribute("emailError", "Email address must be in the form ‘jane@doe.nz’");
        }
        boolean validated = false;
        userService.addUsers(new User
                ("user@gmail.com", "Default", "User", "Password1!", "2000-01-01"));

        return "login";
    }

    /**
    * Handles POST requests to the "/login" URL.
    * Processes the login form submission.
    *
    * @param email The email parameter obtained from the login form.
    * @param password The password parameter obtained from the login form.
    * @param model The Model object used for adding attributes to the view.
    * @return The name of the login view template.
    */
    @PostMapping("/login")
    public String processLoginForm(
            @RequestParam(name = "username") String email,
            @RequestParam(name = "password") String password,
            Model model
    ) {
        Map<String, String> errors = ErrorChecker.loginFormErrors(email, password, userService);

        if (!errors.isEmpty()) {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            return "login";
        } else {
            User user = userService.getUserByEmailAndPassword(email, password);
            model.addAttribute("user", user);

        return "/login";
        }
    }
}
