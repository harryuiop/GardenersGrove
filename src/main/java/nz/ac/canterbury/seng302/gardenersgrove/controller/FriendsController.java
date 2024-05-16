package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@Controller
public class FriendsController {

    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    @Autowired
    public FriendsController(UserService userService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }

    @GetMapping(MANAGE_FRIENDS_URI_STRING)
    public String getFriendsPage(Model model) {
        logger.info("GET {}", viewFriendsUri());

        model.addAttribute("user", userService.getAuthenticatedUser());
        model.addAttribute("viewFriendsGardensUriString", VIEW_ALL_FRIENDS_GARDENS_URI_STRING);
        model.addAttribute("searchUsersUri", searchUsersUri());
        model.addAttribute("search", "");
        return "manageFriends";
    }

    /**
     * Handles GET requests to the home (root) URL.
     * Displays a separate page depending on authentication status.
     *
     * @return the landing page if the user is not authenticated, otherwise the home page
     */
    @PostMapping(SEARCH_USERS_STRING)
    public String getSearchedUsers(@RequestParam String searchUser, Model model) {
        List<User> userOptions = userService.getSearchedUser(searchUser);
        logger.info("Users found: " + searchUser);
        model.addAttribute("foundUsers", userOptions);
        model.addAttribute("searchResults", userService.getAuthenticatedUser());
        model.addAttribute("viewFriendsGardensUriString", VIEW_ALL_FRIENDS_GARDENS_URI_STRING);
        model.addAttribute("searchUsersUri", searchUsersUri());
        model.addAttribute("search", "");
        return "manageFriends";
    }
}
