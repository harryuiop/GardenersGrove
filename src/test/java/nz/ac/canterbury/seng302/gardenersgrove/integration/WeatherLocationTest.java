package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Feature;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.LocationService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.newGardenUri;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewGardenUri;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
public class WeatherLocationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationService locationService;

    @SpyBean
    private UserService userService;

    @SpyBean
    private FormValuesValidator mockFormValuesValidator;

    @MockBean
    private WeatherService mockWeatherService;

    @MockBean
    private Feature mockFeature;

    @MockBean
    private List<WeatherData> mockWeatherData;

    private User user;

    @MockBean
    private MapTilerGeocoding mapTilerGeocoding;

    @BeforeEach
    void setUp() throws ProfanityCheckingException, InterruptedException {
        this.mockFeature = Mockito.mock(Feature.class);
        Mockito.when(mapTilerGeocoding.getFirstSearchResult(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(mockFeature);

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
        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);
        gardenRepository.deleteAll();
        locationService.deleteAll();
    }

    @Test
    void recognizedLocation_withCorrectCoordinatesIntoWeather() throws Exception {
        String gardenName = "Test Garden";
        float gardenSize = 100.0f;
        String country = "New Zealand";
        String city = "Christchurch";
        String streetAddress = "90 Ilam Road";
        String suburb = "Ilam";
        String postcode = "8041";

        // Location testing
        List<Double> expectedCoords = new ArrayList<>();
        Double expectedLng = 172.5721;
        Double expectedLat = -43.5168;
        expectedCoords.add(expectedLng);
        expectedCoords.add(expectedLat); // Ilam coordinates
        Mockito.when(mockFeature.getCenter()).thenReturn(expectedCoords);

        mockMvc.perform(MockMvcRequestBuilders.post(newGardenUri())
                        .param("gardenName", gardenName)
                        .param("gardenSize", Float.toString(gardenSize))
                        .param("country", country)
                        .param("city", city)
                        .param("streetAddress", streetAddress)
                        .param("suburb", suburb)
                        .param("postcode", postcode))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAllByOwner(user);
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);

        // Weather testing
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("viewGarden"));

        System.out.println(expectedCoords);
        assertEquals(expectedLat, garden.getLocation().getLat());
        assertEquals(expectedLng, garden.getLocation().getLng());
        Mockito.verify(mockWeatherService).getWeatherData(expectedLng, expectedLat);
    }

}
