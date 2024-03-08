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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import nz.ac.canterbury.seng302.gardenersgrove.components.FormSubmission;
import java.util.HashMap;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class EditGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(EditGardenController.class);
    FormSubmission checker = new FormSubmission();

    private final GardenService gardenService;

    private Long id;

    @Autowired
    public EditGardenController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    @PostMapping("/edit-garden")
    public String submitForm(@RequestParam(name="gardenName") String gardenName,
                             @RequestParam(name = "gardenLocation") String gardenLocation,
                             @RequestParam(name = "gardenSize", required=false) Float gardenSize,
                             Model model) {
        logger.info("POST /edit-garden");
        HashMap<String, String> errors = checker.formErrors(gardenName, gardenLocation, gardenSize);
        if (errors.isEmpty()) {
            if ( gardenService.getGardenById(this.id).isPresent()) {
                Garden garden = gardenService.getGardenById(this.id).get();
                garden.setName(gardenName);
                garden.setLocation(gardenLocation);
                garden.setSize(gardenSize);
                gardenService.saveGarden(garden);
            }
        } else {
            for (String i: errors.keySet()) {
                model.addAttribute(i, errors.get(i));
            }
            model.addAttribute("gardenName", gardenName);
            model.addAttribute("gardenLocation", gardenLocation);
            model.addAttribute("gardenSize", gardenSize);
            return "gardenForm";
        }
        return "redirect:/view-garden?gardenId=" + this.id;
        }

    /**
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf gardenResponse
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
        return "editGarden";
    }
}
