package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@Import(ArduinoDataPointService.class)
class ArduinoDataPointServiceTest {
    ArduinoDataPointRepository arduinoDataPointRepositoryMock;

    ArduinoDataPointService arduinoDataPointService;

    static User user = new User("arduino@datpoint.com", "First", "Last","Password1!", "");
    static Garden garden = new Garden(user, "Garden", null, new Location("New Zealand", "Christchurch"),
            null, true);

//    @BeforeAll
//    static void setArduinoDataPointService() {
//        userRepository.save(user);
//        gardenRepository.save(garden);
//
//    }

    @BeforeEach
    void setUpArduinoDataPointService() {
        arduinoDataPointRepositoryMock = Mockito.mock(ArduinoDataPointRepository.class);
        arduinoDataPointService = new ArduinoDataPointService(arduinoDataPointRepositoryMock);
        List<ArduinoDataPoint> points = new ArrayList<>();
        for (double i=10; i<20; i++) {
            points.add(new ArduinoDataPoint(
                    garden,
                    LocalDateTime.now(),
                    i-10,
                    i-9,
                    i-8,
                    i-7,
                    i-6));
        }
        Mockito.when(arduinoDataPointRepositoryMock.getArduinoDataPointOverDays(any(), any(), any())).thenReturn(points);
    }

    @ParameterizedTest
    @CsvSource({
            "TEMPERATURE, 9",
            "HUMIDITY, 10",
            "PRESSURE, 11",
            "LIGHT, 12",
            "MOISTURE, 13"
    })
    void checkMaxValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMaxValueInRange(garden.getId(), LocalDateTime.now(), sensor));
    }

    @ParameterizedTest
    @CsvSource({
            "TEMPERATURE, 0",
            "HUMIDITY, 1",
            "PRESSURE, 2",
            "LIGHT, 3",
            "MOISTURE, 4"
    })
    void checkMinValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMinValueInRange(garden.getId(), LocalDateTime.now(), sensor));
    }


}
