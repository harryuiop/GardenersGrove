package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchFriendRequestException;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controls the manage friends page which displays current friends and, incoming and outgoing friend requests.
 */
@Controller
public class FriendsController extends GardensSidebar {

    Logger logger = LoggerFactory.getLogger(FriendsController.class);
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    private FriendshipService friendshipService;

    /**
     * Constructor for controller of manage friends page
     *
     * @param userService          used to get users from ids
     * @param friendRequestService used to change the status of friend requests
     * @param friendshipService    used to adjust friendships between users
     */
    @Autowired
    public FriendsController(UserService userService, FriendRequestService friendRequestService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
    }

    /**
     * Fills out the manage friends template before displaying it to the user.
     * @param   model Connects the template to the information
     * @return  manage friends page displayed to user
     */
    @GetMapping(MANAGE_FRIENDS_URI_STRING)
    public String getFriendsPage(Model model) {
        logger.info("GET {}", viewFriendsUri());

        model.addAttribute("user", userService.getAuthenticatedUser());
        model.addAttribute("viewFriendsGardensUriString", VIEW_ALL_FRIENDS_GARDENS_URI_STRING);
        model.addAttribute("manageFriendsUri", MANAGE_FRIENDS_URI_STRING);
        model.addAttribute("requestService", friendRequestService);
        model.addAttribute("friendshipService", friendshipService);
        model.addAttribute("viewAllGardensUri", viewAllGardensUri());
        model.addAttribute("newGardenUri", newGardenUri());
        model.addAttribute("searchResultsUri", searchResultsUri());
        model.addAttribute("search", "");
        return "manageFriends";
    }

    /**
     * Called when a friend request is accepted, declined, canceled or a friend is removed
     * @param model to pass attributes to the html page
     * @param action states if there hase been a request accepted, declined or cancelled or otherwise a friend removed
     * @param request is the id of the friend or request being altered
     * @return a redirect to the manage friends page
     * @throws NoSuchFriendRequestException
     */
    @PostMapping(MANAGE_FRIENDS_URI_STRING)
    public String submitFriendsPage(Model model, @RequestParam String action, @RequestParam Long request) throws NoSuchFriendRequestException {
        logger.info("POST {}", viewFriendsUri());
        logger.info(request.toString());
        Optional<FriendRequest> optionalFriendRequest = friendRequestService.findRequestById(request);
        if (optionalFriendRequest.isEmpty()) {
            logger.error("No such request id");
            throw new NoSuchFriendRequestException();
        }
        FriendRequest friendRequest = optionalFriendRequest.get();
        User receiver = friendRequest.getReceiver();
        User sender = friendRequest.getSender();
        switch (action) {
            case "Accept":
                logger.info("Accepted Request");
                friendshipService.addFriend(sender, receiver);
                friendRequestService.removeAcceptedRequest(friendRequest);
                break;
            case "Decline":
                logger.info("Declined Request");
                friendRequest.setStatus(Status.DECLINED);
                friendRequestService.updateRequest(friendRequest);
                break;
            default:
                logger.info("Default in switch statement");
        }
        return "redirect:" + viewFriendsUri();
    }
}
