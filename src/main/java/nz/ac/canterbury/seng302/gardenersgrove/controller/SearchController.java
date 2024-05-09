package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the root/home page. This controls what each user will see client side
 * depending on authentication status and permissions.
 */
@Controller
public class SearchController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final GardenService gardenService;
    private final UserService userService;

    /**
     * The HomeController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public SearchController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to the home (root) URL.
     * Displays a separate page depending on authentication status.
     *
     * @return the landing page if the user is not authenticated, otherwise the home page
     */
    @GetMapping(SEARCH_URI_STRING)
    public String getSearchPage(Model model) {
        logger.info("GET {}", homeUri());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Spring security allows requests to the root URI for unauthenticated users.
        // We must check to see if the principle is authenticated to determine which page to display.
        this.updateGardensSidebar(model, gardenService, userService);

        model.addAttribute("searchUri", searchUri());

        return "searchFriends";
    }

    @PostMapping(SEARCH_URI_STRING)
    public String getSearchedUsers(@RequestParam String search, Model model) {
        model.addAttribute("searchUri", searchUri());
        model.addAttribute("users", userService.getSearchedUser(search));
        model.addAttribute("user", userService.getAuthenticatedUser());
        return "searchFriends";
    }

}
