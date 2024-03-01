package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class GardenFormController {
    Logger logger = LoggerFactory.getLogger(GardenFormController.class);

    private final GardenService gardenService;

    @Autowired
    public GardenFormController(GardenService gardenService) {
        this.gardenService = gardenService;
    }
    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param displayGardenName garden name value for used in HTML.
     * @param displayGardenLocation garden location value for use in HTML.
     * @param displayGardenSize garden size value for use in HTML.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/form")
    public String form(@RequestParam(name="displayGardenName", defaultValue = "") String displayGardenName,
                       @RequestParam(name="displayGardenLocation", defaultValue = "") String displayGardenLocation,
                       @RequestParam(name="displayGardenSize", required = false) Float displayGardenSize,
                       Model model) {
        logger.info("GET /form");
        model.addAttribute("displayGardenName", displayGardenName);
        model.addAttribute("displayGardenLocation", displayGardenLocation);
        model.addAttribute("displayGardenSize", displayGardenSize);
        return "gardenForm";
    }

    /**
     * Submits form and saves the garden to the database.
     * @param gardenName The name of the garden as input by the user.
     * @param gardenLocation The location of the garden as input by the user.
     * @param gardenSize The size of the garden as input by the user.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @PostMapping("/form")
    public String submitForm( @RequestParam(name="gardenName") String gardenName,
                              @RequestParam(name = "gardenLocation") String gardenLocation,
                              @RequestParam(name = "gardenSize", required = false) Float gardenSize,
                              Model model) {
        logger.info("POST /form");
        gardenService.saveGarden(new Garden(gardenName, gardenLocation, gardenSize));
        model.addAttribute("displayGardenName", gardenName);
        model.addAttribute("displayGardenLocation", gardenLocation);
        model.addAttribute("displayGardenSize", gardenSize);
        return "gardenForm";
    }
}
