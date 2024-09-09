package nz.ac.canterbury.seng302.gardenersgrove.repository;

import jakarta.transaction.Transactional;
import nz.ac.canterbury.seng302.gardenersgrove.entity.FriendRequest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.utility.Status;
import org.springframework.data.jpa.repository.Modifying;
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

    // Without the Modifying and Transactional annotation this query will throw a 500 error for accepting requests
    @Modifying
    @Transactional
    @Query("DELETE FROM FriendRequest WHERE id = ?1")
    void deleteById(Long id);
}
