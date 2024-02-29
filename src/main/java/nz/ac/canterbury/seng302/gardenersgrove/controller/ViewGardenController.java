package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class ViewGardenController {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);

    private final GardenService gardenService;

    @Autowired
    public ViewGardenController(GardenService gardenService) {
        this.gardenService = gardenService;
        this.gardenService.saveGarden((new Garden("Test Garden", "Location", 100)));
    }

    /**
     * Redirects GET default url '/' to '/demo'
     *
     * @return redirect to /demo
     */
    @GetMapping("/view-garden")
    public String home(Model model) {
        logger.info("GET /view-garden");
        this.viewSpecificGarden(model);
        return "viewGarden";
    }

    /**
     * Gets all Garden responses
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     */
    public void viewSpecificGarden(Model model) {
        logger.info("GET /view-garden viewing % Garden");
        model.addAttribute("gardens", this.gardenService.getAllGardens());
    }


}
