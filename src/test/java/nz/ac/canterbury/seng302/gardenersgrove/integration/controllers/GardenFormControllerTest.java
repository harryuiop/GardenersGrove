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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class GardenFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

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
    }

    @Test
    void submitForm_allValid_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 100.0f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                .param("gardenName", gardenName)
                .param("gardenSize", Float.toString(gardenSize))
                .param("country", country)
                .param("city", city)
                .param("streetAddress", streetAddress)
                .param("suburb", suburb)
                .param("postcode", postcode)
                .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(gardenName, garden.getName());
        assertEquals(gardenSize, garden.getSize());
        assertEquals(country, garden.getLocation().getCountry());
        assertEquals(city, garden.getLocation().getCity());
        assertEquals(streetAddress, garden.getLocation().getStreetAddress());
        assertEquals(suburb, garden.getLocation().getSuburb());
        assertEquals(postcode, garden.getLocation().getPostcode());
    }

    @Test
    void submitForm_invalidName_gardenNotSaved() throws Exception {
        String gardenName = "Test&Garden";
        float gardenSize = 4f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                .param("gardenName", gardenName)
                .param("gardenSize", Float.toString(gardenSize))
                .param("country", country)
                .param("city", city)
                .param("streetAddress", streetAddress)
                .param("suburb", suburb)
                .param("postcode", postcode)
                .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_noCountry_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 4f;
        String country = "";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                .param("gardenName", gardenName)
                .param("gardenSize", Float.toString(gardenSize))
                .param("country", country)
                .param("city", city)
                .param("streetAddress", streetAddress)
                .param("suburb", suburb)
                .param("postcode", postcode)
                .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_invalidCountry_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 4f;
        String country = "r%4>";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenSize", Float.toString(gardenSize))
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode)
                        .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_noCity_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 4f;
        String country = "New Zealand";
        String city = "";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenSize", Float.toString(gardenSize))
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode)
                        .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_invalidCity_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 4f;
        String country = "New Zealand";
        String city = "5^%";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenSize", Float.toString(gardenSize))
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode)
                        .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_invalidStreetAddress_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 4f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam% Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenSize", Float.toString(gardenSize))
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode)
                        .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_invalidSuburb_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 4f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam&";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenSize", Float.toString(gardenSize))
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode)
                        .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidSize_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = -1f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                .param("gardenName", gardenName)
                .param("gardenSize", Float.toString(gardenSize))
                .param("country", country)
                .param("city", city)
                .param("streetAddress", streetAddress)
                .param("suburb", suburb)
                .param("postcode", postcode)
                .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_noSize_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode)
                        .param("ignoreApiCall", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));


        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(gardenName, garden.getName());
        assertNull(garden.getSize());
        assertEquals(country, garden.getLocation().getCountry());
        assertEquals(city, garden.getLocation().getCity());
        assertEquals(streetAddress, garden.getLocation().getStreetAddress());
        assertEquals(suburb, garden.getLocation().getSuburb());
        assertEquals(postcode, garden.getLocation().getPostcode());
    }
}