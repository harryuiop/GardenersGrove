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

import java.util.Map;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class GardenFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(GardenFormController.class);
    GardenFormSubmission gardenValidator = new GardenFormSubmission();
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
    @GetMapping("/gardenform")
    public String form(Model model) {
        logger.info("GET /form");
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("gardenNameError", "");
        model.addAttribute("gardenLocationError", "");
        model.addAttribute("gardenSizeError", "");
        return "gardenForm";
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
        Map<String, String> errors = gardenValidator.formErrors(gardenName, gardenLocation, gardenSize);
        if (errors.isEmpty()) {
            Garden garden = new Garden(gardenName, gardenLocation, gardenSize);
            gardenService.saveGarden(garden);
            return "redirect:/view-garden?gardenId=" + garden.getId();
        }
        else {
            gardenValidator.addErrorAttributes(model, errors);
            model.addAttribute("gardenName", gardenName);
            model.addAttribute("gardenLocation", gardenLocation);
            model.addAttribute("gardenSize", gardenSize);
            return "gardenForm";
        }
    }
}
