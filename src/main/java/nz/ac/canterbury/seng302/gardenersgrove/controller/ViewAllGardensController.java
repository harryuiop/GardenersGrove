package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
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
public class ViewAllGardensController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewAllGardensController.class);

    private final GardenService gardenService;
    private final UserService userService;

    @Autowired
    public ViewAllGardensController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * @return redirect to /view-all
     */
    @GetMapping("/view-all")
    public String home(@RequestParam(name = "gardenId", required = false) Long gardenId, Model model) {
        logger.info("GET /view-all");
        model.addAttribute("gardens", gardenService.getAllGardens());
        this.updateGardensSidebar(model, gardenService, userService);
        return "allGardens";
    }


}
