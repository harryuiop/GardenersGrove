package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
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
     * @param address     The address of the user.
     * @param password    The password of the user.
     * @param dateOfBirth The user's date of birth.
     * @return The name of the login view template.
     */
    @PostMapping("/confirmProfileChanges")
    public String addNewUser(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "dateOfBirth") String dateOfBirth
    ) {
        userService.addUsers(
                new User(email, firstName, lastName, address, password, dateOfBirth)
        );
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
