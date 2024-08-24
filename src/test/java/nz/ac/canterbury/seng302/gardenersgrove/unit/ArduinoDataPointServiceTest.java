package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class ArduinoDataPointServiceTest {

    @ParameterizedTest
    @CsvSource({
            "2024-08-23T10:30, 2024-08-22T10:30", // Different day same time
            "2024-08-23T10:30, 2024-08-19T10:30", // Different day same time
            "2024-08-23T06:00, 2024-08-23T05:59",
            "2024-08-23T12:01, 2024-08-23T05:01",
            "2024-08-23T12:01, 2024-08-23T06:01",
            "2024-08-23T23:55, 2024-08-23T00:01",
            "2024-08-23T00:00, 2024-08-22T23:59"
    })
    void changeQuarterDayBlock_returnTrue(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoDataPointService.changeQuarterDayBlock(currentDateTime, previousDateTime);

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-08-23T10:30, 2024-08-23T10:31", // Future
            "2024-08-23T10:30, 2024-08-25T09:31", // Future
            "2024-08-23T06:13, 2024-08-23T06:00",
            "2024-08-23T07:00, 2024-08-23T06:00",
            "2024-08-23T17:00, 2024-08-23T15:01"
    })
    void changeQuarterDayBlock_returnFalse(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoDataPointService.changeQuarterDayBlock(currentDateTime, previousDateTime);

        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testGetAverageForBlock(List<ArduinoDataPoint> input, List<Double> expected) {
        List<Double> actual = ArduinoDataPointService.getAverageForBlock(input);
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            if (expected.get(i) == null) {
                Assertions.assertNull(actual.get(i));
            } else {
                Assertions.assertEquals(expected.get(i), actual.get(i), 0.01);
            }
        }
    }

    // MethodSource providing test cases
    private static Stream<Arguments> provideTestCases() {
        Garden garden = new Garden(
                new User("", "", "", "", ""),
                "", "",
                new Location("", ""),
                1f, true);
        return Stream.of(
                // Test case 1: Empty list
                org.junit.jupiter.params.provider.Arguments.of(
                        new ArrayList<ArduinoDataPoint>(),
                        Arrays.asList(null, null, null, null, null)
                ),
                // Same Readings
                org.junit.jupiter.params.provider.Arguments.of(
                        Arrays.asList(
                                new ArduinoDataPoint(garden, LocalDateTime.now(), 30d, 40d, 0.9d, 60d, 70d),
                                new ArduinoDataPoint(garden, LocalDateTime.now(), 30d, 40d, 0.9d, 60d, 70d)
                        ),
                        Arrays.asList(30d, 40d, 0.9d, 60d, 70d)
                ),
                // Different Readings
                org.junit.jupiter.params.provider.Arguments.of(
                        Arrays.asList(
                                new ArduinoDataPoint(garden, LocalDateTime.now(), 30d, 40d, 1.1d, 60d, 80d),
                                new ArduinoDataPoint(garden, LocalDateTime.now(), 10d, 20d, 0.9d, 20d, 20d)
                                ),
                        Arrays.asList(20d, 30d, 1.0d, 40d, 50d)
                ),
                // Mix of Null Values included
                org.junit.jupiter.params.provider.Arguments.of(
                        Arrays.asList(
                                new ArduinoDataPoint(garden, LocalDateTime.now(), null, null, null, 30d, 20d),
                                new ArduinoDataPoint(garden, LocalDateTime.now(), 10d, null, 0.9d, 20d, 20d),
                                new ArduinoDataPoint(garden, LocalDateTime.now(), 20d, null, 0.9d, 10d, 20d)
                        ),
                        Arrays.asList(15d, null, 0.9d, 20d, 20d)
                )
        );
    }

}
