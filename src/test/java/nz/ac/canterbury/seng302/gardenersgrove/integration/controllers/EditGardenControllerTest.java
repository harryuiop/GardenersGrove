package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.LocationRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
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
    private User user;

    @BeforeEach
    void setUp() {
        Mockito.when(mapTilerGeocoding.getFirstSearchResult(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(null);

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
        locationRepository.deleteAll();

        initialGardenLocation = new Location(initialCountry, initialCity);
        initialGardenLocation.setStreetAddress(initialStreetAddress);
        initialGardenLocation.setSuburb(initialSuburb);
        initialGardenLocation.setPostcode(initialPostcode);

        Garden garden = new Garden(user, initialGardenName, null, initialGardenLocation, initialGardenSize, true);

        gardenRepository.save(garden);
        gardenId = garden.getId();
    }

    @Test
    void submitEditForm_unchanged_gardenUnchanged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.toString(), garden.getLocation().toString());
        assertEquals(initialGardenSize, garden.getSize());
    }

    @Test
    void submitEditForm_allValid_gardenUpdated() throws Exception {
        String newGardenName = "New Test Garden";
        float newGardenSize = 10.0f;
        String newCountry = "Australia";
        String newCity = "Sydney";
        String newStreetAddress = "1 Smith Road";
        String newSuburb = "Newtown";
        String newPostcode = "2042";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", newGardenName)
                        .param("gardenSize", Float.toString(newGardenSize))
                        .param("country", newCountry)
                        .param("city", newCity)
                        .param("streetAddress", newStreetAddress)
                        .param("suburb", newSuburb)
                        .param("postcode", newPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(newGardenName, garden.getName());
        assertEquals(newCountry, garden.getLocation().getCountry());
        assertEquals(newCity, garden.getLocation().getCity());
        assertEquals(newStreetAddress, garden.getLocation().getStreetAddress());
        assertEquals(newSuburb, garden.getLocation().getSuburb());
        assertEquals(newPostcode, garden.getLocation().getPostcode());
        assertEquals(newGardenSize, garden.getSize());
    }

    @Test
    void submitEditForm_invalidName_gardenNotUpdated() throws Exception {
        String newGardenName = "Test&Garden";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", newGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(initialGardenLocation.toString(), garden.getLocation().toString());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_noCountry_gardenNotUpdated() throws Exception {
        String newGardenName = "New Garden";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", newGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", "")
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.toString(), garden.getLocation().toString());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_invalidCountry_gardenNotUpdated() throws Exception {
        String newCountry = "g%$#";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", newCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.getCountry(), garden.getLocation().getCountry());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_noCity_gardenNotUpdated() throws Exception {
        String newGardenName = "New Garden";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", newGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", "")
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.toString(), garden.getLocation().toString());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_invalidCity_gardenNotUpdated() throws Exception {
        String newCity = "g%$#";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", newCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.getCity(), garden.getLocation().getCity());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_invalidStreetAddress_gardenNotUpdated() throws Exception {
        String newStreetAddress = "g%$#";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", newStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.getStreetAddress(), garden.getLocation().getStreetAddress());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_invalidSuburb_gardenNotUpdated() throws Exception {
        String newSuburb = "g%$#";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", newSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.getSuburb(), garden.getLocation().getSuburb());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidLocation_invalidPostcode_gardenNotUpdated() throws Exception {
        String newPostcode = "g%$#";

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(initialGardenSize))
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", newPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.getPostcode(), garden.getLocation().getPostcode());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitEditForm_invalidSize_gardenNotUpdated() throws Exception {
        float newGardenSize = -1f;

        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", Float.toString(newGardenSize))
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertEquals(initialGardenLocation.toString(), garden.getLocation().toString());
        assertEquals(initialGardenSize, garden.getSize());
        assertEquals(1, allGardens.size());
    }

    @Test
    void submitForm_noSize_gardenSaved() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(editGardenUri(gardenId))
                        .param("gardenName", initialGardenName)
                        .param("gardenSize", "")
                        .param("country", initialCountry)
                        .param("city", initialCity)
                        .param("streetAddress", initialStreetAddress)
                        .param("suburb", initialSuburb)
                        .param("postcode", initialPostcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(initialGardenName, garden.getName());
        assertNull(garden.getSize());
        assertEquals(initialCountry, garden.getLocation().getCountry());
        assertEquals(initialCity, garden.getLocation().getCity());
        assertEquals(initialStreetAddress, garden.getLocation().getStreetAddress());
        assertEquals(initialSuburb, garden.getLocation().getSuburb());
        assertEquals(initialPostcode, garden.getLocation().getPostcode());
    }
}
