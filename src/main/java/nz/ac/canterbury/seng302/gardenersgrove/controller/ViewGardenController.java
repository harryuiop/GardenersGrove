package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
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
        this.updateGardensSidebar(model, gardenService);
        model.addAttribute("garden", gardenService.getGardenById(gardenId));
        return "viewGarden";
    }


}
