package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.repository.*;
import nz.ac.canterbury.seng302.gardenersgrove.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@Import(ArduinoDataPointService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArduinoDataPointServiceTest {

    @Autowired
    private ArduinoDataPointRepository arduinoDataPointRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;
    private ArduinoDataPointService arduinoDataPointService;
    private Garden garden;

    private User user;
    static User user = new User("arduino@datpoint.com", "First", "Last","Password1!", "");
    static Garden garden = new Garden(user, "Garden", null, new Location("New Zealand", "Christchurch"),
            null, true);

    @BeforeEach
    void setup() {

        arduinoDataPointService = new ArduinoDataPointService(arduinoDataPointRepository);

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
        this.garden = new Garden(user, "Test Garden", null, new Location("New Zealand", "Christchurch"),
                null, true);
        gardenRepository.save(this.garden);

    @ParameterizedTest
    @CsvSource({
            "TEMPERATURE, 9",
            "HUMIDITY, 10",
            "AIR PRESSURE, 11",
            "LIGHT, 12",
            "MOISTURE, 13"
    })
    void checkMaxValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMaxValueInRange(garden.getId(), LocalDateTime.now(), sensor));
    }
        arduinoDataPointRepository.deleteAll();

    @ParameterizedTest
    @CsvSource({
            "TEMPERATURE, 0",
            "HUMIDITY, 1",
            "AIR PRESSURE, 2",
            "LIGHT, 3",
            "MOISTURE, 4"
    })
    void checkMinValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMinValueInRange(garden.getId(), LocalDateTime.now(), sensor));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now(), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(1), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(2), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(3), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(4), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(5), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(6), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(7), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(8), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(9), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(10), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(11), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(12), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(15), 0d, 20d, 1d, 60d, 40d));

    }

    @Test
    void check14DaysOfDataTrue(){
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(13), 0d, 20d, 1d, 60d, 40d));
        Assertions.assertTrue(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }

    @Test
    void check14DaysOfDataFalse(){
        Assertions.assertFalse(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }

}
