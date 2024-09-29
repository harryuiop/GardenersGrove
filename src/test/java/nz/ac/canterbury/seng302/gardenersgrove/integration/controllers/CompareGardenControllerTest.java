package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import jakarta.transaction.Transactional;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.LocationService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.compareGardensUri;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class CompareGardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    @SpyBean
    private ArduinoDataPointService arduinoDataPointService;

    static boolean gardenSaved = false;

    static Garden garden;
    static Garden gardenTwo;
    static Garden gardenThree;

    static User user;
    static User userTwo;
    static User userThree;

    @BeforeEach
    void saveGarden() {
        Mockito.reset(arduinoDataPointService);
        if (gardenSaved) {
            return;
        }

        user = new User("testuser@email.com", "Test", "User", "Password1!", "2000-01-01");
        userRepository.save(user);

        userTwo = new User("testusertwo@email.com", "TestTwo", "UserTwo", "Password1!", "2000-01-01");
        userRepository.save(userTwo);

        userThree = new User("testuserthree@email.com", "TestThree", "UserThree", "Password1!", "2000-01-01");
        userRepository.save(userThree);

        Location location = new Location("Test", "Location");
        garden = new Garden(user, "g1", "desc", location, 1.0f, true);
        gardenRepository.save(garden);

        Location locationTwo = new Location("Test", "Location");
        gardenTwo = new Garden(userTwo, "g2", "des2", locationTwo, 1.0f, true);
        gardenRepository.save(gardenTwo);

        Location locationThree = new Location("Test", "Location");
        gardenThree = new Garden(userThree, "g3", "des3", locationThree, 1.0f, true);
        gardenThree.setIsGardenPublic(false);
        gardenRepository.save(gardenThree);

        gardenSaved = true;
    }

    @Test
    void requestCompareGardenPage_validGardenIdOnBoth_200Response() throws Exception {
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(compareGardensUri(garden.getId(), gardenTwo.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("compareGarden"));
    }

    @Test
    void requestCompareGardenPage_invalidGarden_404Response() throws Exception {
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(compareGardensUri(10000, gardenTwo.getId())))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void requestCompareGardenPage_compareWithPrivateGarden_404Response() throws Exception {
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(compareGardensUri(gardenThree.getId(), gardenTwo.getId())))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
