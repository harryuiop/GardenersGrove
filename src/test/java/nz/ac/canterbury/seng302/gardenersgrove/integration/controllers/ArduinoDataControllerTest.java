package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.controller.ArduinoDataController;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


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

    private Garden garden;

    private boolean isSetUp = false;

    Logger logger = LoggerFactory.getLogger(ArduinoDataController.class);

    @BeforeEach
    public void set_up() {
        if (isSetUp) {
            return;
        }

        User user = new User("test@mail.com", "Test", "User", "Password1!", "01/01/2000");
        userRepository.save(user);

        Location location = new Location("New Zealand", "Christchurch");
        garden = new Garden(user, "Test Garden", "Test Description", location, 1f,
                true);
        garden.setArduinoId("testid");
        gardenRepository.save(garden);

        isSetUp = true;
    }

    @Test
    void send_valid_data_saved_to_database() throws Exception {

        JSONObject json = new JSONObject();
        json.put("id", "testid");
        json.put("temperatureCelsius", 18);
        json.put("humidityPercentage", 35);
        json.put("atmosphereAtm", 29);
        json.put("lightLevelPercentage", 80);
        json.put("moisturePercentage", 75);
        json.put("time", "01-01-2024 09:30");

        mockMvc.perform(MockMvcRequestBuilders.post(ARDUINO_SENSOR_DATA)
                        .content(String.valueOf(json))
        );

        Assertions.assertEquals(1, dataPointRepository.countAllByGarden(garden));
    }

}
