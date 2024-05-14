package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.ResetPasswordTokenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller class handling login-related requests and actions.
 * Responsible for displaying login page, processing login form submission, and initializing sample user data.
 */
@Controller
public class LogInController {

    private static final Logger logger = LoggerFactory.getLogger(LogInController.class);

    private final EmailSenderService emailSenderService;

    private final UserService userService;

    private final ResetPasswordTokenService resetPasswordTokenService;

    /**
     * Constructor for LogInController.
     *
     * @param emailSenderService the EmailSenderService responsible for email-sending-related operations
     * @param userService        the UserService responsible for user-related operations.
     */
    @Autowired
    public LogInController(EmailSenderService emailSenderService, UserService userService, ResetPasswordTokenService resetPasswordTokenService) {
        this.emailSenderService = emailSenderService;
        this.userService = userService;
        this.resetPasswordTokenService = resetPasswordTokenService;
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
        model.addAttribute("forgotPasswordUri", resetPasswordEmailUri());
        return "login";
    }

    /**
     * Handles GET requests to the reset password URI.
     * Displays a form in which the user is able to reset their password
     *
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the template
     */
    @GetMapping(RESET_PASSWORD_URI_STRING)
    public String resetPassword(Model model,
                                @PathVariable String token,
                                @PathVariable long userId) {
        logger.info("GET {}", resetPasswordUri(token, userId));
        String hashedToken = resetPasswordTokenService.getTokenByUserId(userId);
        logger.info(hashedToken);
        if (hashedToken != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean tokenValid = encoder.matches(token, resetPasswordTokenService.getTokenByUserId(userId));
            if (tokenValid) {
                logger.info("success");
            } else {
                logger.info("fail");
            }
        }

        model.addAttribute("resetPasswordUri", resetPasswordUri(token, userId));
        model.addAttribute("userId", userId);
        return "resetPassword";
    }

    /**
     * Handles POST requests to the reset password URI.
     *
     * @param newPassword       Users new password
     * @param retypeNewPassword Users new password retyped
     * @param model             The Model object used for adding attributes to the view.
     * @return The name of the template.
     */
    @PostMapping(RESET_PASSWORD_URI_STRING)
    public String submitResetPassword(
            @RequestParam String newPassword,
            @RequestParam String retypeNewPassword,
            @PathVariable long userId,
            @PathVariable String token,
            Model model
    ) {
        logger.info("POST {}", resetPasswordUri(token, userId));

        User user = userService.getUserById((int) userId);

        emailSenderService.sendEmail(user, "resetPasswordEmail");

        return "redirect:" + viewProfileUri();
    }

    @GetMapping(RESET_PASSWORD_EMAIL)
    public String returnForgotPasswordForm () {
        logger.info("GET {}", resetPasswordEmailUri());

        return "forgotPasswordForm";
    }
}
