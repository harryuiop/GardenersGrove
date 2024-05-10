package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class ViewAllGardensController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewAllGardensController.class);

    private final GardenService gardenService;
    private final UserService userService;

    /**
     * Spring calls the ViewAllGardensController constructor at runtime to inject the necessary dependencies.
     *
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public ViewAllGardensController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to the view all gardens endpoint.
     * @param model The object which passes data to the HTML.
     * @return The view all gardens HTML template.
     */
    @GetMapping(VIEW_ALL_GARDENS_URI_STRING)
    public String viewAllGardens(Model model) {
        logger.info("GET {}", viewAllGardensUri());
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAttribute("gardenList", gardenService.getAllGardens(userService));
        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);
        model.addAttribute("owner", true);
        return "allGardens";
    }

    /**
     * Handles GET requests to the view all gardens endpoint from a friends account.
     *
     * @param model The object which passes data to the HTML.
     * @return The view all gardens HTML template.
     */
    @GetMapping(VIEW_ALL_FRIENDS_GARDENS_URI_STRING)
    public String viewAllFriendsGardens(@PathVariable long friendId, Model model) throws NoSuchGardenException {
        logger.info("GET {}", viewAllFriendsGardensUri(friendId));
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAttribute("gardenList", gardenService.getAllFriendsGardens(friendId, userService));
        model.addAttribute("viewGardenUriString", VIEW_FRIENDS_GARDEN_URI_STRING);
        model.addAttribute("friendId", friendId);
        model.addAttribute("owner", false);
        return "allGardens";
    }
}
