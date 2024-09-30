package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the root/home page. This controls what each user will see client side
 * depending on authentication status and permissions.
 */
@Controller
public class HomeController extends NavBar {
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final GardenService gardenService;
    private final UserService userService;
    private final ArduinoDataPointService arduinoDataPointService;

    /**
     * The HomeController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public HomeController(GardenService gardenService, UserService userService, ArduinoDataPointService arduinoDataPointService) {
        this.gardenService = gardenService;
        this.userService = userService;
        this.arduinoDataPointService = arduinoDataPointService;
    }

    /**
     * Handles GET requests to the home (root) URL.
     * Displays a separate page depending on authentication status.
     *
     * @return the landing page if the user is not authenticated, otherwise the home page
     */
    @GetMapping(HOME_URI_STRING)
    public String getLandingPage(Model model) {
        logger.info("GET {}", homeUri());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Spring security allows requests to the root URI for unauthenticated users.
        // We must check to see if the principle is authenticated to determine which page to display.
        if (auth.getPrincipal() == "anonymousUser" || userService.getAuthenticatedUser() == null) {
            model.addAttribute("loginUri", loginUri());
            model.addAttribute("registerUri", registerUri());
            return "landing";
        }
        this.updateGardensNavBar(model, gardenService, userService);

        addUriToModel(model);

        List<Garden> connectedGardens =  gardenService.getConnectedGardens(userService.getAuthenticatedUser());

        Map<String, ArduinoDataPoint> arduinoDataPointsMap = new HashMap<>();
        for (Garden connectedGarden : connectedGardens) {
            ArduinoDataPoint arduinoDataPoint = arduinoDataPointService.getMostRecentArduinoDataPoint(connectedGarden);
            if (arduinoDataPoint != null) {
                arduinoDataPointsMap.put(connectedGarden.getName(),
                        arduinoDataPointService.getMostRecentArduinoDataPoint(connectedGarden));
            }
        }
        model.addAttribute("connectedGardens", arduinoDataPointsMap);

        return "home";
    }

    /**
     * Add page uri links to model
     *
     * @param model Model to add links to
     */
    private void addUriToModel(Model model) {
        model.addAttribute("browseUri", browsePublicGardensUri());
        model.addAttribute("profileUri", viewProfileUri());
        model.addAttribute("friendsUri", viewFriendsUri());
        model.addAttribute("myGardensUri", viewAllGardensUri());
        model.addAttribute("createGardenUri", newGardenUri());
    }
}
