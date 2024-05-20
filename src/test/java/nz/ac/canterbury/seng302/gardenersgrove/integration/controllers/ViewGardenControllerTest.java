package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
public class ViewGardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;
    @SpyBean
    private UserService userService;
    @SpyBean
    private GardenService gardenService;
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private MapTilerGeocoding mapTilerGeocoding;

    private Long gardenId;
    private User user1;
    private User user2;

    User user;

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
        tagRepository.deleteAll();

        gardenRepository.deleteAll();

        String initialGardenName = "Test Garden";
        float initialGardenSize = 100.0f;
        String initialCountry = "New Zealand";
        String initialCity = "Christchurch";
        String initialStreetAddress = "90 Ilam Road";
        String initialSuburb = "Ilam";
        String initialPostcode = "8041";
        Location initialGardenLocation;

        initialGardenLocation = new Location(initialCountry, initialCity);
        initialGardenLocation.setStreetAddress(initialStreetAddress);
        initialGardenLocation.setSuburb(initialSuburb);
        initialGardenLocation.setPostcode(initialPostcode);

        Garden garden = new Garden(user1, initialGardenName, null, initialGardenLocation, initialGardenSize, true);

        gardenRepository.save(garden);
        gardenId = garden.getId();
    }

    @Test
    public void checkUserHasGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
    }

    @Test
    public void userInputInvalidTagName () throws Exception {
        String tagName = "alkals@U)$(*%&(#*!$&@)";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    public void userInputTagNameExceed25Characters () throws Exception {
        String tagName = "This is invalid tag name which will give you a lot of annoy";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                        .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);

        Assertions.assertNull(tag);
    }

    @Test
    public void userInputTagNameExceed25CharactersAndHasInvalidCharacters () throws Exception {
        String tagName = "Thi$ i$ inv@lid t@g name with inv@lid ch@r@cter which will give you @ lot of @nnoy";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1)).param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));

        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    public void userInputValidTagName () throws Exception {
        String validTagName = "Invalid tag name";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                        .param("tagName", validTagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(validTagName);

        Assertions.assertNotNull(tag);
    }

    @Test
    void makePublic_publicTrue_gardenIsPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(gardenId))
                        .param("publicGarden","true"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));;
        Optional<Garden> garden = gardenRepository.findById(gardenId);
        garden.ifPresent(value -> Assertions.assertTrue(value.getPublicGarden()));
    }

    @Test
    void makePublic_publicTrueDescriptionInvalid_gardenIsNotPublic() throws Exception {
        Optional<Garden> garden = gardenRepository.findById(gardenId);
        if (garden.isPresent()) {
            garden.get().setVerifiedDescription(false);
            gardenRepository.save(garden.get());
            mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(gardenId))
                    .param("publicGarden","true"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("viewGarden"));
            ;
            Assertions.assertFalse(garden.get().getPublicGarden());
        }
    }

    @Test
    void makePublic_publicFalse_gardenIsNotPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(gardenId))
                .param("publicGarden","null"));
        Optional<Garden> garden = gardenRepository.findById(gardenId);
        garden.ifPresent(value -> Assertions.assertFalse(value.getPublicGarden()));
    }
}
