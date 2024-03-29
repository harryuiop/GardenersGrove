package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class handling registration-related requests and actions.
 * Responsible for displaying the registration page, checking email duplication,
 * and adding new users to the system.
 */
@Controller
public class RegisterController {

    Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    UserService userService;

    /**
     * Handles POST requests to the "/check-email-duplication" URL.
     * Checks if the provided email already exists in the system.
     *
     * @param email The email to be checked for duplication.
     * @return ResponseEntity containing "true" if the email exists, "false" otherwise.
     */
    @PostMapping("/check-email-duplication")
    @ResponseBody
    public ResponseEntity<String> emailIsExist(@RequestBody String email) {
        logger.info(String.format("POST /check-email-duplication For %s%n", email));
        User user = userService.getUserByEmail(email);
        String valid = (user != null) ? "true" : "false";

        return ResponseEntity.ok(valid);
    }

    /**
     * Handles GET requests to the "/register" URL.
     * Displays the registration page.
     *
     * @return The name of the register view template.
     */
    @GetMapping("/register")
    public String showRegisterPage() {
        logger.info("GET /register");
        return "register";
    }

    /**
     * Handles POST requests to the "/register" URL.
     * Adds a new user to the system.
     *
     * @param email       The email of the user.
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param password    The password of the user.
     * @param dateOfBirth The date of birth of the user.
     * @return Redirects to the login page after successful registration.
     */
    @PostMapping("/register")
    public String addNewUser(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "dateOfBirth") String dateOfBirth
    ) {
        userService.addUsers(
                        new User(email, firstName, lastName, password, dateOfBirth)
        );
        return "redirect:/login";
    }
}
