package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
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
import java.util.*;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class PlantFormController {
    Logger logger = LoggerFactory.getLogger(PlantFormController.class);

    private final PlantService plantService;

    private final int MAX_IMAGE_SIZE_KB = 10000;

    private final int BYTES_IN_KBS = 1024;

    private final List<String> requiredImageTypes = Arrays.asList("jpg", "png", "svg");

    @Autowired
    public PlantFormController(PlantService plantService) {
        this.plantService = plantService;
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/plantform")
    public String form(Model model) {
        logger.info("GET /plantform");
        model.addAttribute("plantNameError", "");
        model.addAttribute("plantCountError", "");
        model.addAttribute("plantDescriptionError", "");
        model.addAttribute("plantedDateError", "");
        model.addAttribute("plantImage", "");
        return "plantForm";
    }

    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

    public boolean checkDate(String string) { return string.matches("(0[1-9]|[12][0-9]|3[01])(\\/)(0[1-9]|1[1,2])(\\/)(19|20)\\d{2}\n"); }

    public boolean checkImageType(String fileName) {
        for (String requiredType : requiredImageTypes) {
            if (fileName.endsWith(requiredType)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkImageSize(byte[] imageBytes) {
        logger.info("Image KB" + imageBytes.length / BYTES_IN_KBS);
        return imageBytes.length / BYTES_IN_KBS <= MAX_IMAGE_SIZE_KB;
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
    @PostMapping("/form")
    public String submitForm(@RequestParam(name = "plantName") String plantName,
                             @RequestParam(name = "plantCount", required = false) Integer plantCount,
                             @RequestParam(name = "plantDescription", required = false) String plantDescription,
                             @RequestParam(name = "plantedDate", required = false) String plantedDate,
                             @RequestParam(name = "plantImage") MultipartFile imageFile,
                             Model model) throws IOException {
        logger.info("POST /form");
        boolean nameIsValid = false;
        boolean countIsValid = false;
        boolean descriptionIsValid = false;
        boolean imageIsValid = false;
        boolean imageIsValidType = false;
        boolean imageIsValidSize = false;
        byte[] imageBytes = new byte[0];

        if (imageFile.isEmpty()) {
            imageIsValid = true;
        } else {
            imageBytes = imageFile.getBytes(); // Convert MultipartFile to byte[] to be saved in database
            imageIsValidType = checkImageType(imageFile.getOriginalFilename());
            imageIsValidSize = checkImageSize(imageFile.getBytes());
            imageIsValid = imageIsValidType && imageIsValidSize;
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


        if (!imageIsValidType) {
            model.addAttribute("plantImageTypeError", "Image must be of type png, jpg or svg.");
        }

        if (!imageIsValidSize) {
            model.addAttribute("plantImageSizeError", "Image must be less than 10MB.");
        }

        logger.info("Type, Size: ", imageIsValidType + ", " + imageIsValidSize);


        Date date = new Date();
        boolean dateIsValid = true;


        if (nameIsValid && countIsValid && descriptionIsValid && dateIsValid && imageIsValid) {
            Plant plant = new Plant(plantName, plantCount, plantDescription, date, imageBytes);
            plantService.savePlant(plant);
            logger.info("Plant Saved: ");

            return "redirect:/demo?plantId=" + plant.getId();
        } else {
            model.addAttribute("plantName", plantName);
            model.addAttribute("plantCount", plantCount);
            model.addAttribute("plantDescription", plantDescription);
            model.addAttribute("plantedDate", plantedDate);
            return "plantForm";
        }
    }
}
