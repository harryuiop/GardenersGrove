package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.ui.Model;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;


public class GardensSidebar {
    /**
     * This method passes information to the sidebar thymeleaf fragment.
     * Currently, it just passes the currently logged-in user, and a list of all the user's gardens.
     * @param model The portal which feeds information into the HTML.
     * @param gardenService Garden database access object.
     * @param userService User database access object.
     */
    public void updateGardensSidebar(Model model, GardenService gardenService, UserService userService) {
        model.addAttribute("gardens", gardenService.getAllGardens(userService));
        model.addAttribute("newGardenUri", newGardenUri());
        model.addAttribute("viewFriendsUri", viewFriendsUri());
        model.addAttribute("viewAllGardensUri", viewAllGardensUri());
        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);
        model.addAttribute("user", userService.getAuthenticatedUser());
    }
}
