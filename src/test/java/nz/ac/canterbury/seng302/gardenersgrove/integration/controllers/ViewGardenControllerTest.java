package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
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

import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class ViewGardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    private boolean isSetUp = false;
    private Long gardenId;

    @BeforeEach
    void setUp() {
        if (isSetUp) {
            return;
        }

        User user = new User("test@mail.com", "Test", "User", "Password1!", "01/01/2000");
        userRepository.save(user);

        Location location = new Location("New Zealand", "Christchurch");
        Garden garden = new Garden(user, "Test Garden", "Test Description", location, 1f);
        gardenRepository.save(garden);

        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);

        gardenId = garden.getId();

        isSetUp = true;
    }

    @Test
    void checkUserHasGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(gardenId)))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
    }

    @Test
    void userInputInvalidTagName() throws Exception {
        String tagName = "alkals@U)$(*%&(#*!$&@)";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    void userInputTagNameExceed25Characters() throws Exception {
        String tagName = "This is invalid tag name which will give you a lot of annoy";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                        .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);

        Assertions.assertNull(tag);
    }

    @Test
    void userInputTagNameExceed25CharactersAndHasInvalidCharacters() throws Exception {
        String tagName = "Thi$ i$ inv@lid t@g name with inv@lid ch@r@cter which will give you @ lot of @nnoy";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(gardenId))
                        .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));

        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    void userInputValidTagName() throws Exception {
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
                .andExpect(view().name("viewGarden"));
        Optional<Garden> garden = gardenRepository.findById(gardenId);
        boolean isPublic = garden.isPresent() ? garden.get().getPublicGarden() : null;
        System.out.println(""+isPublic);
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
            Assertions.assertFalse(garden.get().getPublicGarden());
        }
    }

    @Test
    void makePublic_publicFalse_gardenIsNotPublic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(makeGardenPublicUri(gardenId))
                .param("publicGarden","null"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Optional<Garden> garden = gardenRepository.findById(gardenId);
        garden.ifPresent(value -> Assertions.assertFalse(value.getPublicGarden()));
    }
}
