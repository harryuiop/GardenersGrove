package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.sendFriendRequestUri;

@Controller
public class SearchResultsController {
    SearchUsersController searchUsersController;
    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final UserService userService;

    @Autowired
    public SearchResultsController(UserService userService) {
        this.userService = userService;
        searchUsersController = new SearchUsersController(userService);
    }

    @GetMapping(SEARCH_RESULTS_STRING)
    public String getSearchResultsPage(@RequestParam String searchUser, Model model) {
        logger.info("Get /search/result/{}", searchUser);
        List<Map<String, String>> usersFound = searchUsersController.getSearchedUsers(searchUser);
        List<User> displayUsers = new ArrayList<>();
        for (Map<String,String> mappedUser : usersFound) {
            displayUsers.add(userService.getUserByEmail(mappedUser.get("email")));
        }
        model.addAttribute("usersFound", displayUsers);
        model.addAttribute("sendFriendRequestUri", sendFriendRequestUri());
        model.addAttribute("search", searchUser);
        return "searchResults";
    }

    @PostMapping(SEARCH_RESULTS_STRING)
    public String getSearchResults(@RequestParam String searchUser, RedirectAttributes redirectAttributes) {
        logger.info("POST /search/result/{}", searchUser);
        redirectAttributes.addAttribute("searchUser", searchUser);
        return "redirect:"+SEARCH_RESULTS_STRING;
    }

    @PostMapping(SEND_FREIND_REQUEST_STRING)
    public String sendFriendRequest(@RequestParam String Id) {
        logger.info("User Id: " + Id);
        return "redirect:" + MANAGE_FRIENDS_URI_STRING;
    }
}
