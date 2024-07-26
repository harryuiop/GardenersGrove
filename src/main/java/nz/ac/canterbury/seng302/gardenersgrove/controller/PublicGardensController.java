package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.IntStream;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@Controller
public class PublicGardensController extends NavBar {
    Logger logger = LoggerFactory.getLogger(PublicGardensController.class);
    private final GardenService gardenService;
    private final UserService userService;

    @Autowired
    PublicGardensController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    @GetMapping(BROWSE_PUBLIC_GARDENS_URI_STRING)
    String browseGardens(@RequestParam(required = false) Integer page, Model model) {
        logger.info("GET {}", browsePublicGardensUri());

        if (page == null) {
            page = 1;
        }
        model.addAttribute("currentPage", page);

        List<Garden> gardenList = gardenService.getPageOfPublicGardens(page);
        model.addAttribute("gardenList", gardenList);

        long numberOfGardens = gardenService.countPublicGardens();

        int numberOfPages = (int) Math.min(5, Math.ceil((double) numberOfGardens / 10));
        model.addAttribute("numberOfPages", numberOfPages);
        model.addAttribute("pageNumbers", IntStream.range(1, numberOfPages + 1).toArray());

        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);
        model.addAttribute("browsePublicGardensUriString", BROWSE_PUBLIC_GARDENS_URI_STRING);
        this.updateGardensNavBar(model, gardenService, userService);
        return "publicGardens";
    }
}
