package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
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
class ArduinoDataPointServiceTest {
    ArduinoDataPointRepository arduinoDataPointRepositoryMock;

    ArduinoDataPointService arduinoDataPointService;
    @MockBean
    GardenService gardenService;

    static User user = new User("arduino@datpoint.com", "First", "Last","Password1!", "");
    static Garden garden = new Garden(user, "Garden", null, new Location("New Zealand", "Christchurch"),
            null, true);

    @BeforeEach
    void setUpArduinoDataPointService() {
        Mockito.when(gardenService.getGardenById(any())).thenReturn(Optional.ofNullable(garden));
        arduinoDataPointRepositoryMock = Mockito.mock(ArduinoDataPointRepository.class);
        arduinoDataPointService = new ArduinoDataPointService(arduinoDataPointRepositoryMock, gardenService);
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
            "AIR PRESSURE, 11",
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
            "AIR PRESSURE, 2",
            "LIGHT, 3",
            "MOISTURE, 4"
    })
    void checkMinValues(String sensor, double expected) {
        Assertions.assertEquals(expected, arduinoDataPointService.getMinValueInRange(garden.getId(), LocalDateTime.now(), sensor));
    }

    @Test
    void checkFourteenDaysOfData_noData_returnFalse() {
        Mockito.when(arduinoDataPointRepositoryMock.getArduinoDataPointOverDays(any(), any(), any())).thenReturn(new ArrayList<>());
        Assertions.assertFalse(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }

    @Test
    void checkFourteenDaysOfData_notEnoughData_returnFalse() {
        Assertions.assertFalse(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }

    @Test
    void checkFourteenDaysOfData_enoughData_returnTrue() {
        List<ArduinoDataPoint> points = new ArrayList<>();
        for (double i=10, j=0; j<=14; i++, j++) {
            points.add(new ArduinoDataPoint(
                    garden,
                    LocalDateTime.now().minusDays((long)j),
                    i-10,
                    i-9,
                    i-8,
                    i-7,
                    i-6));
        }
        Mockito.when(arduinoDataPointRepositoryMock.getArduinoDataPointOverDays(any(), any(), any())).thenReturn(points);
        Assertions.assertTrue(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }

    @Test
    void checkFourteenDaysOfData_missingDays_returnFalse() {
        List<ArduinoDataPoint> points = new ArrayList<>();
        for (double i=10, j=0; j<=14; i++, j+=2) {
            points.add(new ArduinoDataPoint(
                    garden,
                    LocalDateTime.now().minusDays((long)j),
                    i-10,
                    i-9,
                    i-8,
                    i-7,
                    i-6));
        }
        Mockito.when(arduinoDataPointRepositoryMock.getArduinoDataPointOverDays(any(), any(), any())).thenReturn(points);
        Assertions.assertFalse(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }
}
