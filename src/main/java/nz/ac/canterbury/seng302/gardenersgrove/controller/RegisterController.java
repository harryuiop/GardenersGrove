package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Controller class handling registration-related requests and actions.
 * Responsible for displaying the registration page, checking email duplication,
 * and adding new users to the system.
 */
@Controller
public class RegisterController {
    final boolean oldEmail = false;

    Logger logger = LoggerFactory.getLogger(RegisterController.class);

    UserService userService;

    EmailSenderService emailSenderService;

    @Autowired
    public RegisterController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
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
        model.addAttribute("firstNameError", "");
        model.addAttribute("lastNameError", "");
        model.addAttribute("emailError", "");
        model.addAttribute("passwordError", "");
        model.addAttribute("passwordConfirmError", "");
        model.addAttribute("dateOfBirthError", "");

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
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "noSurname", required = false) Boolean noSurname,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "passwordConfirm") String passwordConfirm,
            @RequestParam(name = "dateOfBirth", required = false) String dateOfBirth, Model model
    ) {
        if (noSurname == null) {
            noSurname = false;
        }

        boolean dateOfBirthValid = true;
        try {
            if (dateOfBirth != null && !dateOfBirth.isBlank()) {
                LocalDate.parse(dateOfBirth);
            }
        } catch (DateTimeParseException exception) {
            dateOfBirthValid = false;
        }


        Map<String, String> errors = ErrorChecker.registerUserFormErrors(firstName, lastName, noSurname,
                                                                        email, oldEmail, userService,
                                                                        password, passwordConfirm,
                                                                        dateOfBirthValid, dateOfBirth);

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
        User newUser = new User(email, firstName, lastName, password, dateOfBirth);
        userService.addUsers(newUser);

        // send verification email
        emailSenderService.sendRegistrationEmail(newUser, "registrationEmail");

        // This needs to go to email confirmation
        return "redirect:/login";
    }
}
