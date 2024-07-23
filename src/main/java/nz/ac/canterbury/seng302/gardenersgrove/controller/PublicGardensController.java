package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@Controller
public class PublicGardensController {
    Logger logger = LoggerFactory.getLogger(PublicGardensController.class);
    private final GardenService gardenService;

    @Autowired
    PublicGardensController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    @GetMapping(BROWSE_PUBLIC_GARDENS_URI_STRING)
    String browseGardens(Model model) {
        logger.info("GET {}", browsePublicGardensUri());
        model.addAttribute("gardens", gardenService.getAllPublicGardens());
        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);
        return "publicGardens";
    }
}
