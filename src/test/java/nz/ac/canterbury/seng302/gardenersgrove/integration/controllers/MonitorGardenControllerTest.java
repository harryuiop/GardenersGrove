package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
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

import java.time.LocalDateTime;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;

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
    private ArduinoDataPointRepository arduinoDataPointRepository;

    @BeforeEach
    void saveGarden() {
        if (gardenSaved) {
            return;
        }
        User user = new User("testuser@email.com", "Test", "User", "Password1!", "2000-01-01");
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
        ArduinoDataPoint arduinoDataPoint = new ArduinoDataPoint(garden, LocalDateTime.now(), 1.0, 1.0, 1.0, 1.0, 1.0);
        arduinoDataPointRepository.save(arduinoDataPoint);

        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())))
                .andExpect(MockMvcResultMatchers.model().attribute("deviceStatus", "UP_TO_DATE"));
    }

    @Test
    void requestGardenMonitoringPage_invalidGardenId_400Response() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(9999)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
