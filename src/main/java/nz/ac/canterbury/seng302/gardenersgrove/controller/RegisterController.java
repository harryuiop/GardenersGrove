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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller class handling registration-related requests and actions.
 * Responsible for displaying the registration page, checking email duplication,
 * and adding new users to the system.
 */
@Controller
public class RegisterController {
    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final UserService userService;

    private final EmailSenderService emailSenderService;
    private final ErrorChecker errorChecker;

    /**
     * The RegisterController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param userService        The User database access object.
     * @param emailSenderService The email sender service.
     */
    @Autowired
    public RegisterController(UserService userService, EmailSenderService emailSenderService, ErrorChecker errorChecker) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.errorChecker = errorChecker;
    }

    /**
     * Handles GET requests to the register URI.
     * Displays the registration page.
     *
     * @return The name of the register view template.
     */
    @GetMapping(REGISTER_URI_STRING)
    public String showRegisterPage(Model model) {
        logger.info("GET {}", registerUri());
        model.addAttribute("noSurname", false);
        model.addAttribute("firstNameError", "");
        model.addAttribute("lastNameError", "");
        model.addAttribute("emailError", "");
        model.addAttribute("passwordError", "");
        model.addAttribute("passwordConfirmError", "");
        model.addAttribute("dateOfBirthError", "");
        model.addAttribute("homeUri", homeUri());
        model.addAttribute("registerUri", registerUri());
        model.addAttribute("loginUri", loginUri());
        return "register";
    }

    /**
     * Handles POST requests to the register URI.
     * Adds a new user to the system.
     *
     * @param email       The email of the user.
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param password    The password of the user.
     * @param dateOfBirth The date of birth of the user.
     * @return Redirects to the login page after successful registration.
     */
    @PostMapping(REGISTER_URI_STRING)
    public String addNewUser(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean noSurname,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam(required = false) String dateOfBirth,
            Model model
    ) {
        logger.info("POST {}", registerUri());

        if (noSurname == null) {
            noSurname = false;
        }

        boolean dateOfBirthValid = true;
        email = email.toLowerCase();
        try {
            if (dateOfBirth != null && !dateOfBirth.isBlank()) {
                LocalDate.parse(dateOfBirth);
            }
        } catch (DateTimeParseException exception) {
            dateOfBirthValid = false;
        }

        Map<String, String> errors = errorChecker.registerUserFormErrors(
                firstName,
                lastName,
                noSurname,
                email,
                false,
                userService,
                password,
                passwordConfirm,
                dateOfBirthValid,
                dateOfBirth
        );

        if (!errors.isEmpty()) {
            model.addAllAttributes(errors);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("email", email);
            model.addAttribute("noSurname", noSurname);
            model.addAttribute("homeUri", homeUri());
            model.addAttribute("registerUri", registerUri());
            model.addAttribute("loginUri", loginUri());
            return "register";
        }
        User newUser = new User(email, firstName, lastName, password, dateOfBirth);
        userService.addUsers(newUser);

        // send verification email
        emailSenderService.sendEmail(newUser, "registrationEmail");

        return "redirect:" + verifyEmailUri();
    }

    @GetMapping(VERIFY_EMAIL_URI_STRING)
    public String showVerifyPage(Model model) {
        logger.info("GET {}", verifyEmailUri());

        model.addAttribute("verifyEmailUri", verifyEmailUri());
        return "tokenValidation";
    }

    @PostMapping(VERIFY_EMAIL_URI_STRING)
    public String verifyUserAccount(
            @RequestParam String tokenValue,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        logger.info("POST {}", verifyEmailUri());

        User user = userService.getUserByToken(tokenValue);
        if (user == null) {
            model.addAttribute("verifyEmailUri", verifyEmailUri());
            model.addAttribute("tokenInvalid", "Signup code invalid");
            return "tokenValidation";
        }

        user.setConfirmation(true);
        userService.updateUser(user);

        redirectAttributes.addFlashAttribute("accountActiveMessage", "Your account has been activated, please log in");
        return "redirect:" + loginUri();
    }
}
