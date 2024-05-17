package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendshipRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class GardenServiceTest {

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;
    @Autowired
    private FriendshipRepository friendshipRepository;
    private FriendshipService friendshipService;
    private User user;

    @Test
    void gardenRepositoryGardenCreation() {
        userService = new UserService(userRepository);
        friendshipService = new FriendshipService(friendshipRepository, userService);
        if (user == null) {
            user = new User(
                    "test@domain.net",
                    "Test",
                    "User",
                    "Password1!",
                    "2000-01-01"
            );
            userService.addUsers(user);
        }
        GardenService gardenService = new GardenService(gardenRepository, userService, friendshipService);
        Location location = new Location("New Zealand", "Christchurch");
        Garden garden = gardenService.saveGarden(new Garden(user, "Test Garden", location, 100f));
        List<Garden> gardens = gardenRepository.findAllByOwner(user);
        Garden outputGarden = gardens.get(0);

        Assertions.assertEquals(1, gardens.size());
        Assertions.assertEquals(garden.getId(), outputGarden.getId());
        Assertions.assertEquals("Test Garden", outputGarden.getName());
        Assertions.assertEquals(location.toString(), outputGarden.getLocation().toString());
        Assertions.assertEquals(100, outputGarden.getSize());
    }
}
