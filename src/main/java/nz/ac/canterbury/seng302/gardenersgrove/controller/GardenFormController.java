package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FormResult;
import nz.ac.canterbury.seng302.gardenersgrove.service.FormService;
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
 * Note the @link{Autowired} annotation giving us access to the @lnik{FormService} class automatically
 */
@Controller
public class GardenFormController {
    Logger logger = LoggerFactory.getLogger(GardenFormController.class);

    private final FormService formService;

    @Autowired
    public GardenFormController(FormService formService) {
        this.formService = formService;
    }
    /**
     * Gets form to be displayed, includes the ability to display results of previous form when linked to from POST form
     * @param displayName previous name entered into form to be displayed
     * @param displayLanguage previous favourite programming language entered into form to be displayed
     * @param model (map-like) representation of name, language and isJava boolean for use in thymeleaf
     * @return thymeleaf gardenForm
     */
    @GetMapping("/form")
    public String form(@RequestParam(name="displayGardenName", required = true, defaultValue = "") String displayGardenName,
                       @RequestParam(name="displayGardenLocation", required = true, defaultValue = "") String displayGardenLocation,
                       @RequestParam(name="displayGardenSize", required = false, defaultValue = "") String displayGardenSize,
                       Model model) {
        logger.info("GET /form");
        model.addAttribute("displayGardenName", displayGardenName);
        model.addAttribute("displayGardenLocation", displayGardenLocation);
        model.addAttribute("displayGardenSize", displayGardenSize);
        model.addAttribute("Home", displayGardenLocation.equalsIgnoreCase("Home"));
        return "gardenForm";
    }

    /**
     * Posts a form response with name and favourite language
     * @param name name if user
     * @param favouriteLanguage users favourite programming language
     * @param model (map-like) representation of name, language and isJava boolean for use in thymeleaf,
     *              with values being set to relevant parameters provided
     * @return thymeleaf gardenForm
     */
    @PostMapping("/form")
    public String submitForm( @RequestParam(name="gardenName") String gardenName,
                              @RequestParam(name = "gardenLocation") String gardenLocation,
                              @RequestParam(name = "gardenSize") String gardenSize,
                              Model model) {
        logger.info("POST /form");
        formService.addFormResult(new FormResult(gardenName, gardenLocation, gardenSize));
        model.addAttribute("displayGardenName", gardenName);
        model.addAttribute("displayGardenLocation", gardenLocation);
        model.addAttribute("displayGardenSize", gardenSize);
        model.addAttribute("Home", gardenLocation.equalsIgnoreCase("Home"));
        return "gardenForm";
    }

    /**
     * Gets all form responses
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf gardenResponse
     */
    @GetMapping("/form/responses")
    public String responses(Model model) {
        logger.info("GET /form/responses");
        model.addAttribute("responses", formService.getFormResults());
        return "gardenResponse";
    }
}
