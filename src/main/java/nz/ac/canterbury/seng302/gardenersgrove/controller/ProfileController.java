package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ImageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller class handling profile-related requests and actions.
 * Responsible for displaying user profile pages, editing profiles, and uploading profile photos.
 */
@Controller
public class ProfileController extends NavBar {
    Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final UserService userService;
    private final GardenService gardenService;
    private final EmailSenderService emailSenderService;
    private final ErrorChecker errorChecker;

    /**
     * Constructor for ProfileController.
     *
     * @param userService        the UserService responsible for user-related operations.
     * @param gardenService      the GardenService responsible for garden-related operations
     * @param emailSenderService the EmailSenderService responsible for email-sending-related operations
     */
    @Autowired
    public ProfileController(UserService userService, GardenService gardenService, EmailSenderService emailSenderService, ErrorChecker errorChecker) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.emailSenderService = emailSenderService;
        this.errorChecker = errorChecker;
    }

    /**
     * Handles GET requests to the profile URI.
     *
     * @return the profile page with the corresponding information
     */
    @GetMapping(VIEW_PROFILE_URI_STRING)
    public String getProfilePage(Model model) {
        logger.info("GET {}", viewProfileUri());

        model.addAttribute("editProfileUri", editProfileUri());
        model.addAttribute("uploadProfileImageUri", uploadProfileImageUri());
        this.updateGardensNavBar(model, gardenService, userService);
        return "profile";
    }

    /**
     * Handles GET requests to the edit profile URI.
     * Displays the user's profile page.
     *
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the profile view template.
     */
    @GetMapping(EDIT_PROFILE_URI_STRING)
    public String getEditProfilePage(Model model) {
        logger.info("GET {}", editProfileUri());

        User user = userService.getAuthenticatedUser();
        model.addAttribute("noSurname", user.getLastName().isBlank());
        model.addAttribute("editPasswordUri", editPasswordUri());
        model.addAttribute("uploadProfileImageUri", uploadProfileImageUri());
        model.addAttribute("editProfileUri", editProfileUri());
        model.addAttribute("profileUri", viewProfileUri());
        this.updateGardensNavBar(model, gardenService, userService);
        return "editProfile";
    }

    /**
     * Handles GET requests to the edit password URI.
     * Displays a form in which the user is able to change their password
     *
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the editPassword view template
     */
    @GetMapping(EDIT_PASSWORD_URI_STRING)
    public String editPassword(Model model) {
        logger.info("GET {}", editPasswordUri());

        model.addAttribute("editPasswordUri", editPasswordUri());
        model.addAttribute("editProfileUri", editProfileUri());
        this.updateGardensNavBar(model, gardenService, userService);
        return "editPassword";
    }

    /**
     * Handles POST requests to the upload profile image URI.
     * This method is responsible for dealing with grabbing the uploaded profile
     * photo from the user and saving it to the server.
     *
     * @param image              The multipart file object uploaded by the user
     * @param referer            The URL of the page the user was on before uploading the image.
     * @param redirectAttributes The RedirectAttributes object used for adding attributes to the view.
     * @return A redirect to the referer URL.
     */
    @PostMapping(UPLOAD_PROFILE_IMAGE_URI_STRING)
    public String uploadImage(
            @RequestParam MultipartFile image,
            @RequestHeader String referer,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        logger.info("POST {}", uploadProfileImageUri());

        User user = userService.getAuthenticatedUser();
        redirectAttributes.addFlashAttribute("user", user);

        ImageValidator imageValidator = new ImageValidator(image);
        if (imageValidator.isValid()) {
            String fileName = ImageStore.storeImage(image);
            user.setProfilePictureFileName(fileName);
            userService.updateUser(user);
        } else {
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue());
            }
        }
        return "redirect:" + referer;
    }

    /**
     * Handles POST requests to the edit profile URI.
     * Adds a new user with the provided information.
     *
     * @param email       The email of the user.
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param dateOfBirth The user's date of birth.
     * @return The name of the login view template.
     */
    @PostMapping(EDIT_PROFILE_URI_STRING)
    public String updateUser(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean noSurname,
            @RequestParam String dateOfBirth,
            Model model
    ) {
        logger.info("POST {}", editProfileUri());

        if (noSurname == null) {
            noSurname = false;
        }

        User prevUpdateUser = userService.getAuthenticatedUser();

        email = email.toLowerCase();
        boolean newEmail = email.equals(prevUpdateUser.getEmail().toLowerCase());

        boolean dateOfBirthValid = true;
        try {
            if (dateOfBirth != null && !dateOfBirth.isBlank()) {
                LocalDate.parse(dateOfBirth);
            }
        } catch (DateTimeParseException exception) {
            dateOfBirthValid = false;
        }

        Map<String, String> errors = errorChecker.profileFormErrors(
                firstName, lastName, noSurname,
                email, newEmail, userService,
                dateOfBirthValid, dateOfBirth
        );

        if (!errors.isEmpty()) {
            model.addAllAttributes(errors);
            model.addAttribute("editPasswordUri", editPasswordUri());
            model.addAttribute("uploadProfileImageUri", uploadProfileImageUri());
            model.addAttribute("editProfileUri", editProfileUri());
            model.addAttribute("profileUri", viewProfileUri());
            model.addAttribute("user", prevUpdateUser);
            model.addAttribute("noSurname", noSurname);
            return "editProfile";
        }

        prevUpdateUser.setFirstName(firstName);
        prevUpdateUser.setLastName(lastName);
        prevUpdateUser.setEmail(email);
        prevUpdateUser.setDob(dateOfBirth);

        userService.updateUser(prevUpdateUser);

        return "redirect:" + viewProfileUri();
    }

    /**
     * Handles POST requests to the edit password URI.
     *
     * @param oldPassword       Users current password
     * @param newPassword       Users new password
     * @param retypeNewPassword Users new password retyped
     * @param model             The Model object used for adding attributes to the view.
     * @return The name of the editProfile view template.
     */
    @PostMapping(EDIT_PASSWORD_URI_STRING)
    public String confirmEditPassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String retypeNewPassword,
            Model model
    ) {
        logger.info("POST {}", editPasswordUri());

        User user = userService.getAuthenticatedUser();
        model.addAttribute(user);

        Map<String, String> errors = errorChecker.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user, true);

        if (!errors.isEmpty()) {
            logger.info(errors.toString());
            model.addAllAttributes(errors);
            model.addAttribute("oldPassword", oldPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("retypeNewPassword", retypeNewPassword);
            return "editPassword";
        }

        user.setPassword(userService.hashUserPassword(newPassword));
        userService.updateUser(user);

        // send verification email
        emailSenderService.sendEmail(user, "passwordUpdatedEmail");

        return "redirect:" + viewProfileUri();
    }

    /**
     * Handles POST requests to the logout URI.
     * Invalidate the users HTTP session and deletes the cookies (done within SecurityConfiguration.java)
     *
     * @return The name of the login view template
     */
    @PostMapping(LOGOUT_URI_STRING)
    public String logoutUser() {
        logger.info("POST {}", logoutUri());
        return "login";
    }
}
