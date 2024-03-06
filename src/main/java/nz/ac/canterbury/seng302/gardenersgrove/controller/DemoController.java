package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;
import java.util.Optional;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class DemoController extends GardensSidebar {
    private final GardenService gardenService;
    private final PlantService plantService;
    Logger logger = LoggerFactory.getLogger(DemoController.class);


    public DemoController(GardenService gardenService, PlantService plantService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
    }
    /**
    /** Unused here for informational purposes
     * Redirects GET default url '/' to '/demo'
     * @return redirect to /demo
    @GetMapping("/demo")
    public String home() {
        logger.info("GET /");
        return "redirect:./demo";
    }
    */
    /**
     * Gets the thymeleaf page representing the /demo page (a basic welcome screen with some links)
     * @param name url query parameter of user's name
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return thymeleaf demoTemplate
     */
    @GetMapping("/demo")
    public String getTemplate(@RequestParam(name = "plantId", required = false) Long plantId, Model model) {
        logger.info("GET /demo");
        Optional<Plant> plant = plantService.getPlantById(plantId);
        if (plant.isPresent()) {
            byte[] plantBytes = plant.get().getImage();
            String base64Image = Base64.getEncoder().encodeToString(plantBytes);
            String plantImage = "data:image/jpeg;base64," + base64Image;
            logger.info("Plant Image length" + plantImage.length());
            model.addAttribute("plantImage", plantImage);
        }

        return "demoTemplate";
    }

}
