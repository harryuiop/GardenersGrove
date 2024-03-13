package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class PlantFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(PlantFormController.class);
    private final PlantService plantService;
    private final GardenService gardenService;
    private final ErrorChecker validate;

    @Autowired
    public PlantFormController(PlantService plantService, GardenService gardenService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
        this.validate =  new ErrorChecker();
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/plantform")
    public String form(Model model, @RequestParam(name="gardenId") Long gardenId) {
        logger.info("GET /plantform");
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("plantNameError", "");
        model.addAttribute("plantCountError", "");
        model.addAttribute("plantDescriptionError", "");
        model.addAttribute("plantedDateError", "");
        model.addAttribute("gardenName", gardenService.getGardenById(gardenId).get().getName());
        model.addAttribute("gardenId", gardenId);
        return "plantForm";
    }

    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    /**
     * Submits form and saves the garden to the database.
     * @param plantName The name of the plant as input by the user.
     * @param plantCount The number of plants as input by the user.
     * @param plantDescription The description of the plant as input by the user.
     * @param plantedDate The date the plant was planted as input by the user.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping("/plantform")
    public String submitForm(@RequestParam(name = "plantName") String plantName,
                             @RequestParam(name = "plantCount", required = false) Integer plantCount,
                             @RequestParam(name = "plantDescription", required = false) String plantDescription,
                             @RequestParam(name = "plantedDate", required = false) String plantedDate,
                             @RequestParam(name = "gardenId") Long gardenId,
                             Model model) {
        logger.info("POST /form");
        Map<String, String> errors = validate.plantFormErrors(plantName, plantCount, plantDescription);

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);

        Date plantDate = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                plantDate = DateFormat.getDateInstance().parse(plantedDate);
            }
        } catch (ParseException exception) {
            errors.put("plantedDateError", "Date is not in valid format, DD/MM/YYYY");
        }

        if (errors.isEmpty() && optionalGarden.isPresent()) {
            Plant plant = new Plant(plantName, plantCount, plantDescription, plantDate, gardenId);
            plantService.savePlant(plant);
            Garden garden = optionalGarden.get();
            garden.addPlant(plant);
            gardenService.saveGarden(garden);
            model.addAttribute("gardenId", gardenId);
            return "redirect:/view-garden?gardenId=" + garden.getId();
        } else {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("plantName", plantName);
            model.addAttribute("plantCount", plantCount);
            model.addAttribute("plantDescription", plantDescription);
            model.addAttribute("plantedDate", plantedDate);
            model.addAttribute("gardenName", gardenService.getGardenById(gardenId).get().getName());
            model.addAttribute("gardenId", gardenId);
            return "plantForm";
        }
    }
}
