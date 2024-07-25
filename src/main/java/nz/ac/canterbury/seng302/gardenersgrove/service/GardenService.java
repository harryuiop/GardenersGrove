package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchFriendException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GardenService {

    private final GardenRepository gardenRepository;

    private final UserService userService;
    private final FriendshipService friendshipService;

    @Autowired
    public GardenService(GardenRepository gardenRepository, UserService userService, FriendshipService friendshipService) {
        this.gardenRepository = gardenRepository;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    public List<Garden> getAllGardens(UserService userService) {
        return gardenRepository.findAllByOwner(userService.getAuthenticatedUser());
    }

    public List<Garden> getAllFriendsGardens(long friendId, UserService userService) throws NoSuchFriendException {
        if (friendshipService.getFriends(userService.getAuthenticatedUser()).contains(userService.getUserById(friendId))) {
            return gardenRepository.findAllByOwner(userService.getUserById(friendId));
        } else {
            throw new NoSuchFriendException(friendId);
        }
    }
    public Optional<Garden> getGardenById(Long id) {
        return gardenRepository.findById(id);
    }

    public Garden saveGarden(Garden garden) {
        return gardenRepository.save(garden);
    }

    public void deleteGarden(Long id) {
        gardenRepository.deleteById(id);
    }

    public List<Garden> getPageOfPublicGardens(int pageNumber) {
        return gardenRepository.findByGardenPublicTrue((pageNumber - 1) * 10);
    }

    public long countPublicGardens() {
        return gardenRepository.countByIsGardenPublicTrue();
    }
}