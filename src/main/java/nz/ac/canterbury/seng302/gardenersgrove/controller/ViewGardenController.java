package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageResults;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidation;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Controller for the view garden page. For viewing a specific garden.
 */
@Controller
public class ViewGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);

    private final GardenService gardenService;
    private final PlantService plantService;

    @Autowired
    public ViewGardenController(GardenService gardenService, PlantService plantService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
    }

    /**
     * Set up view garden page.
     *
     * @return Thyme leaf html template of the view garden page.
     */
    @GetMapping("/view-garden")
    public String home(@RequestParam(name = "gardenId", required = false) Long gardenId, Model model) {
        logger.info("GET /view-garden");
        this.addViewGardenAttributes(gardenId, model);
        return "viewGarden";
    }

    private void addViewGardenAttributes(Long gardenId, Model model) {
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("gardenId", gardenId);
        model.addAttribute("garden", gardenService.getGardenById(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            List<Plant> plants = garden.getPlants();
            model.addAttribute("plants", plants);
        }
    }

    private void savePlantImage(Long plantId, Long gardenId, byte[] imageBytes, String imageType) {
        Plant plant = plantService.getPlantById(plantId).get();
        plant.setImage(imageBytes);
        plant.setImageType(imageType);
        plantService.savePlant(plant);
        Garden garden = gardenService.getGardenById(gardenId).get();
        gardenService.saveGarden(garden);
    }

    /**
     * Gets post mapping for when a new plant image is selected. Changes the plant image and has
     * user feedback for size and type if the new image is invalid.
     *
     * @param imageFile New Image file.
     * @param plantId Plant id of form selected.
     * @param gardenId Garden id according to the query string of the viewed garden.
     * @param model Model.
     * @return Thyme leaf html template of the view garden page.
     * @throws IOException
     */
    @PostMapping("/view-garden")
    public String submitPlantImage(@RequestParam(name = "plantImage", required=false) MultipartFile imageFile,
                                   @RequestParam(name = "gardenId") Long gardenId,
                                   @RequestParam(name = "plantId") Long plantId,
                                   Model model) throws IOException {

        logger.info("POST/ plant image");
        logger.info("Garden id: " + gardenId);
        logger.info("Plant Id: " + plantId);

        ImageValidation imageValidation = new ImageValidation();
        ImageResults imageResults = imageValidation.getImageResults(imageFile);

        if (imageResults.getImageIsValid()) {
            this.savePlantImage(plantId, gardenId, imageResults.getImageBytes(), imageResults.getImageType());
        } else {
            model.addAttribute("selectedPlantId", plantId);
            if (!imageResults.getImageIsValidType()) {
                model.addAttribute("plantImageTypeError", "Image must be of type png, jpg or svg.");
            }

            if (!imageResults.getImageIsValidSize()) {
                model.addAttribute("plantImageSizeError", "Image must be less than 10MB.");
            }
        }

        this.addViewGardenAttributes(gardenId, model);
        return "viewGarden";
    }
}
