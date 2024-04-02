package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for the view garden page. For viewing a specific garden.
 */
@Controller
public class ViewGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);

    private final GardenService gardenService;
    private final PlantService plantService;
    private final UserService userService;

    /**
     * Spring will automatically call this constructor at runtime to inject the dependencies.
     * @param gardenService A Garden database access object.
     * @param plantService A Plant database access object.
     * @param userService A User database access object.
     */
    @Autowired
    public ViewGardenController(GardenService gardenService, PlantService plantService, UserService userService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.userService = userService;
    }

    /**
     * Set up view garden page and display attributes.
     *
     * @return Thyme leaf html template of the view garden page.
     */
    @GetMapping("/view-garden")
    public String home(
                    @RequestParam(name = "gardenId", required = false) Long gardenId,
                    HttpServletRequest request,
                    Model model
    ) {
        logger.info("GET /view-garden");

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            return "redirect:" + request.getHeader("Referer");
        }
        this.addViewGardenAttributes(model, optionalGarden.get());
        return "viewGarden";
    }

    private void savePlantImage(Plant plant, MultipartFile image) {
        try {
            String fileName = ImageStore.storeImage(image);
            plant.setImageFileName(fileName);
            plantService.savePlant(plant);
        } catch (IOException error) {
            logger.error("Error saving plant image", error);
        }
    }

    /**
     * Handles requests to change a plant's image from the view garden page.
     * Changes the plant image and has user feedback for size and type if the new image is invalid.
     *
     * @param imageFile New Image file.
     * @param plantId   the ID of the plant to attach the image to.
     * @param gardenId  The ID of the garden the plant sits within.
     * @param model     object that passes data through to the HTML.
     * @return Thymeleaf HTML template for the view garden page.
     */
    @PostMapping("/view-garden")
    public String submitPlantImage(
                    @RequestParam(name = "plantImage", required = false) MultipartFile imageFile,
                    @RequestParam(name = "gardenId") long gardenId,
                    @RequestParam(name = "plantId") long plantId,
                    HttpServletRequest request,
                    Model model
    ) {
        logger.info("POST/ plant image");

        Optional<Plant> optionalPlant = plantService.getPlantById(plantId);
        if (optionalPlant.isEmpty()) {
            return "redirect:" + request.getHeader("Referer");
        }
        Plant plant = optionalPlant.get();

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            return "redirect:" + request.getHeader("Referer");
        }
        Garden garden = optionalGarden.get();

        ImageValidator imageValidator = new ImageValidator(imageFile);

        if (imageValidator.isValid()) {
            this.savePlantImage(plant, imageFile);
        } else {
            model.addAttribute("selectedPlantId", plantId);
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        this.addViewGardenAttributes(model, garden);
        return "viewGarden";
    }

    private void addViewGardenAttributes(Model model, Garden garden) {
        this.updateGardensSidebar(model, gardenService, userService);

        model.addAttribute("garden", garden);
        model.addAttribute("gardenId", garden.getId());
        model.addAttribute("plants", plantService.getAllPlantsInGarden(garden));
    }
}
