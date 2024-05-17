package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.annotation.PostConstruct;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Friendship;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    private UserService userService;

    /**
     * Constructor for Friendship Service.
     *
     * @param friendshipRepository The FriendshipRepository instance.
     */
    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void addDefaults() {
        User user1 = userService.getUserById(1);
        User user2 = userService.getUserById(2);
        User user3 = userService.getUserById(3);
        User user4 = userService.getUserById(4);
        this.addFriend(user1, user2);
        this.addFriend(user1, user3);
        this.addFriend(user1, user4);
    }

    public void addFriend(User friend1, User friend2) {
        Friendship save = friendshipRepository.save(new Friendship(friend1, friend2));
    }

    public List<User> getFriends(User user) {
        Stream<User> friends1 = friendshipRepository.findFriendshipByFriend1(user).stream().map(Friendship::getFriend2);
        Stream<User> friends2 = friendshipRepository.findFriendshipByFriend2(user).stream().map(Friendship::getFriend1);
        return Stream.concat(friends1, friends2).toList();
    }
}
