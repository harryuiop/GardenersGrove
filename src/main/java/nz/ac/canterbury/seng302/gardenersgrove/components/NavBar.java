package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
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
     *
     * @param model         The portal which feeds information into the HTML.
     * @param gardenService Garden database access object.
     * @param userService   User database access object.
     */
    public void updateGardensNavBar(Model model, GardenService gardenService, UserService userService) {
        model.addAttribute("gardens", getTop5Gardens(gardenService));
        model.addAttribute("newGardenUri", newGardenUri());
        model.addAttribute("viewAllGardensUri", viewAllGardensUri());
        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);
        model.addAttribute("user", userService.getAuthenticatedUser());
        model.addAttribute("browsePublicGardensUri", browsePublicGardensUri());
    }

    /**
     * Returns the top 5 gardens. If the user has less than 5 it will return all the gardens.
     *
     * @param gardenService Garden datatbase ascess object
     * @return              A list of the users first, at most, 5 gardens.
     */
    private List<Garden> getTop5Gardens(GardenService gardenService) {
        List<Garden> allGardens = gardenService.getAllGardens();
        return allGardens.subList(0, Math.min(5, allGardens.size()));
    }
}
