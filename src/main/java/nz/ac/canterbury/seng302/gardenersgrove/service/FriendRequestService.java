package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendRequestRepository;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.List;
import java.util.Optional;

/**
 * Service class for interacting with FriendRequest entity and FriendRequestRepository.
 * Provides methods to add FriendRequests to persistence and retrieve FriendRequests by sender, receiver or ID.
 */
@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    /**
     * Constructor for Friendship Service.
     *
     * @param friendRequestRepository The FriendRequestRepository instance.
     */
    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    public void sendFriendRequest(User sender, User receiver) {
        friendRequestRepository.save(new FriendRequest(sender, receiver));
    }

    public List<FriendRequest> findRequestByReceiver(User receiver) {
        return friendRequestRepository.findFriendRequestsByReceiverAndStatus(receiver, Status.PENDING);
    }

    public List<FriendRequest> findRequestBySender(User sender) {
        return friendRequestRepository.findFriendRequestsBySender(sender);
    }

    public List<FriendRequest> findRequestBySenderAndReceiver(User sender, User receiver) {
        return friendRequestRepository.findFriendRequestBySenderAndReceiver(sender, receiver);
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
