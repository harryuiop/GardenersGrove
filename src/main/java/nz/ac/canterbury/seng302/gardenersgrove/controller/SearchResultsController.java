package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.friends.SearchedUserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.sendFriendRequestUri;

/**
 * Controls the searchResults page for users. This controls what the users see when the user uses the search bar on the
 * friends page.
 */
@Controller
public class SearchResultsController {
    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    /**
     * This sets up the current user service so that the current users can be reached.
     * @param userService the current service being used to get information about the users
     */
    @Autowired
    public SearchResultsController(UserService userService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }

    /**
     * Gets the results from the user search. Returns the display of the search results.
     * @param searchUser The String that the user inputs to look for friends
     * @param model      The Model object used for adding attributes to the view.
     * @return           The searchResults page is viewable to the users
     */
    @GetMapping(SEARCH_RESULTS_STRING)
    public String getSearchResultsPage(@RequestParam String searchUser, Model model) {
        logger.info("Get /search/result/{}", searchUser);
        List<SearchedUserResult> usersFound = userService.getSearchedUserAndFriendStatus(searchUser,
                userService.getAuthenticatedUser(), friendRequestService);
        model.addAttribute("usersFound", usersFound);
        model.addAttribute("sendFriendRequestUri", sendFriendRequestUri());
        model.addAttribute("searchResultsUri", searchResultsUri());
        model.addAttribute("search", searchUser);
        return "searchResults";
    }

    /**
     * Passes the search string through to the search results page
     * @param searchUser            The string sent by the user containing the possible email or full name of the user of interest
     * @param redirectAttributes    Allows attributes to be sent through to get as params
     * @return  a redirect to the get page.
     */
    @PostMapping(SEARCH_RESULTS_STRING)
    public String getSearchResults(@RequestParam String searchUser, RedirectAttributes redirectAttributes) {
        logger.info("POST /search/result/{}", searchUser);
        redirectAttributes.addAttribute("searchUser", searchUser);
        return "redirect:"+SEARCH_RESULTS_STRING;
    }

    /**
     * Incomplete send friend request function.
     * @param Id    The Id of the user the friend request is being sent to.
     * @return      Goes back to the manage friends page.
     */
    @PostMapping(SEND_FREIND_REQUEST_STRING)
    public String sendFriendRequest(@RequestParam String Id) {
        logger.info("User Id: " + Id);
        return "redirect:" + MANAGE_FRIENDS_URI_STRING;
    }
}
