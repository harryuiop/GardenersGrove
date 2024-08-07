package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FriendshipRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class PlantServiceTest {
    @Autowired
    private PlantRepository plantRepository;
    private PlantService plantService;

    @Autowired
    private GardenRepository gardenRepository;
    private GardenService gardenService;

    @Autowired
    private FriendshipRepository friendshipRepository;
    private FriendshipService friendshipService;

    @Autowired
    private UserRepository userRepository;
    private UserService userService;
    private Garden garden;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        friendshipService = new FriendshipService(friendshipRepository);
        gardenService = new GardenService(gardenRepository, userService, friendshipService);
        plantService = new PlantService(plantRepository, userService, gardenService);
        if (user == null) {
            user = new User(
                    "test@domain.net",
                    "Test",
                    "User",
                    "Password1!",
                    "2000-01-01"
            );
            userRepository.save(user);
        }
        gardenRepository.deleteAll();
        this.garden = new Garden(user, "Test Garden", null, new Location("New Zealand", "Christchurch"),
                null, true);
        gardenRepository.save(this.garden);
    }

    @Test
    void getAllPlantsInGarden_noPlants_emptyList() {
        List<Plant> plants = plantService.getAllPlantsInGarden(this.garden);
        Assertions.assertEquals(0, plants.size());
    }

    @Test
    void getAllPlantsInGarden_onlyOneGarden_listHasOnePlant() {
        Plant plant = new Plant(
                        "Test Plant",
                        null,
                        null,
                        null,
                        null,
                        this.garden
        );
        plantService.savePlant(plant);

        List<Plant> plants = plantService.getAllPlantsInGarden(this.garden);
        Assertions.assertEquals(1, plants.size());
    }

    @Test
    void getAllPlantsInGarden_multipleGardensWithPlants_listHasOnePlant() {
        Plant plant = new Plant(
                        "Test Plant",
                        null,
                        null,
                        null,
                        null,
                        this.garden
        );
        plantService.savePlant(plant);

        Garden gardenTwo = new Garden(user, "Test Garden Two", null, new Location("United States", "Evans"),
                null, true);
        gardenRepository.save(gardenTwo);

        Plant plantTwo = new Plant(
                        "Test Plant Two",
                        null,
                        null,
                        null,
                        null,
                        gardenTwo
        );
        plantService.savePlant(plantTwo);

        List<Plant> plants = plantService.getAllPlantsInGarden(this.garden);
        Assertions.assertEquals(1, plants.size());
    }

}
