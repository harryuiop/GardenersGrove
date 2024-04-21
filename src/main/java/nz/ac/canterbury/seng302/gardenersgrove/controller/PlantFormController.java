package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    private final UserService userService;
    private final ErrorChecker validate;

    /**
     * The PlantFormController constructor need not be called ever.
     * It is autowired in by Spring at run time to initialise instances of all the necessary services.
     *
     * @param plantService  The Plant database access object.
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public PlantFormController(PlantService plantService, GardenService gardenService, UserService userService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
        this.userService = userService;
        this.validate = new ErrorChecker();
    }

    /**
     * Gets form to be displayed and passes previous form values and user/garden information to the HTML.
     *
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/plantform")
    public String form(Model model, @RequestParam(name = "gardenId") Long gardenId) {
        logger.info("GET /plantform");
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAttribute("plantNameError", "");
        model.addAttribute("plantCountError", "");
        model.addAttribute("plantDescriptionError", "");
        model.addAttribute("plantedDateError", "");
        gardenService.getGardenById(gardenId).ifPresent(
                        garden -> model.addAttribute("gardenName", garden.getName())
        );
        model.addAttribute("gardenId", gardenId);
        model.addAttribute("plantImage", "");
        return "plantForm";
    }

    /**
     * Submits form and saves the garden to the database.
     *
     * @param plantName        The name of the plant as input by the user.
     * @param plantCount       The number of plants as input by the user.
     * @param plantDescription The description of the plant as input by the user.
     * @param plantedDate      The date the plant was planted as input by the user.
     * @param model            object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping("/plantform")
    public String submitForm(@RequestParam(name = "plantName") String plantName,
                             @RequestParam(name = "plantCount", required = false) Integer plantCount,
                             @RequestParam(name = "plantDescription", required = false) String plantDescription,
                             @RequestParam(name = "plantedDate", required = false) String plantedDate,
                             @RequestParam(name = "gardenId") Long gardenId,
                             @RequestParam(name = "plantImage", required = false) MultipartFile imageFile,
                             Model model) {
        logger.info("POST /plantform");

        boolean imageIsValid = false;

        Map<String, String> errors = validate.plantFormErrors(plantName, plantCount, plantDescription);

        ImageValidator imageValidator = new ImageValidator(imageFile);
        if (imageFile.isEmpty() || imageValidator.isValid()) {
            imageIsValid = true;
        } else {
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            return "redirect:/";
        }
        Garden garden = optionalGarden.get();

        LocalDate plantDate = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                plantDate = LocalDate.parse(plantedDate);
            }
        } catch (DateTimeParseException exception) {
            errors.put("plantedDateError", "Date is not in valid format, DD/MM/YYYY");
        }

        if (errors.isEmpty() && imageIsValid) {
            String imageFileName = null;
            if (!imageFile.isEmpty()) {
                try {
                    imageFileName = ImageStore.storeImage(imageFile);
                } catch (IOException error) {
                    logger.error("Error saving plant image", error);
                    return "plantForm";
                }
            }
            Plant plant = new Plant(plantName, plantCount, plantDescription, plantDate, imageFileName, garden);
            plantService.savePlant(plant);
            model.addAttribute("gardenId", gardenId);
            return "redirect:/view-garden?gardenId=" + gardenId;
        } else {
            this.updateGardensSidebar(model, gardenService, userService);
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("plantName", plantName);
            model.addAttribute("plantCount", plantCount);
            model.addAttribute("plantDescription", plantDescription);
            model.addAttribute("plantedDate", plantedDate);
            model.addAttribute("gardenName", garden.getName());
            model.addAttribute("gardenId", gardenId);
            return "plantForm";
        }
    }
}
