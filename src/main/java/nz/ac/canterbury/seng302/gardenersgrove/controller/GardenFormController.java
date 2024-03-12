package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
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
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.GardenFormSubmission;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class GardenFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(GardenFormController.class);
    GardenFormSubmission checker = new GardenFormSubmission();
    private final GardenService gardenService;

    private static final String GARDEN_NAME_ERROR = "gardenNameError";
    private static final String GARDEN_LOCATION_ERROR = "gardenLocationError";
    private static final String GARDEN_SIZE_ERROR = "gardenSizeError";

    @Autowired
    public GardenFormController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenform template.
     */
    @GetMapping("/gardenform")
    public String form(Model model) {
        logger.info("GET /form");
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute(GARDEN_NAME_ERROR, "");
        model.addAttribute(GARDEN_LOCATION_ERROR, "");
        model.addAttribute(GARDEN_SIZE_ERROR, "");
        return "gardenform";
    }



    /**
     * Submits form and saves the garden to the database.
     * @param gardenName The name of the garden as input by the user.
     * @param gardenLocation The location of the garden as input by the user.
     * @param gardenSize The size of the garden as input by the user.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping("/gardenform")
    public String submitForm(@RequestParam(name = "gardenName") String gardenName,
                             @RequestParam(name = "gardenLocation") String gardenLocation,
                             @RequestParam(name = "gardenSize", required = false) Float gardenSize,
                             Model model) {
        logger.info("POST /form");
        boolean gardenValid = false;
        boolean locationValid = false;
        boolean sizeValid = false;
        try{
            checker.checkName(gardenName);
            gardenValid = true;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Blank")) {
                model.addAttribute(GARDEN_NAME_ERROR, "Garden name cannot by empty");
            } else if (e.getMessage().equals("InvalidChar")) {
                model.addAttribute(
                        GARDEN_NAME_ERROR,
                        "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
            }
        }
        try {
            checker.checkName(gardenLocation);
            locationValid = true;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Blank")) {
                model.addAttribute(GARDEN_LOCATION_ERROR, "Location cannot be empty");
            } else if (e.getMessage().equals("InvalidChar")) {
                model.addAttribute(GARDEN_LOCATION_ERROR,
                        "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
                );
            }
        }

        try {
            checker.checkSize(gardenSize);
            sizeValid = true;
        } catch (IllegalArgumentException e) {
            model.addAttribute(GARDEN_SIZE_ERROR, "Garden size must be a positive number");
        }

        if (!gardenValid || !locationValid || !sizeValid) {
            return "gardenform";
        }
        Garden garden = new Garden(gardenName, gardenLocation, gardenSize);
        gardenService.saveGarden(garden);
        return "redirect:/view-garden?gardenId=" + garden.getId();
    }
}
