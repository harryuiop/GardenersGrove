package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.SEARCH_USERS_STRING;

@RestController
@RequestMapping(SEARCH_USERS_STRING)
public class SearchUsers {
    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final UserService userService;

    @Autowired
    public SearchUsers(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles POST requests to the home (root) URL.
     * Displays a separate page depending on authentication status.
     *
     * @return the landing page if the user is not authenticated, otherwise the home page
     */
    @GetMapping("")
    public List<String> getSearchedUsers(@RequestParam String searchUser) {
        logger.info("Users found: " + searchUser);
        return userService.getSearchedUser(searchUser);
    }
}
