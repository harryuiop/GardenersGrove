package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Friendship;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    List<Friendship> findFriendshipByFriend1(User friend);

    List<Friendship> findFriendshipByFriend2(User friend);

    Optional<Friendship> findFriendshipByFriend1AndFriend2(User friend1, User friend2);

}
