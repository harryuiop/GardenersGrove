package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.sendFriendRequestUri;

/**
 * Controls the searchResults page for users. This controls what the users see when the user uses the search bar on the
 * friends page.
 */
@Controller
public class SearchResultsController {
    Logger logger = LoggerFactory.getLogger(SearchResultsController.class);
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    private final FriendshipService friendshipService;

    /**
     * This sets up the current user service so that the current users can be reached.
     * @param userService the current service being used to get information about the users
     */
    @Autowired
    public SearchResultsController(UserService userService, FriendRequestService friendRequestService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
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
                userService.getAuthenticatedUser(), friendRequestService, friendshipService);
        model.addAttribute("usersFound", usersFound);
        model.addAttribute("sendFriendRequestUri", sendFriendRequestUri());
        model.addAttribute("searchResultsUri", searchResultsUri());
        model.addAttribute("search", searchUser);
        Map<Long, Long> incomingRequests = new HashMap<>();
        for (FriendRequest request : friendRequestService.findRequestByReceiver(userService.getAuthenticatedUser())) {
            incomingRequests.put(request.getSender().getId(), request.getId());
        }
        model.addAttribute("incomingRequests", incomingRequests);
        model.addAttribute("manageFriendsUri", MANAGE_FRIENDS_URI_STRING);
        return "searchResults";
    }

    /**
     * Passes the search string through to the search results page
     * @param searchUser            The string sent by the user containing the possible email or full name of the user of interest
     * @param redirectAttributes    Allows attributes to be sent through to get as params
     * @return  a redirect to the get page.
     */
    @PostMapping(SEARCH_RESULTS_STRING)
    public String getSearchResults(@RequestParam String searchUser,
                                   RedirectAttributes redirectAttributes) {
        String trimmedSearchUser = searchUser.trim();
        logger.info("POST /search/result/{}", trimmedSearchUser);
        redirectAttributes.addAttribute("searchUser", trimmedSearchUser);
        return "redirect:"+SEARCH_RESULTS_STRING;
    }

    /**
     * Incomplete send friend request function.
     * @param userId    The id of the user the friend request is being sent to.
     * @return      Redirection to friends page.
     */
    @PostMapping(SEND_FREIND_REQUEST_STRING)
    public String sendFriendRequest(@RequestParam long userId) {
        logger.info("POST /search/send/{}", userId);
        User loggedInUser = userService.getAuthenticatedUser();
        User sentUser = userService.getUserById(userId);
        friendRequestService.sendFriendRequest(loggedInUser, sentUser);
        return "redirect:" + MANAGE_FRIENDS_URI_STRING;
    }
}
