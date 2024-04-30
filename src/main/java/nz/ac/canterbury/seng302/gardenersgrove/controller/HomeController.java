package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the root/home page. This controls what each user will see client side
 * depending on authentication status and permissions.
 */
@Controller
public class HomeController extends GardensSidebar {
    private final GardenService gardenService;
    private final UserService userService;

    @Autowired
    public HomeController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to the "/" URL.
     * Displays the login page with different HTML attributes depending on permissions and authentication status.
     *
     * @return a redirect to the login page
     */
    @GetMapping("/")
    public String getLandingPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // The home page is authorized to any user, logged in or not thus all users are technically authenticated,
        // so we must check that the principle is linked to an actual user (which would return an int if true)
        if (auth.getPrincipal() == "anonymousUser") {
            return "landing";
        }
        this.updateGardensSidebar(model, gardenService, userService);
        return "home";
    }
}
