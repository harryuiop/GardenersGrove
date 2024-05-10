package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with FriendRequest entity and FriendRequestRepository.
 * Provides methods to add FriendRequests to persistence and retrieve FriendRequests by sender, receiver or ID.
 */
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    /**
     * Constructor for UserService.
     *
     * @param friendRequestRepository The FriendRequestRepository instance.
     */
    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest save = friendRequestRepository.save(new FriendRequest(sender, receiver));
    }
}
