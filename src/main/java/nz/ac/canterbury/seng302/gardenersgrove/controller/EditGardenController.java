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
import java.util.Optional;

/**
 * Controller for editing garden form.
 */
@Controller
public class EditGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(EditGardenController.class);
    GardenFormSubmission gardenValidator = new GardenFormSubmission();

    private Long id;

    private final GardenService gardenService;

    /**
     * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
     * @param gardenService the linking agent
     */
    @Autowired
    public EditGardenController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    /**
     *
     * @param gardenName represents the entered name value
     * @param gardenLocation represents the entered location value
     * @param gardenSize represents the enetered garden size
     * @param model represents the results to from thymeleaf
     * @return if the submission is valid, redirects to the garden view page, if invalid reloads the same edit page
     */
    @PostMapping("/edit-garden")
    public String submitForm(@RequestParam(name="gardenName") String gardenName,
                             @RequestParam(name = "gardenLocation") String gardenLocation,
                             @RequestParam(name = "gardenSize", required=false) Float gardenSize,
                             Model model) {
        Long gardenId = this.id;
        logger.info("POST /edit-garden");
        Map<String, String> errors = gardenValidator.formErrors(gardenName, gardenLocation, gardenSize);
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (errors.isEmpty()) {
            if (optionalGarden.isPresent()) {
                Garden garden = optionalGarden.get();
                garden.setName(gardenName);
                garden.setLocation(gardenLocation);
                garden.setSize(gardenSize);
                gardenService.saveGarden(garden);
            }
        } else {
            gardenValidator.addErrorAttributes(model, errors);
            this.addEditGardenAttributes(gardenId, model);
            return "editGarden";
        }
        return "redirect:/view-garden?gardenId=" + gardenId;
    }


    /**
     * @param model (map-like) representation of results to be used by thymeleaf
     * @param gardenId represents the identifier for the garden in the database
     * @return thymeleaf editGarden
     */
    @GetMapping("/edit-garden")
    public String home(@RequestParam(name = "gardenId", required = true) Long gardenId, Model model) {
        logger.info("GET /edit-garden");
        this.addEditGardenAttributes(gardenId, model);
        this.id = gardenId;
        return "editGarden";
    }

    private void addEditGardenAttributes(Long gardenId, Model model) {
        this.updateGardensSidebar(model, gardenService);

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            model.addAttribute("displayGardenName", garden.getName());
            model.addAttribute("displayGardenLocation", garden.getLocation());
            model.addAttribute("displayGardenSize", garden.getSize());
            model.addAttribute("gardenId", gardenId);
        }
    }
}
