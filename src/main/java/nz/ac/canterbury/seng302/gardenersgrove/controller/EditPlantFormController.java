package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
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

import java.util.Date;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class EditPlantFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(EditPlantFormController.class);

    private final PlantService plantService;
    private final GardenService gardenService;

    private Long id;

    @Autowired
    public EditPlantFormController(PlantService plantService, GardenService gardenService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
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
        return "editPlantForm";
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
    @PostMapping("/plantform/edit")
    public String submitForm(@RequestParam(name = "plantName") String plantName,
                             @RequestParam(name = "plantCount", required = false) Integer plantCount,
                             @RequestParam(name = "plantDescription", required = false) String plantDescription,
                             @RequestParam(name = "plantedDate", required = false) String plantedDate,
                             @RequestParam(name = "plantId") Long plantId,
                             Model model) {
        logger.info("POST /plantform/edit");
        boolean nameIsValid = false;
        boolean countIsValid = false;
        boolean descriptionIsValid = false;
        boolean dateIsValid = true;

        if (plantName.isBlank() || !checkString(plantName)) {
            model.addAttribute(
                    "plantNameError",
                    "Plant name cannot by empty and must only include letters, numbers, spaces, dots, hyphens or apostrophes");
        } else {
            nameIsValid = true;
        }

        if (plantCount != null && plantCount <= 0) {
            model.addAttribute("plantCountError", "Plant count must be a positive number");
        } else {
            countIsValid = true;
        }

        if (plantDescription != null && plantDescription.length() > 512) {
            model.addAttribute("plantDescriptionError", "Plant description must be less than 512 characters");
        } else {
            descriptionIsValid = true;
        }

        if (nameIsValid && countIsValid && descriptionIsValid && dateIsValid) {
            Plant plant = plantService.getPlantById(this.id).get();
            Date date = null;
            if (!plantedDate.isBlank()) {
                date = new Date(Integer.parseInt(plantedDate.split("-")[2]), Integer.parseInt(plantedDate.split("-")[1]), Integer.parseInt(plantedDate.split("-")[0]));
            }
            plant.setName(plantName);
            plant.setCount(plantCount);
            plant.setDescription(plantDescription);
            plant.setPlantedOn(date);
            plantService.savePlant(plant);

            return "redirect:/view-garden?gardenId=" + plant.getGardenId();
        } else {
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
