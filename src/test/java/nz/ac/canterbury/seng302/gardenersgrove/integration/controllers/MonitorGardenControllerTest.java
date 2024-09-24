package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import jakarta.transaction.Transactional;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.AdviceRangesDTO;
import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;
import org.junit.jupiter.api.Assertions;
import nz.ac.canterbury.seng302.gardenersgrove.service.FriendshipService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class MonitorGardenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    GardenRepository gardenRepository;

    static Garden garden;
    static boolean gardenSaved = false;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendshipService friendshipService;
    @SpyBean
    private ArduinoDataPointService arduinoDataPointService;
    @SpyBean
    private UserService userService;

    static User user;

    @BeforeEach
    void saveGarden() {
        Mockito.reset(arduinoDataPointService);
        if (gardenSaved) {
            return;
        }
        user = new User("testuser@email.com", "Test", "User", "Password1!", "2000-01-01");
        userRepository.save(user);
        Location location = new Location("Test", "Location");
        garden = new Garden(user, "g1", "desc", location, 1.0f, true);
        gardenRepository.save(garden);
        gardenSaved = true;

    }

    @Test
    void requestGardenMonitoringPage_validGardenId_200Response() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenMonitoring"));
    }

    @Test
    void requestGardenMonitoringPage_noArduino_notLinkedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("deviceStatus", "NOT_LINKED"))
                .andExpect(MockMvcResultMatchers.model().attribute("owner", true));
    }

    @Test
    void requestGardenMonitoringPage_linkedArduinoNoData_noDataStatus() throws Exception {
        garden.setArduinoId("127.0.0.1");
        gardenRepository.save(garden);

        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("deviceStatus", "NO_DATA"));
    }

    @Test
    void requestGardenMonitoringPage_linkedArduinoNewData_upToDateStatus() throws Exception {
        garden.setArduinoId("127.0.0.1");
        ArduinoDataPoint arduinoDataPoint = new ArduinoDataPoint(garden, LocalDateTime.of(2000, 1, 1, 0, 0), 1.0, 1.0, 1.0, 1.0, 1.0);
        arduinoDataPointService.saveDataPoint(arduinoDataPoint);
        Mockito.doReturn(arduinoDataPoint).when(arduinoDataPointService).getMostRecentArduinoDataPoint(any());
        gardenRepository.save(garden);

        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("deviceStatus", "OUT_OF_DATE"));
    }

    @Test
    void requestGardenMonitoringPage_invalidGardenId_400Response() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(9999)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void requestGardenMonitoringPage_nullDataAll_correctStringDisplayed() throws Exception {
        garden.setArduinoId("127.0.0.1");
        ArduinoDataPoint arduinoDataPoint = new ArduinoDataPoint(garden, LocalDateTime.of(2000, 1, 1, 0, 0), null, null, null, null, null);
        arduinoDataPointService.saveDataPoint(arduinoDataPoint);
        Mockito.doReturn(arduinoDataPoint).when(arduinoDataPointService).getMostRecentArduinoDataPoint(any());
        gardenRepository.save(garden);

        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("tempReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("moistReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("lightReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("pressureReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("humidReading", "-"));
    }

    @Test
    void requestGardenMonitoringPage_invalidDataAll_correctStringDisplayed() throws Exception {
        garden.setArduinoId("127.0.0.1");
        ArduinoDataPoint arduinoDataPoint = new ArduinoDataPoint(garden, LocalDateTime.of(2000, 1, 1, 0, 0), null, null, 0d, 0d, 0d);
        arduinoDataPointService.saveDataPoint(arduinoDataPoint);
        Mockito.doReturn(arduinoDataPoint).when(arduinoDataPointService).getMostRecentArduinoDataPoint(any());
        gardenRepository.save(garden);

        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("tempReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("moistReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("lightReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("pressureReading", "-"))
                .andExpect(MockMvcResultMatchers.model().attribute("humidReading", "-"));
    }

    @Test
    void requestGardenMonitoringPage_validDataAll_correctStringDisplayed() throws Exception {
        garden.setArduinoId("127.0.0.1");
        ArduinoDataPoint arduinoDataPoint = new ArduinoDataPoint(garden, LocalDateTime.of(2000, 1, 1, 0, 0), 2.0, 2.0, 2.0, 2.0, 2.0);
        arduinoDataPointService.saveDataPoint(arduinoDataPoint);
        Mockito.doReturn(arduinoDataPoint).when(arduinoDataPointService).getMostRecentArduinoDataPoint(any());
        gardenRepository.save(garden);

        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("tempReading", "2.0"))
                .andExpect(MockMvcResultMatchers.model().attribute("moistReading", "2"))
                .andExpect(MockMvcResultMatchers.model().attribute("lightReading", "2"))
                .andExpect(MockMvcResultMatchers.model().attribute("pressureReading", "2.000"))
                .andExpect(MockMvcResultMatchers.model().attribute("humidReading", "2"));
    }

    @Test
    @Transactional
    void postAdviceRanges_allInvalid_errorsShownAndAdviceRangesNotUpdated() throws Exception {
        // Make a copy of the previous advice ranges
        AdviceRangesDTO previousAdviceRanges = new AdviceRangesDTO(garden.getAdviceRanges());

        mockMvc.perform(MockMvcRequestBuilders.post(editAdviceRangesUri(garden.getId()))
                        .param("minTemp", Double.toString(ArduinoDataValidator.MIN_TEMPERATURE - 1))
                        .param("maxTemp", Double.toString(ArduinoDataValidator.MAX_TEMPERATURE + 1))
                        .param("minSoilMoisture", Double.toString(ArduinoDataValidator.MIN_MOISTURE - 1))
                        .param("maxSoilMoisture", Double.toString(ArduinoDataValidator.MAX_MOISTURE + 1))
                        .param("minAirPressure", Double.toString(ArduinoDataValidator.MIN_ATMOSPHERE - 1))
                        .param("maxAirPressure", Double.toString(ArduinoDataValidator.MAX_ATMOSPHERE + 1))
                        .param("minHumidity", Double.toString(ArduinoDataValidator.MIN_HUMIDITY - 1))
                        .param("maxHumidity", Double.toString(ArduinoDataValidator.MAX_HUMIDITY + 1))
                        .param("lightLevel", LightLevel.FULL_SHADE.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("temperatureError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("moistureError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("pressureError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("humidityError"));

        Optional<Garden> optionalUpdatedGarden = gardenRepository.findById(garden.getId());
        Assertions.assertTrue(optionalUpdatedGarden.isPresent());
        Garden updatedGarden = optionalUpdatedGarden.get();

        Assertions.assertEquals(previousAdviceRanges.getMinTemp(), updatedGarden.getAdviceRanges().getMinTemperature());
        Assertions.assertEquals(previousAdviceRanges.getMaxTemp(), updatedGarden.getAdviceRanges().getMaxTemperature());
        Assertions.assertEquals(previousAdviceRanges.getMinHumidity(), updatedGarden.getAdviceRanges().getMinHumidity());
        Assertions.assertEquals(previousAdviceRanges.getMaxHumidity(), updatedGarden.getAdviceRanges().getMaxHumidity());
        Assertions.assertEquals(previousAdviceRanges.getMinAirPressure(), updatedGarden.getAdviceRanges().getMinPressure());
        Assertions.assertEquals(previousAdviceRanges.getMaxAirPressure(), updatedGarden.getAdviceRanges().getMaxPressure());
    }

    @Test
    @Transactional
    void postAdviceRanges_allValid_errorsNotShownAndAdviceRangesUpdated() throws Exception {
        double newMinTemp = 15.34;
        double newMaxTemp = 27.57;
        double newMinSoilMoisture = 50.36;
        double newMaxSoilMoisture = 60.85;
        double newMinAirPressure = 0.93;
        double newMaxAirPressure = 1.07;
        double newMinHumidity = 50.34;
        double newMaxHumidity = 60.52;
        mockMvc.perform(MockMvcRequestBuilders.post(editAdviceRangesUri(garden.getId()))
                        .param("minTemp", Double.toString(newMinTemp))
                        .param("maxTemp", Double.toString(newMaxTemp))
                        .param("minSoilMoisture", Double.toString(newMinSoilMoisture))
                        .param("maxSoilMoisture", Double.toString(newMaxSoilMoisture))
                        .param("minAirPressure", Double.toString(newMinAirPressure))
                        .param("maxAirPressure", Double.toString(newMaxAirPressure))
                        .param("minHumidity", Double.toString(newMinHumidity))
                        .param("maxHumidity", Double.toString(newMaxHumidity))
                        .param("lightLevel", LightLevel.FULL_SHADE.toString())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(MONITOR_GARDEN_URI_STRING))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("temperatureError"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("moistureError"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("pressureError"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("humidityError"));

        Optional<Garden> optionalUpdatedGarden = gardenRepository.findById(garden.getId());
        Assertions.assertTrue(optionalUpdatedGarden.isPresent());
        Garden updatedGarden = optionalUpdatedGarden.get();

        Assertions.assertEquals(newMinTemp, updatedGarden.getAdviceRanges().getMinTemperature(), 0.001);
        Assertions.assertEquals(newMaxTemp, updatedGarden.getAdviceRanges().getMaxTemperature(), 0.001);
        Assertions.assertEquals(newMinHumidity, updatedGarden.getAdviceRanges().getMinHumidity(), 0.001);
        Assertions.assertEquals(newMaxHumidity, updatedGarden.getAdviceRanges().getMaxHumidity(), 0.001);
        Assertions.assertEquals(newMinAirPressure, updatedGarden.getAdviceRanges().getMinPressure(), 0.001);
        Assertions.assertEquals(newMaxAirPressure, updatedGarden.getAdviceRanges().getMaxPressure(), 0.001);
    }
    @Test
    void requestMonitorPage_randomUserNotPublic_notAbleToVisit() throws Exception {
        User randomUser = new User("randomUser@mail.com", "Random", "User", "Password1!", "");
        userRepository.save(randomUser);
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(randomUser);
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void requestMonitorPage_friendUser_ableToVisit() throws Exception {
        User friendUser = new User("friendUser@mail.com", "Friend", "User", "Password1!", "");
        userRepository.save(friendUser);
        friendshipService.addFriend(user, friendUser);
        Assertions.assertTrue(friendshipService.areFriends(user, friendUser));
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(friendUser);
        Assertions.assertEquals(user, garden.getOwner());
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void requestMonitorPage_nonFriendPublicGarden_ableToVisit() throws Exception {
        User randomUser = new User("randomUser@mail.com", "Random", "User", "Password1!", "");
        userRepository.save(randomUser);
        garden.setIsGardenPublic(true);
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(randomUser);
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        garden.setIsGardenPublic(false);
    }
}
