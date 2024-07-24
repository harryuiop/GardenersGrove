package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    String browseGardens(HttpServletRequest request, Model model) {
        logger.info("GET {}", browsePublicGardensUri());

        List<Garden> gardenList = gardenService.getAllPublicGardens();
        model.addAttribute("gardenList", gardenList);

        // Current page number is passed through the url anchor
        String[] urlParts = request.getRequestURL().toString().split("#");
        int currentPage = urlParts.length > 1 ? Integer.parseInt(urlParts[1]) : 1;
        model.addAttribute("currentPage", currentPage);

        int numberOfPages = (int) Math.min(5, Math.ceil((double) gardenList.size() / 10));
        model.addAttribute("pageNumbers", IntStream.range(1, numberOfPages + 1).toArray());

        model.addAttribute("viewGardenUriString", VIEW_GARDEN_URI_STRING);
        this.updateGardensNavBar(model, gardenService, userService);
        return "publicGardens";
    }
}
