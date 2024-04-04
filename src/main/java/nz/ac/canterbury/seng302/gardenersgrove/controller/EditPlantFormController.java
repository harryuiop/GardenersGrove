package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
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
    private final UserService userService;

    private final DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String referer;


    /**
     * Spring calls this constructor at runtime to inject all required dependencies.
     *
     * @param plantService  The Plant database access object.
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public EditPlantFormController(PlantService plantService, GardenService gardenService, UserService userService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Load all necessary attributes into the model for Thymeleaf to show in the HTML.
     *
     * @param plantNameError        An error message associated with the plant name.
     * @param plantCountError       An error message associated with the plant count.
     * @param plantDescriptionError An error message associated with the plant description.
     * @param plantedDateError      An error message associated with the plant date.
     * @param plantImageTypeError   An error message associated with the plant image type.
     * @param plantImageSizeError   An error message associated with the plant image size.
     * @param plantName             The name of the plant.
     * @param plantCount            The number of this plant that appear in the garden.
     * @param plantDescription      The description of the plant.
     * @param plantedDate           The date the plant was planted.
     * @param plantImagePath        The path to the image of the plant.
     * @param plantId               The plant's ID number.
     * @param gardenName            The name of the garden the plant is in.
     * @param gardenId              The garden's ID number.
     * @param model                 The Thymeleaf model to add attributes to.
     * @return The name of the Thymeleaf template to render.
     */
    private String loadPlantForm(
                    String plantNameError,
                    String plantCountError,
                    String plantDescriptionError,
                    String plantedDateError,
                    String plantImageTypeError,
                    String plantImageSizeError,
                    String plantName,
                    Integer plantCount,
                    String plantDescription,
                    Date plantedDate,
                    String plantImagePath,
                    Long plantId,
                    String gardenName,
                    Long gardenId,
                    Model model
    ) {
        this.updateGardensSidebar(model, gardenService, userService);

        model.addAttribute("plantNameError", plantNameError);
        model.addAttribute("plantCountError", plantCountError);
        model.addAttribute("plantDescriptionError", plantDescriptionError);
        model.addAttribute("plantedDateError", plantedDateError);
        model.addAttribute("plantImageTypeError", plantImageTypeError);
        model.addAttribute("plantImageSizeError", plantImageSizeError);

        model.addAttribute("plantName", plantName);
        model.addAttribute("plantCount", plantCount);
        model.addAttribute("plantDescription", plantDescription);
        model.addAttribute("plantedDate", plantedDate != null ? readFormat.format(plantedDate) : null);
        model.addAttribute("plantImagePath", plantImagePath);
        model.addAttribute("plantId", plantId);
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenId", gardenId);
        return "editPlantForm";
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     *
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/plantform/edit")
    public String form(
                    @RequestParam(name = "plantId") Long plantId,
                    HttpServletRequest request,
                    Model model
    ) {
        logger.info("GET /plantform/edit");
        this.referer = request.getHeader("Referer");

        Optional<Plant> optionalPlant = plantService.getPlantById(plantId);
        if (optionalPlant.isEmpty()) {
            return "redirect:" + this.referer;
        }
        Plant plant = optionalPlant.get();

        return loadPlantForm(
                        "", "", "", "", "", "",
                        plant.getName(),
                        plant.getCount(),
                        plant.getDescription(),
                        plant.getPlantedOn(),
                        plant.getImageFilePath(),
                        plant.getId(),
                        plant.getGarden().getName(),
                        plant.getGarden().getId(),
                        model
        );
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
    @PostMapping("/plantform/edit")
    public String submitForm(
                    @RequestParam(name = "plantName") String plantName,
                    @RequestParam(name = "plantCount", required = false) Integer plantCount,
                    @RequestParam(name = "plantDescription", required = false) String plantDescription,
                    @RequestParam(name = "plantedDate", required = false) String plantedDate,
                    @RequestParam(name = "plantImage", required = false) MultipartFile imageFile,
                    @RequestParam(name = "plantId") Long plantId,
                    Model model
    ) {
        logger.info("POST /plantform/edit");

        Optional<Plant> optionalPlant = plantService.getPlantById(plantId);
        if (optionalPlant.isEmpty()) {
            return "redirect:" + this.referer;
        }
        Plant plant = optionalPlant.get();
        Garden garden = plant.getGarden();

        Map<String, String> errors = ErrorChecker.plantFormErrors(
                        plantName,
                        plantCount,
                        plantDescription,
                        imageFile
        );

        Date plantDate = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                plantDate = readFormat.parse(plantedDate);
            }
        } catch (ParseException exception) {
            errors.put("plantedDateError", "Date is not in valid format, yyyy-MM-dd");
        }

        if (!errors.isEmpty()) {
            return loadPlantForm(
                            errors.getOrDefault("plantNameError", ""),
                            errors.getOrDefault("plantCountError", ""),
                            errors.getOrDefault("plantDescriptionError", ""),
                            errors.getOrDefault("plantedDateError", ""),
                            errors.getOrDefault("plantImageTypeError", ""),
                            errors.getOrDefault("plantImageSizeError", ""),
                            plantName,
                            plantCount,
                            plantDescription,
                            plantDate,
                            plant.getImageFilePath(),
                            plantId,
                            garden.getName(),
                            garden.getId(),
                            model
            );
        }

        String imageFileName = null;
        if (!imageFile.isEmpty()) {
            try {
                imageFileName = ImageStore.storeImage(imageFile);
            } catch (IOException error) {
                logger.error("Error saving plant image", error);
            }
        }
        plant.setName(plantName);
        plant.setCount(plantCount);
        plant.setDescription(plantDescription);
        plant.setPlantedOn(plantDate);
        plant.setImageFileName(imageFileName);

        plantService.savePlant(plant);

        return "redirect:/view-garden?gardenId=" + garden.getId();
    }
}
