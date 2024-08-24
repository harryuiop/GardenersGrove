package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.MONITOR_GARDEN_URI_STRING;

/**
 * Controller for the monitor garden page. For viewing statistics and live updates for a specific garden.
 */
@Controller
public class MonitorGardenController extends NavBar {
    private final UserService userService;
    private final GardenService gardenService;
    private final ArduinoDataPointService dataPointService;

    /**
     * Spring will automatically call this constructor at runtime to inject the dependencies.
     *
     * @param gardenService A Garden database access object.
     * @param userService   A User database access object.
     */
    @Autowired
    public MonitorGardenController(
            UserService userService,
            GardenService gardenService,
            ArduinoDataPointService dataPointService
    ) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.dataPointService = dataPointService;
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
        this.updateGardensNavBar(model, gardenService, userService);

        User currentUser = userService.getAuthenticatedUser();

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()
                || optionalGarden.get().getOwner().getId() != currentUser.getId()
                && !optionalGarden.get().isGardenPublic()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        String deviceStatus;
        ArduinoDataPoint lastDataPoint;
        Long timeSinceLastReading = null;

        if (garden.getArduinoId() == null) {
            deviceStatus = "NOT_LINKED";
        } else {
            lastDataPoint = dataPointService.lastPointFromGarden(garden);
            if (lastDataPoint == null) {
                deviceStatus = "NO_DATA";
            } else {
                timeSinceLastReading = Duration.between(LocalDateTime.now(), lastDataPoint.getTime()).abs().toMinutes();
                if (timeSinceLastReading <= 5) {
                    deviceStatus = "UP_TO_DATE";
                } else {
                    deviceStatus = "OUT_OF_DATE";
                }
            }
        }

        model.addAttribute("garden", garden);
        model.addAttribute("owner", garden.getOwner() == currentUser);
        model.addAttribute("deviceStatus", deviceStatus);
        model.addAttribute("timeSinceLastReading", timeSinceLastReading);
        return "gardenMonitoring";
    }

}
