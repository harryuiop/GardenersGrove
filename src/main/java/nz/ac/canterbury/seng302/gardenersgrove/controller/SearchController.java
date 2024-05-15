package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the root/home page. This controls what each user will see client side
 * depending on authentication status and permissions.
 */
@Controller
public class SearchController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final UserService userService;

    /**
     * The HomeController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param userService   The User database access object.
     */
    @Autowired
    public SearchController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles GET requests to the home (root) URL.
     * Displays a separate page depending on authentication status.
     *
     * @return the landing page if the user is not authenticated, otherwise the home page
     */
    @GetMapping(SEARCH_URI_STRING)
    public List<User> getSearchedUsers(@RequestParam String search) {
        List<User> userOptions = userService.getSearchedUser(search);
        logger.info("Users found: " + search);
        return userOptions;
    }
}
