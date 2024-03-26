package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Controller class handling registration-related requests and actions.
 * Responsible for displaying the registration page, checking email duplication,
 * and adding new users to the system.
 */
@Controller
public class RegisterController {

    Logger logger = LoggerFactory.getLogger(RegisterController.class);

    ErrorChecker validator = new ErrorChecker();

    private final DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat printFormat = new SimpleDateFormat("dd/MM/yyyy");

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
        Users user = userService.getUserByEmail(email);
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
    public String showRegisterPage(Model model) {
        logger.info("GET /register");
        model.addAttribute("noSurname", false);
        return "register";
    }

    /**
     * Handles POST requests to the "/register" URL.
     * Adds a new user to the system.
     *
     * @param email       The email of the user.
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param address     The address of the user.
     * @param password    The password of the user.
     * @param dateOfBirth The date of birth of the user.
     * @return Redirects to the login page after successful registration.
     */
    @PostMapping("/register")
    public String addNewUser(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "noSurname", required = false) Boolean noSurname,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "passwordConfirm") String passwordConfirm,
            @RequestParam(name = "dateOfBirth") String dateOfBirth, Model model
    ) {
        if (noSurname == null) {
            noSurname = false;
        }

        boolean dateOfBirthValid = true;
        try {
            if (dateOfBirth != null && !dateOfBirth.isBlank()) {
                readFormat.parse(dateOfBirth);
            }
        } catch (ParseException exception) {
            dateOfBirthValid = false;
        }


        Map<String, String> errors = validator.registerUserFormErrors(firstName, lastName, noSurname, email,
                                                                        password, passwordConfirm,
                                                                        dateOfBirthValid, dateOfBirth, userService);

        if (!errors.isEmpty()) {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            model.addAttribute("noSurname", noSurname);
            return "register";
        }
        userService.addUsers(
                new Users(email, firstName, lastName, address, password, dateOfBirth)
        );
        return "redirect:/login";
    }
}
