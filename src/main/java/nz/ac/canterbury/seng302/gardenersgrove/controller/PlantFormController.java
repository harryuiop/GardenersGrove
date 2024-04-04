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
public class PlantFormController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(PlantFormController.class);

    private final PlantService plantService;
    private final GardenService gardenService;
    private final UserService userService;

    private final DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat printFormat = new SimpleDateFormat("dd/MM/yyyy");

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
    }

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
        model.addAttribute("plantImage", plantImagePath);
        model.addAttribute("gardenId", gardenId);
        return "plantForm";
    }

    /**
     * Gets form to be displayed and passes previous form values and user/garden information to the HTML.
     *
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/plantform")
    public String form(
                    @RequestParam(name = "gardenId") Long gardenId,
                    Model model
    ) {
        logger.info("GET /plantform");

        return loadPlantForm(
                        "", "", "", "", "", "",
                        null, null, null, null, null,
                        gardenId,
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
    @PostMapping("/plantform")
    public String submitForm(
                    @RequestParam(name = "plantName") String plantName,
                    @RequestParam(name = "plantCount", required = false) Integer plantCount,
                    @RequestParam(name = "plantDescription", required = false) String plantDescription,
                    @RequestParam(name = "plantedDate", required = false) String plantedDate,
                    @RequestParam(name = "gardenId") Long gardenId,
                    @RequestParam(name = "plantImage", required = false) MultipartFile imageFile,
                    HttpServletRequest request,
                    Model model
    ) {
        logger.info("POST /plantform");

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            return "redirect:" + request.getHeader("Referer");
        }
        Garden garden = optionalGarden.get();

        Map<String, String> errors = ErrorChecker.plantFormErrors(
                        plantName,
                        plantCount,
                        plantDescription,
                        imageFile
        );

        Date date = null;
        try {
            if (plantedDate != null && !plantedDate.isBlank()) {
                date = readFormat.parse(plantedDate);
            }
        } catch (ParseException exception) {
            errors.put("plantedDateError", "Planted date must be in the format yyyy-MM-dd");
        }
        String imageFileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageFileName = ImageStore.storeImage(imageFile);
            } catch (IOException error) {
                logger.error("Error saving plant image", error);
                errors.put("plantImageTypeError", "Uploading image failed, please try again.");
            }
        }

        if (!errors.isEmpty()) {
            return loadPlantForm(
                            errors.get("plantNameError"),
                            errors.get("plantCountError"),
                            errors.get("plantDescriptionError"),
                            errors.get("plantedDateError"),
                            errors.get("plantImageTypeError"),
                            errors.get("plantImageSizeError"),
                            plantName,
                            plantCount,
                            plantDescription,
                            date,
                            imageFileName != null ? "/uploads/" + imageFileName : "images/default-plant.png",
                            gardenId,
                            model
            );
        }

        Plant plant = new Plant(plantName, plantCount, plantDescription, date, imageFileName, garden);
        plantService.savePlant(plant);
        return "redirect:/view-garden?gardenId=" + garden.getId();
    }
}
