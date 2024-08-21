package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    List<FriendRequest> findFriendRequestsBySender(User sender);

    @Query("SELECT friendRequest FROM FriendRequest friendRequest WHERE friendRequest.status != 3 AND friendRequest.sender = ?1")
    List<FriendRequest> findOutgoingRequests(User sender);

    List<FriendRequest> findFriendRequestBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findFriendRequestsByReceiverAndStatus(User receiver, Status status);

    Optional<FriendRequest> findFriendRequestById(Long requestId);

    void deleteById(Long id);

}
