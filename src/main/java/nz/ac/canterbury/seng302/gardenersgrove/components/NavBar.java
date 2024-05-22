package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Class to update and handle the navbar.
 */
public class NavBar {

    /**
     * This method passes information to the navbar thymeleaf fragment.
     * Currently, it just passes the currently logged-in user, and a list of all the user's gardens.
     * @param model The portal which feeds information into the HTML.
     * @param gardenService Garden database access object.
     * @param userService User database access object.
     */
    public void updateGardensNavBar(Model model, GardenService gardenService, UserService userService) {
        model.addAttribute("gardens", getTopNGardens(5, gardenService, userService));
        model.addAttribute("newGardenUri", newGardenUri());
        model.addAttribute("viewAllGardensUri", viewAllGardensUri());
        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = Integer.parseInt(auth.getName());
        User user = userService.getUserById(currentPrincipalName);

        model.addAttribute("user", user);
    }

    private List<Garden> getTopNGardens(int amount, GardenService gardenService, UserService userService) {
        List<Garden> allGardens = gardenService.getAllGardens(userService);
        return allGardens.subList(0, Math.min(amount, allGardens.size()));
    }
}
