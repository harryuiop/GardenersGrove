package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class EditPlantFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(EditPlantFormController.class);

    private final PlantService plantService;
    private final GardenService gardenService;
    private final ErrorChecker validate;
    private final DateFormat printFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final DateFormat readFormat = new SimpleDateFormat("yyyy/MM/dd");
    private Long id;

    @Autowired
    public EditPlantFormController(PlantService plantService, GardenService gardenService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
        this.validate = new ErrorChecker();
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/plantform/edit")
    public String form(Model model, @RequestParam(name="plantId") Long plantId) {
        logger.info("GET /plantform/edit");
        this.updateGardensSidebar(model, gardenService);
        this.id = plantId;
        model.addAttribute("plantNameError", "");
        model.addAttribute("plantCountError", "");
        model.addAttribute("plantDescriptionError", "");
        model.addAttribute("plantedDateError", "");
        model.addAttribute("plantName", plantService.getPlantById(plantId).get().getName());
        model.addAttribute("plantCount", plantService.getPlantById(plantId).get().getCount());
        model.addAttribute("plantDescription", plantService.getPlantById(plantId).get().getDescription());
        model.addAttribute("plantedDate", plantService.getPlantById(plantId).get().getPlantedOn());
        model.addAttribute("plantId", plantId);
        model.addAttribute("gardenId", plantService.getPlantById(plantId).get().getGardenId());
        return "editPlantForm";
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
    @PostMapping("/plantform/edit")
    public String submitForm(@RequestParam(name = "plantName") String plantName,
                             @RequestParam(name = "plantCount", required = false) Integer plantCount,
                             @RequestParam(name = "plantDescription", required = false) String plantDescription,
                             @RequestParam(name = "plantedDate", required = false) String plantedDate,
                             @RequestParam(name = "plantId") Long plantId,
                             Model model) {
        logger.info("POST /plantform/edit");

        Map<String, String> errors = validate.plantFormErrors(plantName, plantCount, plantDescription);

        Optional<Garden> optionalGarden = gardenService.getGardenById(plantService.getPlantById(plantId).get().getGardenId());

        Date plantDate = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                plantDate = readFormat.parse(plantedDate);
            }
        } catch (ParseException exception) {
            errors.put("plantedDateError", "Date is not in valid format, DD/MM/YYYY");
        }

        if (errors.isEmpty() && optionalGarden.isPresent()) {
            Plant plant = plantService.getPlantById(this.id).get();
            plant.setName(plantName);
            plant.setCount(plantCount);
            plant.setDescription(plantDescription);
            plant.setPlantedOn(plantDate);
            plantService.savePlant(plant);

            return "redirect:/view-garden?gardenId=" + plant.getGardenId();
        } else {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("plantName", plantName);
            model.addAttribute("plantCount", plantCount);
            model.addAttribute("plantDescription", plantDescription);
            model.addAttribute("plantedDate", plantedDate);
            model.addAttribute("plantId", plantId);
            model.addAttribute("gardenName", gardenService.getGardenById(plantService.getPlantById(plantId).get().getGardenId()).get().getName());
            model.addAttribute("gardenId", plantService.getPlantById(plantId).get().getGardenId());
            return "editPlantForm";
        }
    }
}
