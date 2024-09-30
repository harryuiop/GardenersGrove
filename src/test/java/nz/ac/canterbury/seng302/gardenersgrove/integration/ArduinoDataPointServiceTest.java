package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

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

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    private Garden garden;

    @BeforeEach
    void setup() {
        arduinoDataPointRepository.deleteAll();
        userRepository.deleteAll();

        arduinoDataPointService = new ArduinoDataPointService(arduinoDataPointRepository);
        User user = new User(
                "test@domain.net",
                "Test",
                "User",
                "Password1!",
                "2000-01-01"
        );
        userRepository.save(user);

        this.garden = new Garden(user, "Test Garden", null, new Location("New Zealand", "Christchurch"),
                null, true);
        gardenRepository.save(this.garden);

        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now(), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(1), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(2), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(3), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(4), 0d, 20d, 1d, 70d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(5), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(6), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(7), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(8), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(9), 9d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(10), 0d, 30d, 11d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(11), 0d, 20d, 1d, 60d, 40d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(12), 0d, 20d, 1d, 60d, 50d));
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays(15), 0d, 20d, 1d, 60d, 40d));
    }

    @ParameterizedTest
    @CsvSource({
            "TEMPERATURE, 9",
            "HUMIDITY, 30",
            "AIR PRESSURE, 11",
            "LIGHT, 70",
            "MOISTURE, 50"
    })
    void checkMaxValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMaxValueInRange(garden.getId(), LocalDateTime.now().minusDays(14), sensor));
    }

    @ParameterizedTest
    @CsvSource({
            "TEMPERATURE, 0",
            "HUMIDITY, 20",
            "AIR PRESSURE, 1",
            "LIGHT, 60",
            "MOISTURE, 40"
    })
    void checkMinValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMinValueInRange(garden.getId(), LocalDateTime.now().minusDays(14), sensor));

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
