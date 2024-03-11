package nz.ac.canterbury.seng302.gardenersgrove.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class ViewGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);

    private final GardenService gardenService;

    @Autowired
    public ViewGardenController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    /**
     * Redirects GET default url '/' to '/demo'
     *
     * @return redirect to /demo
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

    @PostMapping("/view-garden")
    public String submitForm(@RequestParam(name = "plantImage", required=false) MultipartFile imageFile,
                             @RequestParam(name = "plantId", required=false) Long plantFormId,
                             @RequestParam(name = "gardenId", required = false) Long gardenId,
                             Model model) throws IOException {
        logger.info("POST/ plant image");

        logger.info("Garden id" + gardenId);

        Garden gardn = gardenService.getGardenById(1l).get();
        gardn.getPlants().get(0).setImage(imageFile.getBytes());
        gardenService.saveGarden(gardn);

        this.addViewGardenAttributes(gardenId, model);

        return "viewGarden";
    }


}
