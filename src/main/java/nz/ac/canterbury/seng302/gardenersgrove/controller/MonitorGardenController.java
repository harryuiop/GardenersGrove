package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the monitor garden page. For viewing statistics and live updates for a specific garden.
 */
@Controller
public class MonitorGardenController {
    private final UserService userService;
    private final GardenService gardenService;
    private final TemperatureService temperatureService;

    /**
     * Spring will automatically call this constructor at runtime to inject the dependencies.
     *
     * @param gardenService A Garden database access object.
     * @param userService   A User database access object.
     */
    @Autowired
    public MonitorGardenController(UserService userService, GardenService gardenService, TemperatureService temperatureService) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.temperatureService = temperatureService;
    }

    /**
     * Set up monitor garden page and display attributes.
     *
     * @param gardenId The id of the garden being viewed
     * @param model    Puts the data into the template to be viewed
     * @return Thymeleaf html template of the monitor garden page.
     */
    @GetMapping(MONITOR_GARDEN_URI_STRING)
    public String monitorGarden(
            @PathVariable long gardenId,
            Model model
    ) throws NoSuchGardenException {
        User currentUser = userService.getAuthenticatedUser();

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()
                || optionalGarden.get().getOwner().getId() != currentUser.getId()
                && !optionalGarden.get().isGardenPublic()) {
            throw new NoSuchGardenException(gardenId);
        }
        boolean owner = optionalGarden.get().getOwner() == currentUser;

        model.addAttribute("garden", optionalGarden.get());
        model.addAttribute("owner", owner);
        model.addAttribute("connected", false); //This is where we input if the arduino is connected. Still to be implemented.
        model.addAttribute("averageMonthTemp", temperatureService.getGraphData(30));
        model.addAttribute("averageWeekTemp", temperatureService.getGraphData(7));

        // TODO: Change to get results every 6 hours
        model.addAttribute("averageDayTemp", temperatureService.getGraphData(1));

        return "gardenMonitoring";
    }

}
