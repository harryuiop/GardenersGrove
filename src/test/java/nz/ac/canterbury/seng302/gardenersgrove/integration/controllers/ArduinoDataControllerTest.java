package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.controller.ArduinoDataController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.format.DateTimeFormatter;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
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

    Logger logger = LoggerFactory.getLogger(ArduinoDataController.class);

    static Integer count = 0;

    @BeforeEach
    public void set_up() {
        if (gardenRepository.findAllByArduinoId("testid").isEmpty()) {
            user = new User("test@mail.com", "Test", "User", "Password1!", "01/01/2000");
            userRepository.save(user);

            Location location = new Location("New Zealand", "Christchurch");
            Garden garden = new Garden(user, "Test Garden", "Test Description", location, 1f, true);
            garden.setArduinoId("testid");
            gardenRepository.save(garden);

            this.gardenId = garden.getId();
        } else {
            gardenId = gardenRepository.findAllByArduinoId("testid").get(0).getId();
        }
    }

    @Test
    void send_valid_data_saved_to_database() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String jsonString = "{\"humidityPercentage\":35,\"lightLevelPercentage\":80,\"moisturePercentage\":75,\"id\":\"testid\",\"time\":\"22-08-2024 14:36\",\"temperatureCelsius\":18,\"atmosphereAtm\":100}";

        mockMvc.perform(MockMvcRequestBuilders.post(ARDUINO_SENSOR_DATA)
                        .content(jsonString)
                        .contentType(MediaType.TEXT_PLAIN))
                        .andExpect(status().isOk());

        logger.info(String.valueOf(count) + "saved");
        count += 1;
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
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        logger.info(String.valueOf(count) + "invalid");
        count += 1;
        Assertions.assertEquals(0, dataPointRepository.findAllByGardenId(gardenId).size());
    }
}
