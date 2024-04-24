package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.VIEW_GARDEN_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.editGardenUri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class EditGardenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    private final String initialGardenName = "Test Garden";

    private final String initialGardenLocation = "Test Location";

    private final float initialGardenSize = 100.0f;

    private Long gardenId;
    private boolean userCreated = false;

    @BeforeEach
    void setUp() {
        if (!userCreated) {
            User user = new User(
                            "test@domain.net",
                            "Test",
                            "User",
                            "Password1!",
                            "2000-01-01"
            );
            userRepository.save(user);
            userCreated = true;
        }
        gardenRepository.deleteAll();
        Garden garden = new Garden(initialGardenName, initialGardenLocation, initialGardenSize);
        gardenRepository.save(garden);
        gardenId = garden.getId();
    }

    @Test
    void submitEditForm_unchanged_gardenUnchanged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                                        .param("gardenName", initialGardenName)
                                        .param("gardenLocation", initialGardenLocation)
                                        .param("gardenSize", Float.toString(initialGardenSize)))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation, garden.getLocation());
        assertEquals(initialGardenSize, garden.getSize());
    }

    @Test
    void submitEditForm_allValid_gardenUpdated() throws Exception {
        String newGardenName = "New Test Garden";
        String newGardenLocation = "New Test Location";
        float newGardenSize = 10.0f;

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                                        .param("gardenName", newGardenName)
                                        .param("gardenLocation", newGardenLocation)
                                        .param("gardenSize", Float.toString(newGardenSize)))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(newGardenName, garden.getName());
        assertEquals(newGardenLocation, garden.getLocation());
        assertEquals(newGardenSize, garden.getSize());
    }

    @Test
    void submitEditForm_invalidName_gardenNotUpdated() throws Exception {
        String newGardenName = "Test&Garden";
        String newGardenLocation = "Test Location";
        float newGardenSize = 4f;

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                                        .param("gardenName", newGardenName)
                                        .param("gardenLocation", newGardenLocation)
                                        .param("gardenSize", Float.toString(newGardenSize)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation, garden.getLocation());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_gardenNotUpdated() throws Exception {
        String newGardenName = "Test Garden";
        String newGardenLocation = "Test^Location";
        float newGardenSize = 4f;

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                                        .param("gardenName", newGardenName)
                                        .param("gardenLocation", newGardenLocation)
                                        .param("gardenSize", Float.toString(newGardenSize)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation, garden.getLocation());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidSize_gardenNotUpdated() throws Exception {
        String newGardenName = "Test Garden";
        String newGardenLocation = "Test Location";
        float newGardenSize = -1f;

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                                        .param("gardenName", newGardenName)
                                        .param("gardenLocation", newGardenLocation)
                                        .param("gardenSize", Float.toString(newGardenSize)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation, garden.getLocation());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitForm_noSize_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                                        .param("gardenName", gardenName)
                                        .param("gardenLocation", gardenLocation)
                                        .param("gardenSize", ""))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));


        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation, garden.getLocation());
        assertNull(garden.getSize());
    }
}
