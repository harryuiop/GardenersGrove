package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.controller.GardenController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.VIEW_GARDEN_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.newGardenUri;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;


@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class GardenFormControllerTest {

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    private GardenService gardenService;

    private UserService userService;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp() {

        gardenService = Mockito.spy(new GardenService(gardenRepository));

        userService = Mockito.spy(new UserService(userRepository));

        mockMvc = MockMvcBuilders.standaloneSetup(new GardenController(gardenService, userService)).apply(sharedHttpSession()).build();

        if (user == null) {
            User user = new User(
                            "test@domain.net",
                            "Test",
                            "User",
                            "Password1!",
                            "2000-01-01"
            );
            userRepository.save(user);
        }
        gardenRepository.deleteAll();
    }

    @Test
    void submitForm_allValid_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";
        float gardenSize = 100.0f;
        Mockito.when(userService.getAuthenticatedUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
                                        .param("gardenName", gardenName)
                                        .param("gardenLocation", gardenLocation)
                                        .param("gardenSize", Float.toString(gardenSize)))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(gardenName, garden.getName());
        assertEquals(gardenLocation, garden.getLocation());
        assertEquals(gardenSize, garden.getSize());
    }

    @Test
    void submitForm_invalidName_gardenNotSaved() throws Exception {
        String gardenName = "Test&Garden";
        String gardenLocation = "Test Location";
        float gardenSize = 4f;
        Mockito.when(userService.getAuthenticatedUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
                                        .param("gardenName", gardenName)
                                        .param("gardenLocation", gardenLocation)
                                        .param("gardenSize", Float.toString(gardenSize)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test^Location";
        float gardenSize = 4f;
        Mockito.when(userService.getAuthenticatedUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
                                        .param("gardenName", gardenName)
                                        .param("gardenLocation", gardenLocation)
                                        .param("gardenSize", Float.toString(gardenSize)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidSize_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";
        float gardenSize = -1f;
        Mockito.when(userService.getAuthenticatedUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
                                        .param("gardenName", gardenName)
                                        .param("gardenLocation", gardenLocation)
                                        .param("gardenSize", Float.toString(gardenSize)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_noSize_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";
        Mockito.when(userService.getAuthenticatedUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
                                        .param("gardenName", gardenName)
                                        .param("gardenLocation", gardenLocation))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));


        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(gardenName, garden.getName());
        assertEquals(gardenLocation, garden.getLocation());
        assertNull(garden.getSize());
    }
}