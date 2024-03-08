package nz.ac.canterbury.seng302.gardenersgrove.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class ViewGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);

    private final GardenService gardenService;
    private final PlantService plantService;

    /**
     * Temporary function to get plants with garden id
     * @return
     */
    public List<Plant> getPlantsFromGardenId(Long gardenId) {
        List<Plant> allPlants = plantService.getAllPlants();
        ArrayList<Plant> myPlants = new ArrayList<>();
        for (Plant plant : allPlants) {
            if (plant.getGardenId() == gardenId) {
                myPlants.add(plant);
            }
        }
        return myPlants;

    }

    @Autowired
    public ViewGardenController(GardenService gardenService, PlantService plantService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
    }

    /**
     * Redirects GET default url '/' to '/demo'
     *
     * @return redirect to /demo
     */
    @GetMapping("/view-garden")
    public String home(@RequestParam(name = "gardenId", required = false) Long gardenId, Model model) {
        logger.info("GET /view-garden");
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("garden", gardenService.getGardenById(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            List<Plant> plants = getPlantsFromGardenId(gardenId);
            logger.info("plants" + plants);
            ArrayList<String> plantImages = new ArrayList<>();
            for (Plant plant : plants) {
                byte[] plantBytes = plant.getImage();
                String base64Image = Base64.getEncoder().encodeToString(plantBytes);
                plantImages.add("data:image/;base64," + base64Image);
            }
            model.addAttribute("plants", plants);
        }
        return "viewGarden";
    }


}
