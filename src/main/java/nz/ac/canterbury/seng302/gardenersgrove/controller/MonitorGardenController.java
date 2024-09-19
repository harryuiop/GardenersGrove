package nz.ac.canterbury.seng302.gardenersgrove.controller;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.MONITOR_GARDEN_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator.isHumidityConnected;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator.isLightConnected;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator.isMoistConnected;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator.isPressureConnected;
import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator.isTempConnected;
import static nz.ac.canterbury.seng302.gardenersgrove.utility.TimeConverter.minutestoTimeString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.AdviceRangesService;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;

/**
 * Controller for the monitor garden page. For viewing statistics and live
 * updates for a specific garden.
 */
@Controller
public class MonitorGardenController extends NavBar {
    private final UserService userService;
    private final GardenService gardenService;
    private final ArduinoDataPointService arduinoDataPointService;
    private final AdviceRangesService adviceRangesService;

    /**
     * Spring will automatically call this constructor at runtime to inject the
     * dependencies.
     *
     * @param gardenService           A Garden database access object.
     * @param userService             A User database access object.
     * @param arduinoDataPointService A arduinoDataPointService database access
     *                                object.
     * @param adviceRangesService     A AdviceRangesService database access object.
     */
    @Autowired
    public MonitorGardenController(
            UserService userService,
            GardenService gardenService,
            ArduinoDataPointService arduinoDataPointService,
            AdviceRangesService adviceRangesService) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.arduinoDataPointService = arduinoDataPointService;
        this.adviceRangesService = adviceRangesService;
    }

    /**
     * Set up monitor garden page and display attributes.
     *
     * @param gardenId The id of the garden being viewed
     * @param model    Puts the data into the template to be viewed
     *
     * @return Thymeleaf html template of the monitor garden page.
     */
    @GetMapping(MONITOR_GARDEN_URI_STRING)
    public String monitorGarden(@PathVariable long gardenId, Model model)
            throws NoSuchGardenException {

        this.updateGardensNavBar(model, gardenService, userService);

        User currentUser = userService.getAuthenticatedUser();
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);

        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        boolean notOwner = garden.getOwner().getId() != currentUser.getId();
        boolean privateGarden = !garden.isGardenPublic();
        if (notOwner && privateGarden) {
            throw new NoSuchGardenException(gardenId);
        }

        model.addAttribute("garden", garden);
        model.addAttribute("owner", garden.getOwner() == currentUser);
        model.addAttribute("adviceRanges", garden.getAdviceRanges());

        // This is where we input if the arduino is connected. Still to be implemented.
        model.addAttribute("connected", false);

        addDeviceStatusInformationToModel(model, garden);
        addCurrentSensorReadingsToModel(model, garden);
        addGraphDataToModel(model, gardenId);
        addArduinoDataThresholds(model);


        return "gardenMonitoring";
    }

    /**
     * Helper method to add current sensor readings to html model.
     */
    private void addCurrentSensorReadingsToModel(Model model, Garden garden) {
        String tempReading = "-";
        String moistReading = "-";
        String lightReading = "-";
        String pressureReading = "-";
        String humidReading = "-";
        boolean tempSensorConnected;
        boolean moistSensorConnected;
        boolean lightSensorConnected;
        boolean pressureSensorConnected;
        boolean humidSensorConnected;

        ArduinoDataPoint arduinoDataPoint = arduinoDataPointService.getMostRecentArduinoDataPoint(garden);
        if (arduinoDataPoint != null) {
            if (arduinoDataPoint.getTempCelsius() != null) {
                tempSensorConnected = isTempConnected(arduinoDataPoint.getTempCelsius());
                tempReading = (tempSensorConnected) ? String.format("%.1f", arduinoDataPoint.getTempCelsius()) : "-";
            }
            if (arduinoDataPoint.getMoisturePercent() != null) {
                moistSensorConnected = isMoistConnected(arduinoDataPoint.getMoisturePercent());
                moistReading = (moistSensorConnected) ? String.format("%.0f", arduinoDataPoint.getMoisturePercent())
                        : "-";
            }
            if (arduinoDataPoint.getLightPercent() != null) {
                lightSensorConnected = isLightConnected(arduinoDataPoint.getLightPercent());
                lightReading = (lightSensorConnected) ? String.format("%.0f", arduinoDataPoint.getLightPercent()) : "-";
            }
            if (arduinoDataPoint.getAtmosphereAtm() != null) {
                pressureSensorConnected = isPressureConnected(arduinoDataPoint.getAtmosphereAtm());
                pressureReading = (pressureSensorConnected) ? String.format("%.3f", arduinoDataPoint.getAtmosphereAtm())
                        : "-";
            }
            if (arduinoDataPoint.getHumidityPercent() != null) {
                humidSensorConnected = isHumidityConnected(arduinoDataPoint.getHumidityPercent());
                humidReading = (humidSensorConnected) ? String.format("%.0f", arduinoDataPoint.getHumidityPercent())
                        : "-";
            }

        }
        model.addAttribute("tempReading", tempReading);
        model.addAttribute("moistReading", moistReading);
        model.addAttribute("lightReading", lightReading);
        model.addAttribute("pressureReading", pressureReading);
        model.addAttribute("humidReading", humidReading);
    }

    /**
     * Helper method to add graph data to html model.
     */
    private void addGraphDataToModel(Model model, Long gardenId) {
        FormattedGraphData dayData = arduinoDataPointService.getDayGraphData(gardenId, LocalDateTime.now());
        FormattedGraphData weekData = arduinoDataPointService.getWeekGraphData(gardenId, LocalDateTime.now());
        FormattedGraphData monthData = arduinoDataPointService.getMonthGraphData(gardenId, LocalDateTime.now());

        model.addAttribute("graphDay", dayData);
        model.addAttribute("graphWeek", weekData);
        model.addAttribute("graphMonth", monthData);
    }

    /**
     * Add device status, and time since last reading to html model.
     */
    private void addDeviceStatusInformationToModel(Model model, Garden garden) {
        String deviceStatus;
        ArduinoDataPoint lastDataPoint;
        long minutesSinceLastReading;
        String timeSinceLastReading = "";

        if (garden.getArduinoId() == null) {
            deviceStatus = "NOT_LINKED";
        } else {
            lastDataPoint = arduinoDataPointService.getMostRecentArduinoDataPoint(garden);
            if (lastDataPoint == null) {
                deviceStatus = "NO_DATA";
            } else {
                minutesSinceLastReading = Duration.between(LocalDateTime.now(), lastDataPoint.getTime()).abs()
                        .toMinutes();
                timeSinceLastReading = minutestoTimeString(minutesSinceLastReading);
                if (minutesSinceLastReading <= 5) {
                    deviceStatus = "UP_TO_DATE";
                } else {
                    deviceStatus = "OUT_OF_DATE";
                }
            }
        }
        model.addAttribute("deviceStatus", deviceStatus);
        model.addAttribute("timeSinceLastReading", timeSinceLastReading);
    }

    /**
     * Add all of the data thresholds to the model to be used for the valid ranges
     * on the advice range inputs.
     */
    private void addArduinoDataThresholds(Model model) {
        model.addAttribute("MIN_VALID_TEMP", ArduinoDataValidator.MIN_TEMPERATURE);
        model.addAttribute("MAX_VALID_TEMP", ArduinoDataValidator.MAX_TEMPERATURE);
        model.addAttribute("MIN_VALID_MOISTURE", ArduinoDataValidator.MIN_MOISTURE);
        model.addAttribute("MAX_VALID_MOISTURE", ArduinoDataValidator.MAX_MOISTURE);
        model.addAttribute("MIN_VALID_ATMOSPHERE", ArduinoDataValidator.MIN_ATMOSPHERE);
        model.addAttribute("MAX_VALID_ATMOSPHERE", ArduinoDataValidator.MAX_ATMOSPHERE);
        model.addAttribute("MIN_VALID_HUMIDITY", ArduinoDataValidator.MIN_HUMIDITY);
        model.addAttribute("MAX_VALID_HUMIDITY", ArduinoDataValidator.MAX_HUMIDITY);
        model.addAttribute("LIGHT_LEVELS", Arrays.asList(LightLevel.values()));
        System.out.println(Arrays.asList(LightLevel.values()));
    }

}
