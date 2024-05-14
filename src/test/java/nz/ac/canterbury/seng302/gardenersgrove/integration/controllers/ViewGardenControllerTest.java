package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.LocationRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.makeGardenPublicUri;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
public class ViewGardenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private MapTilerGeocoding mapTilerGeocoding;

    private final String initialGardenName = "Test Garden";
    private final float initialGardenSize = 100.0f;
    private final String initialCountry = "New Zealand";
    private final String initialCity = "Christchurch";
    private final String initialStreetAddress = "90 Ilam Road";
    private final String initialSuburb = "Ilam";
    private final String initialPostcode = "8041";
    private Location initialGardenLocation;

    private Long gardenId;
    private User user1;
    private User user2;


    @BeforeEach
    void setUp() {
        Mockito.when(mapTilerGeocoding.getFirstSearchResult(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(null);

        if (user1 == null) {
            user1 = new User(
                    "test@domain.net",
                    "Test",
                    "User",
                    "Password1!",
                    "2000-01-01"
            );
            userRepository.save(user1);
        }
        if (user2 == null) {
            user2 = new User(
                    "jane@doe.com",
                    "Jane",
                    "Doe",
                    "Password1!",
                    "2000-01-01"
            );
            userRepository.save(user2);
        }

        gardenRepository.deleteAll();
        locationRepository.deleteAll();

        initialGardenLocation = new Location(initialCountry, initialCity);
        initialGardenLocation.setStreetAddress(initialStreetAddress);
        initialGardenLocation.setSuburb(initialSuburb);
        initialGardenLocation.setPostcode(initialPostcode);

        Garden garden = new Garden(user1, initialGardenName, null, initialGardenLocation, initialGardenSize, true);

        gardenRepository.save(garden);
        gardenId = garden.getId();
    }
    @Test
    void makePublic_publicTrue_gardenIsPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri())
                        .param("gardenId", String.valueOf(gardenId))
                        .param("publicGarden","true"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<Garden> garden = gardenRepository.findById(gardenId);
        if (!garden.isEmpty()) {
            Assertions.assertTrue(garden.get().getPublicGarden());
        }
    }

}
