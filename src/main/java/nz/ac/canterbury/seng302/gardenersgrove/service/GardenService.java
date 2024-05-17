package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.annotation.PostConstruct;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
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

    @PostConstruct
    public void addDefaults() {
        User user1 = userService.getUserById(1);
        User user2 = userService.getUserById(2);
        this.saveGarden(new Garden(user1, "Garden 1", null, new Location("New Zealand", "Christchurch"), 1F));
        this.saveGarden(new Garden(user2, "Garden 2", null, new Location("New Zealand", "Christchurch"), 2F));
    }

    public List<Garden> getAllGardens(UserService userService) {
        return gardenRepository.findAllByOwner(userService.getAuthenticatedUser());
    }

    public List<Garden> getAllFriendsGardens(long friendId, UserService userService) throws NoSuchGardenException {
        if (friendshipService.getFriends(userService.getAuthenticatedUser()).contains(userService.getUserById(friendId))) {
            return gardenRepository.findAllByOwner(userService.getUserById(friendId));
        } else {
            throw new NoSuchGardenException();
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
}