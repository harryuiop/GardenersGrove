package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ImageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    public PlantFormController(PlantService plantService, GardenService gardenService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
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
        model.addAttribute("plantImage", "");
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
                             @RequestParam(name = "plantImage", required=false) MultipartFile imageFile,
                             Model model) {
        logger.info("POST /plantform");
        boolean nameIsValid = false;
        boolean countIsValid = false;
        boolean descriptionIsValid = false;
        boolean dateIsValid = false;
        boolean gardenIsValid = false;
        boolean imageIsValid = false;

        ImageValidator imageValidator = new ImageValidator(imageFile);
        if (imageFile == null || imageValidator.isValid()) {
            imageIsValid = true;
        } else {
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }

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

        Date plantDate = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                plantDate = DateFormat.getDateInstance().parse(plantedDate);
            }
            dateIsValid = true;
        } catch (ParseException exception) {
            model.addAttribute("plantedDateError", "Date not in valid format (DD/MM/YYYY)");
        }

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isPresent()) {
            gardenIsValid = true;
        }

        if (nameIsValid && countIsValid && descriptionIsValid && dateIsValid && imageIsValid && gardenIsValid) {
            Garden garden = optionalGarden.get();
            String imageFileName = null;
            if (imageFile != null) {
                try {
                    imageFileName = ImageStore.storeImage(imageFile);
                } catch (IOException error) {
                    logger.error("Error saving plant image", error);
                    return "plantForm";
                }
            }
            Plant plant = new Plant(plantName, plantCount, plantDescription, plantDate, imageFileName, gardenId);
            plantService.savePlant(plant);
            garden.addPlant(plant);
            gardenService.saveGarden(garden);

            model.addAttribute("garden", gardenService.getGardenById(gardenId));
            gardenService.saveGarden(garden);
            return "redirect:/view-garden?gardenId=" + garden.getId();
        } else {
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
