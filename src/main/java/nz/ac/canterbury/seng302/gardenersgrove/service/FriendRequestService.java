package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.annotation.PostConstruct;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for interacting with FriendRequest entity and FriendRequestRepository.
 * Provides methods to add FriendRequests to persistence and retrieve FriendRequests by sender, receiver or ID.
 */
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final UserService userService;

    /**
     * Constructor for Friendship Service.
     *
     * @param friendRequestRepository The FriendRequestRepository instance.
     */
    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void addDefaults() {
        User user1 = userService.getUserById(1);
        User user5 = userService.getUserById(5);
        User user6 = userService.getUserById(6);
        User user7 = userService.getUserById(7);
        User user8 = userService.getUserById(8);
        this.sendFriendRequest(user5, user1);
        this.sendFriendRequest(user6, user1);
        this.sendFriendRequest(user7, user1);
        this.sendFriendRequest(user8, user1);
    }

    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest save = friendRequestRepository.save(new FriendRequest(sender, receiver));
    }

    public List<FriendRequest> findRequestByReceiver(User receiver) {
        return friendRequestRepository.findFriendRequestsByReceiverAndStatus(receiver, Status.PENDING);
    }

    public List<FriendRequest> findRequestBySender(User receiver) {
        return friendRequestRepository.findFriendRequestsBySender(receiver);
    }

    public Optional<FriendRequest> findRequestById(Long id) {
        return friendRequestRepository.findFriendRequestById(id);
    }

    public void updateRequest(FriendRequest request) {
        friendRequestRepository.save(request);
    }

    public void removeAcceptedRequest(FriendRequest request) {
        friendRequestRepository.deleteById(request.getId());
    }

}
