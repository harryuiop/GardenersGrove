package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
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
import java.text.SimpleDateFormat;
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

    private final DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat printFormat = new SimpleDateFormat("dd/MM/yyyy");

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
        model.addAttribute("plantImage", "");
        return "plantForm";
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
        boolean imageIsValid = false;

        Map<String, String> errors = validate.plantFormErrors(plantName, plantCount, plantDescription);

        ImageValidator imageValidator = new ImageValidator(imageFile);
        logger.info("bool imageFile", (imageFile.isEmpty()));
        if (imageFile.isEmpty() || imageValidator.isValid()) {
            imageIsValid = true;
        } else {
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);

        Date plantDate = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                plantDate = readFormat.parse(plantedDate);
            }
        } catch (ParseException exception) {
            errors.put("plantedDateError", "Date is not in valid format, DD/MM/YYYY");
        }

        if (errors.isEmpty() && optionalGarden.isPresent() && imageIsValid) {
            Garden garden = optionalGarden.get();
            String imageFileName = null;
            if (!imageFile.isEmpty()) {
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
            model.addAttribute("gardenId", gardenId);
            return "redirect:/view-garden?gardenId=" + gardenId;
        } else {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("plantName", plantName);
            model.addAttribute("plantCount", plantCount);
            model.addAttribute("plantDescription", plantDescription);
            model.addAttribute("plantedDate", plantedDate);
            optionalGarden.ifPresent(garden -> model.addAttribute("gardenName", garden.getName()));
            model.addAttribute("gardenId", gardenId);
            return "plantForm";
        }
    }
}
