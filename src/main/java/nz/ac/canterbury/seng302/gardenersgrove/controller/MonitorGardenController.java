package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.AdviceRangesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.*;
import nz.ac.canterbury.seng302.gardenersgrove.utility.AdviceRangesDTO;
import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the monitor garden page. For viewing statistics and live
 * updates for a specific garden.
 */
@Controller
public class MonitorGardenController extends NavBar {
    Logger logger = LoggerFactory.getLogger(MonitorGardenController.class);
    private final UserService userService;
    private final GardenService gardenService;
    private final FriendshipService friendshipService;
    private final ArduinoControllerDataService arduinoControllerDataService;
    private final AdviceRangesService adviceRangesService;

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
            ArduinoControllerDataService arduinoControllerDataService,
            AdviceRangesService adviceRangesService) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.friendshipService = friendshipService;
        this.arduinoControllerDataService = arduinoControllerDataService;
        this.adviceRangesService = adviceRangesService;
    }

    /**
     * Set up monitor garden page and display attributes.
     *
     * @param gardenId The id of the garden being viewed
     * @param model    Puts the data into the template to be viewed
     * @return Thymeleaf HTML template of the monitor garden page.
     */
    @GetMapping(MONITOR_GARDEN_URI_STRING)
    public String monitorGarden(@PathVariable long gardenId, Model model)
            throws NoSuchGardenException {
        logger.info("GET {}", monitorGardenUri(gardenId));
        return loadMonitorGardenPage(gardenId, model, new HashMap<>(), Optional.empty());
    }

    /**
     * Add monitor garden information to model.
     *
     * @param gardenId id of garden monitoring
     * @param model    Model to add to
     * @return Load of monitor gardens page
     * @throws NoSuchGardenException If no garden is found.
     */
    private String loadMonitorGardenPage(Long gardenId,
                                         Model model,
                                         Map<String, String> adviceRangesErrors,
                                         Optional<AdviceRangesDTO> adviceRangesDTO
    ) throws NoSuchGardenException {
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
        model.addAttribute("editAdviceUri", EDIT_ADVICE_RANGES_URI_STRING);
        model.addAllAttributes(adviceRangesErrors);
        model.addAttribute("openAdviceRangesModel", !adviceRangesErrors.isEmpty());

        // Add user inputted advice ranges if there are errors, otherwise displayed stored values
        if (adviceRangesDTO.isPresent()) {
            model.addAttribute("adviceRanges", adviceRangesDTO.get());
        } else {
            model.addAttribute("adviceRanges", new AdviceRangesDTO(garden.getAdviceRanges()));
        }
        // Allow values to be reset to the saved ones
        model.addAttribute("savedAdviceRanges", garden.getAdviceRanges());

        // This is where we input if the arduino is connected. Still to be implemented.
        model.addAttribute("connected", false);

        arduinoControllerDataService.addDeviceStatusInformationToModel(model, garden);
        arduinoControllerDataService.addCurrentSensorReadingsToModel(model, garden);
        arduinoControllerDataService.addGraphDataAndAdviceMessagesToModel(model, gardenId, garden);
        arduinoControllerDataService.addArduinoDataThresholds(model);

        return "gardenMonitoring";
    }


    /**
     * Update advice ranges as set by user.
     *
     * @param gardenId        Id of garden updating
     * @param minTemp         Min Temperature
     * @param maxTemp         Max Temperature
     * @param minSoilMoisture Min soil moisture
     * @param maxSoilMoisture Max soil moisture
     * @param minAirPressure  Min air pressure
     * @param maxAirPressure  Max air pressure
     * @param minHumidity     Min humidity
     * @param maxHumidity     Max humidity
     * @param lightLevel      Light level (discrete string that is converted to an enum)
     * @param model           Model to add attributes to
     * @return Load of monitor gardens page
     * @throws NoSuchGardenException If garden does not exist
     */
    @PostMapping(EDIT_ADVICE_RANGES_URI_STRING)
    public String editAdviceForGarden(@PathVariable long gardenId,
                                      @RequestParam double minTemp, @RequestParam double maxTemp,
                                      @RequestParam double minSoilMoisture, @RequestParam double maxSoilMoisture,
                                      @RequestParam double minAirPressure, @RequestParam double maxAirPressure,
                                      @RequestParam double minHumidity, @RequestParam double maxHumidity,
                                      @RequestParam String lightLevel, Model model) throws NoSuchGardenException {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            return loadMonitorGardenPage(gardenId, model, new HashMap<>(), Optional.empty());
        }

        Garden garden = optionalGarden.get();
        AdviceRanges adviceRanges = garden.getAdviceRanges();

        LightLevel lightLevelEnum = LightLevel.fromDisplayName(lightLevel);

        // Validation
        AdviceRangesDTO adviceRangesDTO = new AdviceRangesDTO(minTemp, maxTemp, minSoilMoisture,
                maxSoilMoisture, minAirPressure, maxAirPressure, minHumidity, maxHumidity, lightLevelEnum);

        Map<String, String> errors = AdviceRangesValidator.checkAdviceRanges(adviceRangesDTO);

        if (errors.isEmpty()) {
            adviceRanges.setMinTemperature(minTemp);
            adviceRanges.setMaxTemperature(maxTemp);
            adviceRanges.setMinMoisture(minSoilMoisture);
            adviceRanges.setMaxMoisture(maxSoilMoisture);
            adviceRanges.setMinPressure(minAirPressure);
            adviceRanges.setMaxPressure(maxAirPressure);
            adviceRanges.setMinHumidity(minHumidity);
            adviceRanges.setMaxHumidity(maxHumidity);

            adviceRanges.setLightLevel(LightLevel.fromDisplayName(lightLevel));

            adviceRangesService.saveAdviceRanges(adviceRanges);
            gardenService.saveGarden(garden);
            return "redirect:" + monitorGardenUri(gardenId);
        }
        return loadMonitorGardenPage(gardenId, model, errors, Optional.of(adviceRangesDTO));
    }

}
