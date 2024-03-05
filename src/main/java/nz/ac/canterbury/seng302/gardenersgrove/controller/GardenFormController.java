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
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/form")
    public String form(Model model) {
        logger.info("GET /form");
        model.addAttribute("gardenNameError", "");
        model.addAttribute("gardenLocationError", "");
        model.addAttribute("gardenSizeError", "");
        return "gardenForm";
    }

    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Submits form and saves the garden to the database.
     * @param gardenName The name of the garden as input by the user.
     * @param gardenLocation The location of the garden as input by the user.
     * @param gardenSize The size of the garden as input by the user.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping("/form")
    public String submitForm(@RequestParam(name = "gardenName") String gardenName,
                             @RequestParam(name = "gardenLocation") String gardenLocation,
                             @RequestParam(name = "gardenSize", required = false) Float gardenSize,
                             Model model) {
        logger.info("POST /form");
        boolean nameIsValid = false;
        boolean locationIsValid = false;
        boolean sizeIsValid = false;

        if (gardenName.isBlank()) {
            model.addAttribute("gardenNameError", "Garden name cannot by empty");
        } else if (!checkString(gardenName)) {
            model.addAttribute(
                "gardenNameError",
                "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
        } else {
            nameIsValid = true;
        }

        if (gardenLocation.isBlank()) {
            model.addAttribute("gardenLocationError", "Location cannot be empty");
        } else if (!checkString(gardenLocation)) {
            model.addAttribute(
                "gardenLocationError",
                "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
            );
        } else {
            locationIsValid = true;
        }

        if (gardenSize != null && gardenSize <= 0) {
            model.addAttribute("gardenSizeError", "Garden size must be a positive number");
        } else {
            sizeIsValid = true;
        }

        if (nameIsValid && locationIsValid && sizeIsValid) {
            gardenService.saveGarden(new Garden(gardenName, gardenLocation, gardenSize));
            return "redirect:/";
        } else {
            return "gardenForm";
        }
    }
}
