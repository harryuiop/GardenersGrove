package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
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

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
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
