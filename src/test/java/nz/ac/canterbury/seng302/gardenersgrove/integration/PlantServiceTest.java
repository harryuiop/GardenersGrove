package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
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
    private PlantService plantService;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserService userService;

    private Garden garden;
    private User user;

    @BeforeEach
    void setUp() {
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
        gardenRepository.deleteAll();
        this.garden = new Garden(user, "Test Garden", new Location("New Zealand", "Christchurch"), null);
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

        Garden gardenTwo = new Garden(user, "Test Garden Two", new Location("United States", "Evans"), null);
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
