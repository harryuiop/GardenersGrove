package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchFriendRequestException;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
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

import java.util.List;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controls the manage friends page which displays current friends and, incoming and outgoing friend requests.
 */
@Controller
public class FriendsController extends NavBar {

    Logger logger = LoggerFactory.getLogger(FriendsController.class);
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    private final List<String> requestActions = List.of("Accept", "Decline", "Cancel");

    private final FriendshipService friendshipService;

    private final GardenService gardenService;

    /**
     * Constructor for controller of manage friends page
     *
     * @param userService          used to get users from ids
     * @param friendRequestService used to change the status of friend requests
     * @param friendshipService    used to adjust friendships between users
     */
    @Autowired
    public FriendsController(
            UserService userService,
            FriendRequestService friendRequestService,
            FriendshipService friendshipService,
            GardenService gardenService
    ) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
        this.gardenService = gardenService;
    }

    /**
     * Fills out the manage friends template before displaying it to the user.
     * @param   model Connects the template to the information
     * @return  manage friends page displayed to user
     */
    @GetMapping(MANAGE_FRIENDS_URI_STRING)
    public String getFriendsPage(Model model) {
        logger.info("GET {}", viewFriendsUri());

        this.updateGardensNavBar(model, gardenService, userService);
        model.addAttribute("user", userService.getAuthenticatedUser());
        model.addAttribute("viewFriendsGardensUriString", VIEW_ALL_FRIENDS_GARDENS_URI_STRING);
        model.addAttribute("manageFriendsUri", MANAGE_FRIENDS_URI_STRING);
        model.addAttribute("friends", friendshipService.getFriends(userService.getAuthenticatedUser()));
        model.addAttribute("incoming", friendRequestService.findRequestByReceiver(userService.getAuthenticatedUser()));
        model.addAttribute("outgoing", friendRequestService.findRequestBySender(userService.getAuthenticatedUser()));
        model.addAttribute("viewAllGardensUri", viewAllGardensUri());
        model.addAttribute("newGardenUri", newGardenUri());
        model.addAttribute("searchResultsUri", searchResultsUri());
        model.addAttribute("search", "");
        return "manageFriends";
    }

    /**
     * Called when a friend request is accepted, declined, canceled or a friend is removed
     * @param action states if there hase been a request accepted, declined or cancelled or otherwise a friend removed
     * @param request is the id of the friend or request being altered
     * @return a redirect to the manage friends page
     * @throws NoSuchFriendRequestException if the request id does not exist
     */
    @PostMapping(MANAGE_FRIENDS_URI_STRING)
    public String submitFriendsPage(@RequestParam String action, @RequestParam Long request) throws NoSuchFriendRequestException {
        logger.info("POST {}", viewFriendsUri());
        if (requestActions.contains(action)) {
            Optional<FriendRequest> optionalFriendRequest = friendRequestService.findRequestById(request);
            if (optionalFriendRequest.isEmpty() && requestActions.contains(action)) {
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
                    friendRequestService.removeRequest(friendRequest);
                    break;
                case "Decline":
                    logger.info("Declined Request");
                    friendRequest.setStatus(Status.DECLINED);
                    friendRequestService.updateRequest(friendRequest);
                    break;
                case "Cancel":
                    logger.info("Canceled Request");
                    friendRequestService.removeRequest(friendRequest);
                default:
                    logger.info("Default in switch statement");
            }
        } else {
            logger.info("Remove Friend");
            User userFriend = userService.getUserById(request);
            friendshipService.removeFriendship(friendshipService.getFriendship(userService.getAuthenticatedUser(), userFriend));
        }
        return "redirect:" + viewFriendsUri();
    }
}
