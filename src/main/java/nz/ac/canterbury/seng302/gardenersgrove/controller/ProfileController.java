package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ImageStore;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Integer.parseInt;

/**
 * Controller class handling profile-related requests and actions.
 * Responsible for displaying user profile pages, editing profiles, and uploading profile photos.
 */
@Controller
public class ProfileController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    /**
     * Constructor for ProfileController.
     *
     * @param userService           The UserService responsible for user-related operations.
     * @param authenticationManager The AuthenticationManager for managing user authentication.
     */
    public ProfileController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    /**
     * Get mapping for the profile page
     *
     * @return the profile page with the corresponding information
     */
    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = parseInt(auth.getName());
        User user = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", user);

        return "profile";
    }

    /**
     * Handles GET requests to the "/profile" URL.
     * Displays the user's profile page.
     *
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the profile view template.
     */
    @GetMapping("editProfile")
    public String getEditProfilePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = parseInt(auth.getName());
        User user = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", user);
        boolean noSurname = user.getLastName() == null;
        model.addAttribute("noSurname", noSurname);
        return "editProfile";
    }

    /**
     * Handles GET requests to the "/editProfile" URL.
     * This method is responsible for dealing with grabbing the uploaded profile
     * photo from the user and saving it to where he global variable UPLOAD_DIRECTORY specify
     *
     * @param model The Model object used for adding attributes to the view.
     * @param file  The multipart file object uploaded by the user
     * @return The name of the editProfile view template.
     */
    @PostMapping("/uploadProfileImage")
    public String uploadImage(@RequestParam("image") MultipartFile file,
                              HttpServletRequest request,
                              Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = parseInt(auth.getName());
        User user = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", user);

        ImageValidator imageValidator = new ImageValidator(file);
        if (imageValidator.isValid()) {
            String fileName = ImageStore.storeImage(file);
            user.setProfilePictureFileName(fileName);
            userService.updateUser(user);
        } else {
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        return "redirect:" + request.getHeader("Referer");
    }

    /**
     * Handles POST requests to the "/confirmEditProfile" URL.
     * Confirms the changes made to the user's profile.
     *
     * @return The name of the profile view template.
     */
    @PostMapping("/confirmEditProfile")
    public String confirmEditProfilePost() {
        return "profile";
    }

    /**
     * Handles POST requests to the "/confirmProfileChanges" URL.
     * Adds a new user with the provided information.
     *
     * @param email       The email of the user.
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param password    The password of the user.
     * @param dateOfBirth The user's date of birth.
     * @return The name of the login view template.
     */
    @PostMapping("/confirmProfileChanges")
    public String updateUser(
            @RequestParam int userId,
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "noSurname", required = false) Boolean noSurname,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam String dateOfBirth, Model model
    ) {
        if (noSurname == null) {
            noSurname = true;
        }

        User user = userService.getUserById(userId);
        boolean newEmail = email.equals(user.getEmail());

        boolean dateOfBirthValid = true;
        try {
            if (dateOfBirth != null && !dateOfBirth.isBlank()) {
                LocalDate.parse(dateOfBirth);
            }
        } catch (DateTimeParseException exception) {
            dateOfBirthValid = false;
        }

        ErrorChecker validator = new ErrorChecker();
        Map<String, String> errors = validator.registerUserFormErrors(firstName, lastName, noSurname, email,
                password, passwordConfirm,
                dateOfBirthValid, dateOfBirth, userService, newEmail);

        model.addAttribute("user", user);

        if (!errors.isEmpty()) {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute(noSurname);
            return "editProfile";
        }


        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setDob(dateOfBirth);

        userService.updateUser(user);

        return "profile";
    }

    /**
     * Handles POST requests to the "/logout" URL.
     * Invalidate the users HTTP session and deletes the cookies (done within SecurityConfiguration.java)
     *
     * @return The name of the login view template
     */
    @PostMapping("/logout")
    public String logoutUser() {
        return "login";
    }

    /**
     * Handles requests to the "/profile" URL.
     * Displays the profile page.
     *
     * @return The name of the profile view template.
     */
    @RequestMapping("/profile")
    public String displayImage() {
        return "profile";
    }

    /**
     * Handles requests to the "/editProfile" URL.
     * Displays the editProfile page.
     *
     * @return The name of the editProfile view template.
     */
    @RequestMapping("/editProfile")
    public String displayEditImage() {
        return "editProfile";
    }
}
