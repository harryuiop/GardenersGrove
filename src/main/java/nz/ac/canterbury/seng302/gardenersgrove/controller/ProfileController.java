package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ImageStore;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Controller class handling profile-related requests and actions.
 * Responsible for displaying user profile pages, editing profiles, and uploading profile photos.
 */
@Controller
public class ProfileController extends GardensSidebar {

    private final UserService userService;
    private final GardenService gardenService;
    private final ErrorChecker validator = new ErrorChecker();

    /**
     * Constructor for ProfileController.
     *
     * @param userService           The UserService responsible for user-related operations.
     */
    public ProfileController(UserService userService, GardenService gardenService) {
        this.userService = userService;
        this.gardenService = gardenService;
    }

    /**
     * Handles GET requests to the "/profile" URL.
     *
     * @return the profile page with the corresponding information
     */
    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        this.updateGardensSidebar(model, gardenService, userService);
        User user = userService.getAuthenticatedUser(userService);
        model.addAttribute("user", user);

        return "profile";
    }

    /**
     * Handles GET requests to the "/editProfile" URL.
     * Displays the user's profile page.
     *
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the profile view template.
     */
    @GetMapping("/editProfile")
    public String getEditProfilePage(Model model) {
        User user = userService.getAuthenticatedUser(userService);
        model.addAttribute("user", user);
        return "editProfile";
    }

    /**
     *  Handles GET requests to the "/editPassword" URL.
     *  Displays a form in which the user is able to change their password
     *
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the editPassword view template
     */
    @GetMapping("/editPassword")
    public String editPassword(Model model) {
        User user = userService.getAuthenticatedUser(userService);
        model.addAttribute("user", user);

        return "editPassword"; }

    /**
     * Handles POST requests to the "/uploadProfileImage" URL.
     * This method is responsible for dealing with grabbing the uploaded profile
     * photo from the user and saving it to the server.
     *
     * @param model The Model object used for adding attributes to the view.
     * @param file  The multipart file object uploaded by the user
     * @param request The HTTP request object.
     * @param model The Model object used for adding attributes to the view.
     * @return The name of the editProfile view template.
     */
    @PostMapping("/uploadProfileImage")
    public String uploadImage(
                    @RequestParam("image") MultipartFile file,
                    HttpServletRequest request,
                    Model model
    ) throws IOException {
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
     * @param dateOfBirth The user's date of birth.
     * @return The name of the login view template.
     */
    @PostMapping("/confirmProfileChanges")
    public String addNewUser(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName,
            @RequestParam(name = "dateOfBirth") String dateOfBirth,
            Model model
    ) {
        User prevUpdateUser = userService.getAuthenticatedUser(userService);
        model.addAttribute(prevUpdateUser);

        boolean checked = true;
        userService.addUsers(
                new User(email, firstName, lastName, prevUpdateUser.getPassword(), dateOfBirth), checked
        );
        return "profile";
    }

    @PostMapping("/confirmEditPassword")
    public String confirmEditPassword (
            @RequestParam(name = "oldPassword") String oldPassword,
            @RequestParam(name = "newPassword") String newPassword,
            @RequestParam(name = "retypeNewPassword") String retypeNewPassword,
            Model model
    ) {
        User user = userService.getAuthenticatedUser(userService);
        model.addAttribute(user);

        Map<String, String> errors = validator.editPasswordFormErrors(oldPassword, newPassword, retypeNewPassword, user);

        if (!errors.isEmpty()) {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("oldPassword", oldPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("retypeNewPassword", retypeNewPassword);
            return "editPassword";
        }

        user.setPassword(newPassword);
        userService.updateUser(user);
        return "editProfile";
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
}
