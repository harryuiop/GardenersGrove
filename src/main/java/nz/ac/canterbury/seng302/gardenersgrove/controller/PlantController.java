package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchPlantException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@Controller
public class PlantController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(PlantController.class);

    private final PlantService plantService;
    private final GardenService gardenService;
    private final UserService userService;

    /**
     * The PlantFormController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param plantService  The Plant database access object.
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public PlantController(PlantService plantService, GardenService gardenService, UserService userService) {
        this.plantService = plantService;
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Loads the plant form with the given errors and pre-filled values.
     *
     * @param plantNameError        The error message for the plant name form field.
     * @param plantCountError       The error message for the plant count form field.
     * @param plantDescriptionError The error message for the plant description form field.
     * @param plantedDateError      The error message for the planted date form field.
     * @param imageTypeError   The error message for the plant image type.
     * @param imageSizeError   The error message for the plant image size.
     * @param plantImageUploadError The error message for the plant image upload.
     * @param plantName             The name to pre-fill the form with.
     * @param plantCount            The count to pre-fill the form with.
     * @param plantDescription      The description to pre-fill the form with.
     * @param plantedDate           The date to pre-fill the form with.
     * @param plantImagePath        The path to the image to show on the form.
     * @param gardenName            The name of the garden the plant is in.
     * @param formSubmissionUri     The URI to submit the form to.
     * @param cancelButtonUri       The URI to direct to if the user presses the cancel button.
     * @param model                 The model to pass the data to the HTML.
     * @return The name of the HTML template to render.
     */
    private String loadPlantForm(
                    String plantNameError,
                    String plantCountError,
                    String plantDescriptionError,
                    String plantedDateError,
                    String imageTypeError,
                    String imageSizeError,
                    String plantImageUploadError,
                    String plantName,
                    Integer plantCount,
                    String plantDescription,
                    LocalDate plantedDate,
                    String plantImagePath,
                    String gardenName,
                    URI formSubmissionUri,
                    URI cancelButtonUri,
                    Model model
    ) {
        this.updateGardensSidebar(model, gardenService, userService);

        model.addAttribute("plantNameError", plantNameError);
        model.addAttribute("plantCountError", plantCountError);
        model.addAttribute("plantDescriptionError", plantDescriptionError);
        model.addAttribute("plantedDateError", plantedDateError);
        model.addAttribute("imageTypeError", imageTypeError);
        model.addAttribute("imageSizeError", imageSizeError);
        model.addAttribute("plantImageUploadError", plantImageUploadError);

        model.addAttribute("plantName", plantName);
        model.addAttribute("plantCount", plantCount);
        model.addAttribute("plantDescription", plantDescription);
        model.addAttribute("plantedDate", plantedDate != null ? plantedDate.toString() : null);
        model.addAttribute("plantImagePath", plantImagePath);

        model.addAttribute("gardenName", gardenName);
        model.addAttribute("formSubmissionUri", formSubmissionUri);
        model.addAttribute("cancelButtonUri", cancelButtonUri);
        return "plantForm";
    }

    /**
     * Serves the plant form to the user, passing user/garden information to the HTML.
     *
     * @param gardenId The id of the garden to place the plant in.
     * @param model    object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping(NEW_PLANT_URI_STRING)
    public String createPlant(
                    @PathVariable long gardenId,
                    Model model
    ) throws NoSuchGardenException {
        logger.info("GET {}", newPlantUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();
        return loadPlantForm(
                        "", "", "", "", "", "", "",
                        null, null, null, null, "/images/default-plant.jpg",
                        garden.getName(),
                        newPlantUri(gardenId), viewGardenUri(gardenId),
                        model
        );
    }

    /**
     * Serves the plant form to the user, passing user/garden information to the HTML,
     * with existing plant information pre-filled.
     *
     * @param plantId The id of the plant to edit.
     * @param model   object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping(EDIT_PLANT_URI_STRING)
    public String editPlant(
                    @PathVariable long gardenId,
                    @PathVariable long plantId,
                    Model model
    ) throws NoSuchPlantException {
        logger.info("GET {}", editPlantUri(gardenId, plantId));

        Optional<Plant> optionalPlant = plantService.getPlantByGardenIdAndPlantId(gardenId, plantId);
        if (optionalPlant.isEmpty()) {
            throw new NoSuchPlantException(
                            "Unable to find plant with id " + plantId + " in garden with id " + gardenId + "."
            );
        }
        Plant plant = optionalPlant.get();

        return loadPlantForm(
                        "", "", "", "", "", "", "",
                        plant.getName(),
                        plant.getCount(),
                        plant.getDescription(),
                        plant.getPlantedOn(),
                        plant.getImageFilePath(),
                        plant.getGarden().getName(),
                        editPlantUri(gardenId, plantId),
                        viewGardenUri(gardenId),
                        model
        );
    }

    /**
     * Parse the date string into a {@link LocalDate} object.
     *
     * @param dateString The string to parse.
     * @param errors     The map of errors to update with any error.
     * @return The parsed date if parse was successful or {@code null} if not.
     */
    private LocalDate parseDate(String dateString, Map<String, String> errors) {
        LocalDate date = null;
        try {
            if (dateString != null && !dateString.isBlank()) {
                date = LocalDate.parse(dateString);
            }
        } catch (DateTimeParseException exception) {
            errors.put("plantedDateError", "Planted date must be in the format yyyy-MM-dd");
        }
        return date;
    }

    /**
     * Save the image to disk if there are no form errors and the image file isn't empty.
     *
     * @param image  The file to write to disk.
     * @param errors The map of errors to update with any error.
     * @return The name of the file if save was successful or {@code null} if not.
     */
    private String saveImage(MultipartFile image, Map<String, String> errors) {
        String imageFileName = null;
        if (errors.isEmpty() && image != null && !image.isEmpty()) {
            try {
                imageFileName = ImageStore.storeImage(image);
            } catch (IOException error) {
                logger.error("Error saving plant image", error);
                errors.put("plantImageUploadError", "Uploading image failed, please try again.");
            }
        }
        return imageFileName;
    }

    /**
     * Submits form and saves the plant to the database.
     *
     * @param plantName        The name of the plant as input by the user.
     * @param plantCount       The number of plants as input by the user.
     * @param plantDescription The description of the plant as input by the user.
     * @param plantedDate      The date the plant was planted as input by the user.
     *                         Must be in ISO format (yyyy-MM-dd).
     * @param plantImage       The image file uploaded by the user.
     * @param gardenId         The id of the garden the plant is in.
     * @param model            object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping(NEW_PLANT_URI_STRING)
    public String submitNewPlant(
                    @PathVariable long gardenId,
                    @RequestParam String plantName,
                    @RequestParam(required = false) Integer plantCount,
                    @RequestParam(required = false) String plantDescription,
                    @RequestParam(required = false) String plantedDate,
                    @RequestParam(required = false) MultipartFile plantImage,
                    Model model
    ) throws NoSuchGardenException {
        logger.info("POST {}", newPlantUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        Map<String, String> errors = ErrorChecker.plantFormErrors(
                        plantName,
                        plantCount,
                        plantDescription,
                        plantImage
        );

        LocalDate date = parseDate(plantedDate, errors);
        String imageFileName = saveImage(plantImage, errors);

        if (!errors.isEmpty()) {
            return loadPlantForm(
                            errors.getOrDefault("plantNameError", ""),
                            errors.getOrDefault("plantCountError", ""),
                            errors.getOrDefault("plantDescriptionError", ""),
                            errors.getOrDefault("plantedDateError", ""),
                            errors.getOrDefault("imageTypeError", ""),
                            errors.getOrDefault("imageSizeError", ""),
                            errors.getOrDefault("plantImageUploadError", ""),
                            plantName,
                            plantCount,
                            plantDescription,
                            date,
                            "/images/default-plant.jpg",
                            garden.getName(),
                            newPlantUri(gardenId),
                            viewGardenUri(gardenId),
                            model
            );
        }
        Plant plant = new Plant(plantName, plantCount, plantDescription, date, imageFileName, garden);
        plantService.savePlant(plant);
        return "redirect:" + viewGardenUri(garden.getId());
    }

    /**
     * Submits form and saves the plant to the database.
     *
     * @param plantId          The id of the plant being submitted.
     * @param plantName        The name of the plant as input by the user.
     * @param plantCount       The number of plants as input by the user.
     * @param plantDescription The description of the plant as input by the user.
     * @param plantedDate      The date the plant was planted as input by the user.
     *                         Must be in ISO format (yyyy-MM-dd).
     * @param plantImage       The image file uploaded by the user.
     * @param gardenId         The id of the garden the plant is in.
     * @param model            object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping(EDIT_PLANT_URI_STRING)
    public String submitPlantEdits(
                    @PathVariable long plantId,
                    @PathVariable long gardenId,
                    @RequestParam String plantName,
                    @RequestParam(required = false) Integer plantCount,
                    @RequestParam(required = false) String plantDescription,
                    @RequestParam(required = false) String plantedDate,
                    @RequestParam(required = false) MultipartFile plantImage,
                    Model model
    ) throws NoSuchPlantException {
        logger.info("POST {}", editPlantUri(gardenId, plantId));

        Optional<Plant> optionalPlant = plantService.getPlantByGardenIdAndPlantId(gardenId, plantId);
        if (optionalPlant.isEmpty()) {
            throw new NoSuchPlantException(
                            "Unable to find plant with id " + plantId + " in garden with id " + gardenId + "."
            );
        }
        Plant plant = optionalPlant.get();

        Map<String, String> errors = ErrorChecker.plantFormErrors(
                        plantName,
                        plantCount,
                        plantDescription,
                        plantImage
        );

        LocalDate date = parseDate(plantedDate, errors);
        String imageFileName = saveImage(plantImage, errors);

        if (!errors.isEmpty()) {
            return loadPlantForm(
                            errors.getOrDefault("plantNameError", ""),
                            errors.getOrDefault("plantCountError", ""),
                            errors.getOrDefault("plantDescriptionError", ""),
                            errors.getOrDefault("plantedDateError", ""),
                            errors.getOrDefault("imageTypeError", ""),
                            errors.getOrDefault("imageSizeError", ""),
                            errors.getOrDefault("plantImageUploadError", ""),
                            plantName,
                            plantCount,
                            plantDescription,
                            date,
                            plant.getImageFilePath(),
                            plant.getGarden().getName(),
                            editPlantUri(gardenId, plantId),
                            viewGardenUri(gardenId),
                            model
            );
        }

        plant.setName(plantName);
        plant.setCount(plantCount);
        plant.setDescription(plantDescription);
        plant.setPlantedOn(date);
        if (imageFileName != null) {
            plant.setImageFileName(imageFileName);
        }
        plantService.savePlant(plant);
        return "redirect:" + viewGardenUri(plant.getGarden().getId());
    }
}
