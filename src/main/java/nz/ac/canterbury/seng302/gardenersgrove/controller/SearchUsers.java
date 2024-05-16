package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.SEARCH_USERS_STRING;

@RestController
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
    @GetMapping(SEARCH_USERS_STRING)
    public List<Map<String, String>> getSearchedUsers(@RequestParam String searchUser) {
        return userService.getSearchedUser(searchUser);
    }

    @GetMapping(SEARCH_USERS_STRING + "/send")
    public String sendUsers(@RequestParam String userEmail) {
        logger.info("Users found: " + userEmail);
        return "home";
    }

    @GetMapping("/send/request")
    public void sendFriendRequest(@RequestParam String email) {
        logger.info(email);
    }

    @GetMapping("/user/name")
    public List<String> getUserName(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        List<String> userNames = new ArrayList<>();
        if (user != null) {
            userNames.add(user.getName());
        }
        return userNames;
    }
}
