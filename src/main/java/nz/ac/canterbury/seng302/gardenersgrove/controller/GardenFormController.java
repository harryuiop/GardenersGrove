package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class GardenFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(GardenFormController.class);
    private final GardenService gardenService;
    private final ErrorChecker Validator;

    private String url;

    @Autowired
    public GardenFormController(GardenService gardenService) {
        this.gardenService = gardenService;
        this.Validator = new ErrorChecker();
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param model object that passes data through to the HTML.
     * @param url string to redirect to if the cancel button is selected
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/gardenform")
    public String form(@RequestHeader(name="Referer") String url,  Model model) {
        logger.info("GET /form");
        this.url = url;
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("gardenNameError", "");
        model.addAttribute("gardenLocationError", "");
        model.addAttribute("gardenSizeError", "");
        model.addAttribute("url", this.url);
        return "gardenForm";
    }



    /**
     * Submits form and saves the garden to the database or redirects to the previous page if cancelled.
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
        Map<String, String> errors = Validator.gardenFormErrors(gardenName, gardenLocation, gardenSize);
        if (errors.isEmpty()) {
            Garden garden = new Garden(gardenName, gardenLocation, gardenSize);
            gardenService.saveGarden(garden);
            return "redirect:/view-garden?gardenId=" + garden.getId();
        }
        else {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("gardenName", gardenName);
            model.addAttribute("gardenLocation", gardenLocation);
            model.addAttribute("gardenSize", gardenSize);
            model.addAttribute("url", this.url);
            return "gardenForm";
        }
    }
}
