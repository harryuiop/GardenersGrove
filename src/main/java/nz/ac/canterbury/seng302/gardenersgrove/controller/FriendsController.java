package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.NEW_GARDEN_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewFriendsUri;

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

    @GetMapping(NEW_GARDEN_URI_STRING)
    public String getFriendsPage(Model model) {
        logger.info("GET {}", viewFriendsUri());

        model.addAttribute("user", userService.getAuthenticatedUser());
        return "manageFriends";
    }
}
