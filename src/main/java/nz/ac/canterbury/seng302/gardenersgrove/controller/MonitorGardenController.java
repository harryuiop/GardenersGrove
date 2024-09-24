package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the monitor garden page. For viewing statistics and live
 * updates for a specific garden.
 */
@Controller
public class MonitorGardenController extends NavBar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);
    private final UserService userService;
    private final GardenService gardenService;
    private final FriendshipService friendshipService;
    private final ArduinoControllerDataService arduinoControllerDataService;

    /**
     * Spring will automatically call this constructor at runtime to inject the
     * dependencies.
     *
     * @param gardenService                  A Garden database access object.
     * @param userService                    A User database access object.
     * @param arduinoControllerDataService   A ArduinoControllerDataService object.
     * @param friendshipService              A friendshipService object.
     */
    @Autowired
    public MonitorGardenController(
            UserService userService,
            GardenService gardenService,
            FriendshipService friendshipService,
            ArduinoControllerDataService arduinoControllerDataService) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.friendshipService = friendshipService;
        this.arduinoControllerDataService = arduinoControllerDataService;
    }

    /**
     * Set up monitor garden page and display attributes.
     *
     * @param gardenId The id of the garden being viewed
     * @param model    Puts the data into the template to be viewed
     *
     * @return Thymeleaf HTML template of the monitor garden page.
     */
    @GetMapping(MONITOR_GARDEN_URI_STRING)
    public String monitorGarden(@PathVariable long gardenId, Model model)
            throws NoSuchGardenException {
        logger.info("GET {}", monitorGardenUri(gardenId));

        this.updateGardensNavBar(model, gardenService, userService);

        User currentUser = userService.getAuthenticatedUser();
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);

        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        boolean notOwner = garden.getOwner().getId() != currentUser.getId();
        boolean privateGarden = !garden.isGardenPublic();
        boolean notFriends = !friendshipService.areFriends(garden.getOwner(), currentUser);
        if (notOwner && privateGarden && notFriends) {
            throw new NoSuchGardenException(gardenId);
        }

        model.addAttribute("garden", garden);
        model.addAttribute("owner", garden.getOwner() == currentUser);
        model.addAttribute("gardenList", gardenService.getAllGardens());
        model.addAttribute("adviceRanges", garden.getAdviceRanges());

        // This is where we input if the arduino is connected. Still to be implemented.
        model.addAttribute("connected", false);

        arduinoControllerDataService.addDeviceStatusInformationToModel(model, garden);
        arduinoControllerDataService.addCurrentSensorReadingsToModel(model, garden);
        arduinoControllerDataService.addGraphDataToModel(model, gardenId);
        arduinoControllerDataService.addArduinoDataThresholds(model);
        arduinoControllerDataService.addAdviceMessagesToModel(model);

        return "gardenMonitoring";
    }
}
