package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.COMPARE_GARDENS_URI_STRING;


/**
 * Controller for the monitor garden page. For viewing statistics and live updates for a specific garden.
 */
@Controller
public class CompareGardenController extends NavBar {
    private final UserService userService;
    private final GardenService gardenService;
    private final ArduinoDataPointService arduinoDataPointService;

    /**
     * Spring will automatically call this constructor at runtime to inject the dependencies.
     *
     * @param gardenService A Garden database access object.
     * @param userService   A User database access object.
     * @param arduinoDataPointService A arduinoDataPointService database access object.
     */
    @Autowired
    public CompareGardenController(
            UserService userService,
            GardenService gardenService,
            ArduinoDataPointService arduinoDataPointService
    ) {
        this.userService = userService;
        this.gardenService = gardenService;
        this.arduinoDataPointService = arduinoDataPointService;
    }

    /**
     * Set up monitor garden page and display attributes.
     *
     * @param gardenId1 The id of the garden chosen to be compared
     * @param gardenId2 The id of the users garden chosen to be compared
     * @param model    Puts the data into the template to be viewed
     *
     * @return Thymeleaf html template of the monitor garden page.
     */
    @GetMapping(COMPARE_GARDENS_URI_STRING)
    public String monitorGarden(@PathVariable long gardenId1, @PathVariable long gardenId2, Model model)
            throws NoSuchGardenException {

        this.updateGardensNavBar(model, gardenService, userService);

        Optional<Garden> optionalGarden1 = gardenService.getGardenById(gardenId1);
        Optional<Garden> optionalGarden2 = gardenService.getGardenById(gardenId2);

        if (optionalGarden1.isEmpty() || optionalGarden2.isEmpty()) {
            throw new NoSuchGardenException(gardenId1);
        }
        Garden garden1 = optionalGarden1.get();
        Garden garden2 = optionalGarden2.get();

        model.addAttribute("garden1", garden1);
        model.addAttribute("garden2", garden2);

        //This is where we input if the arduino is connected. Still to be implemented.
        model.addAttribute("connected", false);

        addGraphDataToModel(model, gardenId1, gardenId2);

        return "comparingGraphs";
    }

    /**
     * Helper method to add graph data to html model.
     */
    private void addGraphDataToModel(Model model, Long gardenId1, Long gardenId2) {
        FormattedGraphData dayData = arduinoDataPointService.getDayGraphData(gardenId1, LocalDateTime.now());
        FormattedGraphData weekData = arduinoDataPointService.getWeekGraphData(gardenId1, LocalDateTime.now());
        FormattedGraphData monthData = arduinoDataPointService.getMonthGraphData(gardenId1, LocalDateTime.now());

        dayData.addAll(arduinoDataPointService.getDayGraphData(gardenId2, LocalDateTime.now()));
        weekData.addAll(arduinoDataPointService.getWeekGraphData(gardenId2, LocalDateTime.now()));
        monthData.addAll(arduinoDataPointService.getMonthGraphData(gardenId2, LocalDateTime.now()));

        model.addAttribute("graphDay", dayData);
        model.addAttribute("graphWeek", weekData);
        model.addAttribute("graphMonth", monthData);
    }
}
