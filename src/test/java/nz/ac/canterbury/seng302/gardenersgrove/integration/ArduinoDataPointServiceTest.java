package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.*;
import nz.ac.canterbury.seng302.gardenersgrove.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
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

        arduinoDataPointRepository.deleteAll();

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
