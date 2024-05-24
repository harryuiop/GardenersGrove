package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
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

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controls the manage friends page which displays current friends and, incoming and outgoing friend requests.
 */
@Controller
public class FriendsController {

    Logger logger = LoggerFactory.getLogger(FriendsController.class);
    private final UserService userService;
    private final FriendRequestService friendRequestService;

    /**
     * Sets up services.
     * @param userService           Allows access to users information
     * @param friendRequestService  Allows access to current friend requests
     */
    @Autowired
    public FriendsController(UserService userService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
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
        model.addAttribute("searchResultsUri", searchResultsUri());
        model.addAttribute("search", "");
        return "manageFriends";
    }

    /**
     * Used to accept and decline friend requests
     * @param action                    determines whether to accept or decline the friend request
     * @param request                   the Id of the friend requests source user
     * @return                          redirects back to the manage friends page
     * @throws NoSuchGardenException    If the user Id cannot find a corresponding user throws this error
     */
    @PostMapping(MANAGE_FRIENDS_URI_STRING)
    public String submitFriendsPage(@RequestParam String action, @RequestParam Long request) throws NoSuchGardenException {
        logger.info("POST {}", viewFriendsUri());
        logger.info(request.toString());
        FriendRequest friendRequest = friendRequestService.findRequestById(request).isPresent() ? friendRequestService.findRequestById(request).get() : null;
        if (friendRequest == null) {
            logger.error("No such request id");
            throw new NoSuchGardenException(request);
        }
        User receiver = friendRequest.getReceiver();
        User sender = friendRequest.getSender();
        switch (action) {
            case "Accept":
                logger.info("Accepted Request");
                receiver.addFriend(sender);
                sender.addFriend(receiver);
                userService.updateUser(receiver);
                userService.updateUser(sender);
                friendRequest.setStatus(Status.ACCEPTED);
                friendRequestService.updateRequest(friendRequest);
                break;
            case "Decline":
                logger.info("Declined Request");
                friendRequest.setStatus(Status.DECLINED);
                friendRequestService.updateRequest(friendRequest);
                break;
            default:
                logger.info("Default in switch statement");
        }

        return "redirect:"+MANAGE_FRIENDS_URI_STRING;
    }
}
