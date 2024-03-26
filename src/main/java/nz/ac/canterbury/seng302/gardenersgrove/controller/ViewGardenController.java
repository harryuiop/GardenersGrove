package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    public ViewGardenController(GardenService gardenService, PlantService plantService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
    }

    /**
     * Set up view garden page and display attributes.
     *
     * @return Thyme leaf html template of the view garden page.
     */
    @GetMapping("/view-garden")
    public String home(@RequestParam(name = "gardenId", required = false) Long gardenId, Model model) {
        logger.info("GET /view-garden");
        this.addViewGardenAttributes(model, gardenId);
        return "viewGarden";
    }

    private void savePlantImage(Long plantId, MultipartFile image) {
        Optional<Plant> optionalPlant = plantService.getPlantById(plantId);
        if (optionalPlant.isEmpty()) {
            return;
        }
        Plant plant = optionalPlant.get();
        String fileName;
        try {
            fileName = ImageStore.storeImage(image);
        } catch (IOException error) {
            logger.error("Error saving plant image", error);
            return;
        }
        plant.setImageFileName(fileName);
        plantService.savePlant(plant);
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
     */
    @PostMapping("/view-garden")
    public String submitPlantImage(@RequestParam(name = "plantImage", required=false) MultipartFile imageFile,
                                   @RequestParam(name = "gardenId") Long gardenId,
                                   @RequestParam(name = "plantId") Long plantId,
                                   Model model) {
        logger.info("POST/ plant image");

        ImageValidator imageValidator = new ImageValidator(imageFile);

        if (imageValidator.isValid()) {
            this.savePlantImage(plantId, imageFile);
        } else {
            model.addAttribute("selectedPlantId", plantId);
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        this.addViewGardenAttributes(model, gardenId);
        return "viewGarden";
    }

    private void addViewGardenAttributes(Model model, Long gardenId) {
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("garden", gardenService.getGardenById(gardenId));
        model.addAttribute("gardenId", gardenId);
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            List<Plant> plants = garden.getPlants();
            model.addAttribute("plants", plants);
        }
    }
}
