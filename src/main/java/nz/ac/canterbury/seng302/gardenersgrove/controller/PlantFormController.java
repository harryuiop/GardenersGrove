package nz.ac.canterbury.seng302.gardenersgrove.controller;

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

import java.util.Date;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class PlantFormController {
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
        model.addAttribute("plantNameError", "");
        model.addAttribute("plantCountError", "");
        model.addAttribute("plantDescriptionError", "");
        model.addAttribute("plantedDateError", "");
        model.addAttribute("gardenName", gardenService.getGardenById(gardenId).get().getName());
        return "plantForm";
    }

    public boolean checkString(String string) {
        return string.matches("[a-zA-Z0-9 .,\\-']*");
    }

//    public boolean checkDate(String string) { return string.matches("(0[1-9]|[12][0-9]|3[01])(\\/)(0[1-9]|1[1,2])(\\/)(19|20)\\d{2}\n"); }

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
                             @RequestParam(name = "plantedDate", required = false) Date plantedDate,
                             @PathVariable Garden garden,
                             Model model) {
        logger.info("POST /form");
        boolean nameIsValid = false;
        boolean countIsValid = false;
        boolean descriptionIsValid = false;
        boolean dateIsValid = false;

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

//        if (plantedDate != null && !checkDate(plantedDate)) {
//            model.addAttribute("plantedDateError", "Date in not in valid format, DD/MM/YYYY");
//        } else {
//            dateIsValid = true;
//        }

        if (nameIsValid && countIsValid && descriptionIsValid && dateIsValid) {

            //Date date = new Date(Integer.parseInt(plantedDate.split("/")[2]), Integer.parseInt(plantedDate.split("/")[1]), Integer.parseInt(plantedDate.split("/")[0]));
            Plant plant = new Plant(plantName, plantCount, plantDescription, plantedDate);
            plantService.savePlant(plant);
            garden.addPlant(plant);
            return "redirect:/";
        } else {
            model.addAttribute("plantName", plantName);
            model.addAttribute("plantCount", plantCount);
            model.addAttribute("plantDescription", plantDescription);
            model.addAttribute("plantedDate", plantedDate);
            return "plantForm";
        }
    }
}
