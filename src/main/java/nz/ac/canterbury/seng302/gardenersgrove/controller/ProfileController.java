package nz.ac.canterbury.seng302.gardenersgrove.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.slf4j.Logger;

import static java.lang.Integer.parseInt;

/**
 * Controller class handling profile-related requests and actions.
 * Responsible for displaying user profile pages, editing profiles, and uploading profile photos.
 */
@Controller
public class ProfileController {

    private static final String UPLOAD_DIRECTORY = "/csse/users/hel46/team-l/src/main/resources/static/css/images";
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    /**
     * Constructor for ProfileController.
     *
     * @param userService The UserService responsible for user-related operations.
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
        Users u = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", u);

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
        Users u = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", u);
        return "editProfile";
    }

    /**
     * Handles GET requests to the "/editProfile" URL.
     * This method is responsible for dealing with grabbing the uploaded profile
     * photo from the user and saving it to where he global variable UPLOAD_DIRECTORY specify
     *
     * @param model The Model object used for adding attributes to the view.
     * @param file The multipart file object uploaded by the user
     * @return The name of the editProfile view template.
     */
    @PostMapping("/editProfile")
    public String uploadImage(Model model,
                              @RequestParam("image") MultipartFile file
    ) throws IOException {

        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        return "editProfile";
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
     * @param email The email of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param address The address of the user.
     * @param password The password of the user.
     * @param dateOfBirth The date of birth of the user.
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
                new Users(email, firstName, lastName, address, password, dateOfBirth)
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
