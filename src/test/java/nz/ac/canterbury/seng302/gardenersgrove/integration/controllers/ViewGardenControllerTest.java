package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.*;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendRequestService;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
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

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.newGardenTagUri;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewGardenUri;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
public class ViewGardenControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private FriendshipService friendshipService;

    @SpyBean
    private UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
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
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);
        gardenRepository.deleteAll();
        locationRepository.deleteAll();

        Location gardenLocation = new Location("New Zealand", "Christchurch");
        gardenLocation.setStreetAddress("90 Ilam Road");
        gardenLocation.setSuburb("Ilam");
        gardenLocation.setPostcode("8041");

        gardenRepository.save(new Garden(user, "Test Garden", null, gardenLocation, null));
    }

    @Test
    public void checkUserHasGarden() throws Exception {
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(gardenId)))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
    }

    @Test
    public void userInputInvalidTagName () throws Exception {
        String tagName = "alkals@U)$(*%&(#*!$&@)";
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(gardenId))
                .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    public void userInputTagNameExceed25Characters () throws Exception {
        String tagName = "This is invalid tag name which will give you a lot of annoy";
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(gardenId))
                        .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);

        Assertions.assertNull(tag);
    }

    @Test
    public void userInputTagNameExceed25CharactersAndHasInvalidCharacters () throws Exception {
        String tagName = "Thi$ i$ inv@lid t@g name with inv@lid ch@r@cter which will give you @ lot of @nnoy";
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(gardenId)).param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));

        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    public void userInputValidTagName () throws Exception {
        String validTagName = "Invalid tag name";
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(gardenId))
                        .param("tagName", validTagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(validTagName);

        Assertions.assertNotNull(tag);
    }
}
