package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchFriendRequestException;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Friendship;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    /**
     * Constructor for Friendship Service.
     * Should never be called directly as it is managed by Spring.
     *
     * @param friendshipRepository The FriendshipRepository instance.
     */
    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Adds a friendship between two users.
     *
     * @param friend1 The first user.
     * @param friend2 The second user.
     */
    public void addFriend(User friend1, User friend2) {
        friendshipRepository.save(new Friendship(friend1, friend2));
    }

    /**
     * Get all friends of a user.
     *
     * @param user The user to get friends of.
     * @return A list of all friends of the user.
     */
    public List<User> getFriends(User user) {
        Stream<User> friends1 = friendshipRepository.findFriendshipByFriend1(user).stream().map(Friendship::getFriend2);
        Stream<User> friends2 = friendshipRepository.findFriendshipByFriend2(user).stream().map(Friendship::getFriend1);
        return Stream.concat(friends1, friends2).toList();
    }

    /**
     * Get the friendship entity between two users.
     *
     * @param user1 The first user.
     * @param user2 The second user.
     * @return The friendship entity between the two users.
     * @throws NoSuchFriendRequestException If there is no friendship between the two users.
     */
    public Friendship getFriendship(User user1, User user2) throws NoSuchFriendRequestException {
        Optional<Friendship> friendship1 = friendshipRepository.findFriendshipByFriend1AndFriend2(user1, user2);
        Optional<Friendship> friendship2 = friendshipRepository.findFriendshipByFriend1AndFriend2(user2, user1);

        if (friendship1.isPresent()) {
            return friendship1.get();
        } else if (friendship2.isPresent()) {
            return friendship2.get();
        } else {
            throw new NoSuchFriendRequestException();
        }
    }

    /**
     * Checks if two users are friends.
     *
     * @param user1 The first user.
     * @param user2 The second user.
     * @return True if the users are friends, false otherwise.
     */
    public boolean areFriends(User user1, User user2) {
        try {
            getFriendship(user1, user2);
            return true;
        } catch (NoSuchFriendRequestException e) {
            return false;
        }
    }

    /**
     * Removes a friendship between two users.
     *
     * @param friendship The friendship to remove.
     */
    public void removeFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }
}
