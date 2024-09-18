package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.ARDUINO_SENSOR_DATA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "1")
public class ArduinoDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointRepository dataPointRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Long gardenId;

    @BeforeEach
    public void set_up() {
        if (gardenRepository.findByArduinoId("testid") == null) {
            user = new User("test@mail.com", "Test", "User", "Password1!", "01/01/2000");
            userRepository.save(user);

            Location location = new Location("New Zealand", "Christchurch");
            Garden garden = new Garden(user, "Test Garden", "Test Description", location, 1f, true);
            garden.setArduinoId("testid");
            gardenRepository.save(garden);

            this.gardenId = garden.getId();
        } else {
            gardenId = gardenRepository.findByArduinoId("testid").getId();
        }
    }

    @Test
    void send_valid_data_saved_to_database() throws Exception {
        String jsonString = "{\"humidityPercentage\":35,\"lightLevelPercentage\":80,\"moisturePercentage\":75,\"id\":\"testid\",\"time\":\"22-08-2024 14:36\",\"temperatureCelsius\":18,\"atmosphereAtm\":1.0}";

        mockMvc.perform(MockMvcRequestBuilders.post(ARDUINO_SENSOR_DATA)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Assertions.assertEquals(1, dataPointRepository.findAllByGardenId(gardenId).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"humidityPercentage\":-10,\"lightLevelPercentage\":80,\"moisturePercentage\":75,\"id\":\"testid\",\"time\":\"21-08-2024 16:51\",\"temperatureCelsius\":18,\"atmosphereAtm\":100}",
            "{\"humidityPercentage\":10,\"lightLevelPercentage\":810,\"moisturePercentage\":75,\"id\":\"testid\",\"time\":\"21-08-2024 16:51\",\"temperatureCelsius\":18,\"atmosphereAtm\":100}",
            "{\"humidityPercentage\":10,\"lightLevelPercentage\":80,\"moisturePercentage\":751,\"id\":\"testid\",\"time\":\"21-08-2024 16:51\",\"temperatureCelsius\":18,\"atmosphereAtm\":100}",
})
    void send_invalid_data_not_saved(String jsonString) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ARDUINO_SENSOR_DATA)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(0, dataPointRepository.findAllByGardenId(gardenId).size());
    }
}
