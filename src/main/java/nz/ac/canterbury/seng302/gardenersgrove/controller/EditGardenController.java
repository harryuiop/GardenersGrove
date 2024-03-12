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
 * Controller for editing garden form.
 */
@Controller
public class EditGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(EditGardenController.class);
    GardenFormSubmission checker = new GardenFormSubmission();

    private final GardenService gardenService;

    private Long id;

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
        logger.info("POST /edit-garden");
        boolean gardenValid = false;
        boolean locationValid = false;
        boolean sizeValid = false;
        try{
            checker.checkName(gardenName);
            gardenValid = true;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Blank")) {
                model.addAttribute("gardenNameError", "Garden name cannot by empty");
            } else if (e.getMessage().equals("InvalidChar")) {
                model.addAttribute(
                                "gardenNameError",
                        "Garden name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes");
            }
        }
        try {
            checker.checkName(gardenLocation);
            locationValid = true;
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Blank")) {
                model.addAttribute("gardenLocationError", "Location cannot be empty");
            } else if (e.getMessage().equals("InvalidChar")) {
                model.addAttribute("gardenLocationError",
                        "Location name must only include letters, numbers, spaces, commas, dots, hyphens or apostrophes"
                );
            }
        }

        try {
            checker.checkSize(gardenSize);
            sizeValid = true;
        } catch (IllegalArgumentException e) {
            model.addAttribute("gardenSizeError", "Garden size must be a positive number");
        }

        if (!gardenValid || !locationValid || !sizeValid) {
            return "redirect:/edit-garden?gardenId=" + this.id;
        }

        if ( gardenService.getGardenById(this.id).isPresent()) {
            Garden garden = gardenService.getGardenById(this.id).get();
            garden.setName(gardenName);
            garden.setLocation(gardenLocation);
            garden.setSize(gardenSize);
            gardenService.saveGarden(garden);
        }
        return "redirect:/view-garden?gardenId=" + this.id;
    }

    /**
     * @param model (map-like) representation of results to be used by thymeleaf
     * @param gardenId represents the Id for the garden in the database
     * @return thymeleaf editGarden
     */
    @GetMapping("/edit-garden")
    public String home(@RequestParam(name = "gardenId", required = true) Long gardenId, Model model) {
        this.updateGardensSidebar(model, gardenService);
        logger.info("GET /edit-garden");
        this.id = gardenId;
        if (gardenService.getGardenById(gardenId).isPresent()) {
            Garden garden = gardenService.getGardenById(gardenId).get();
            model.addAttribute("displayGardenName", garden.getName());
            model.addAttribute("displayGardenLocation", garden.getLocation());
            model.addAttribute("displayGardenSize", garden.getSize());
            model.addAttribute("gardenId", gardenId);
        }
        return "edit-garden";
    }
}
