package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class HomeController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final GardenService gardenService;

    @Autowired
    public HomeController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    /**
     * Gets the thymeleaf page representing the /demo page (a basic welcome screen with some links)
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return thymeleaf homeTemplate
     */
    @GetMapping("/")
    public String getTemplate(Model model) {
        logger.info("GET /");
        this.updateGardensSidebar(model, gardenService);
        return "homeTemplate";
    }

}
