package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.SEARCH_USERS_STRING;

@RestController
public class SearchUsersController {
    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final UserService userService;

    @Autowired
    public SearchUsersController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles POST requests to the home (root) URL.
     * Displays a separate page depending on authentication status.
     *
     * @return the landing page if the user is not authenticated, otherwise the home page
     */
    @GetMapping(SEARCH_USERS_STRING)
    public List<Map<String, String>> getSearchedUsers(@RequestParam String searchUser) {
        return userService.getSearchedUser(searchUser);
    }

    @GetMapping("/send/request")
    public void sendFriendRequest(@RequestParam String email) {
        logger.info(email);
    }

}
