package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoDataBlock;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoGraphResults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ArduinoGraphResultsUnitTest {

    private static Garden garden;

    @BeforeAll
    static void setUp() {
        garden = new Garden(
                new User("", "", "", "", ""),
                "", "",
                new Location("", ""),
                1f, true);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-08-23T10:30, 2024-08-22T10:30", // Different day same time
            "2024-08-23T10:30, 2024-08-19T10:30", // Different day same time
            "2024-01-01T10:30, 2023-01-01T10:30",
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

        boolean result = ArduinoGraphResults.changeQuarterDayBlock(currentDateTime, previousDateTime);

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-08-23T06:13, 2024-08-23T06:00",
            "2024-08-23T07:00, 2024-08-23T06:00",
            "2024-08-23T17:00, 2024-08-23T15:01"
    })
    void changeQuarterDayBlock_returnFalse(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoGraphResults.changeQuarterDayBlock(currentDateTime, previousDateTime);

        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-01T00:00, 2023-01-01T00:00",
            "2024-08-23T10:30, 2024-08-22T10:31",
            "2024-08-23T10:30, 2023-08-23T10:30",
            "2024-08-23T06:13, 2024-05-23T06:00",
            "2024-08-23T00:00, 2024-08-22T23:59"
    })
    void changeDayBlock_returnTrue(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoGraphResults.changeDayBlock(currentDateTime, previousDateTime);

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-01T00:00, 2024-01-01T00:00",
            "2024-08-23T06:13, 2024-08-23T06:00",
            "2024-08-23T23:59, 2024-08-23T00:00"
    })
    void changeDayBlock_returnFalse(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoGraphResults.changeDayBlock(currentDateTime, previousDateTime);

        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-02T00:00, 2023-01-01T23:59",
            "2024-01-02T00:30, 2024-01-02T00:00",
            "2024-08-23T10:30, 2024-08-23T10:29",
            "2024-01-01T10:30, 2023-01-01T10:30",
            "2024-08-23T10:30, 2023-08-23T09:30",
            "2024-08-23T06:00, 2024-05-23T05:59",
            "2024-08-23T00:45, 2024-08-23T00:27"
    })
    void changeHalfHourBlock_returnTrue(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoGraphResults.changeHalfHourBlock(currentDateTime, previousDateTime);

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-02T00:29, 2024-01-02T00:00",
            "2024-01-02T00:15, 2024-01-02T00:11",
            "2024-01-02T05:59, 2024-01-02T05:30",
            "2024-01-02T05:39, 2024-01-02T05:34",
    })
    void changeHalfHourBlock_returnFalse(String currentDateTimeStr, String previousDateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime currentDateTime = LocalDateTime.parse(currentDateTimeStr, formatter);
        LocalDateTime previousDateTime = LocalDateTime.parse(previousDateTimeStr, formatter);

        boolean result = ArduinoGraphResults.changeHalfHourBlock(currentDateTime, previousDateTime);

        Assertions.assertFalse(result);
    }


    @Test
    public void testGetAverageForBlock_allNull() {
        ArduinoDataBlock actual = ArduinoGraphResults.getAverageForBlock(new ArrayList<>());

        Assertions.assertNull(actual.getTemperatureCelsiusAvg());
        Assertions.assertNull(actual.getHumidityPercentageAvg());
        Assertions.assertNull(actual.getAtmosphereAtmAvg());
        Assertions.assertNull(actual.getLightLevelPercentageAvg());
        Assertions.assertNull(actual.getMoisturePercentageAvg());
    }

    @Test
    void testGetAverageForBlock_sameReadings() {
        List<ArduinoDataPoint> arduinoDataPointsInput = Arrays.asList(
                new ArduinoDataPoint(garden, LocalDateTime.now(), 30d, 40d, 0.9d, 60d, 70d),
                new ArduinoDataPoint(garden, LocalDateTime.now(), 30d, 40d, 0.9d, 60d, 70d)
        );
        ArduinoDataBlock expected = new ArduinoDataBlock(LocalDateTime.now(), LocalDateTime.now(),
                30d, 40d, 0.9d,
                60d, 70d);

        ArduinoDataBlock actual = ArduinoGraphResults.getAverageForBlock(arduinoDataPointsInput);

        Assertions.assertEquals(expected.getTemperatureCelsiusAvg(), actual.getTemperatureCelsiusAvg(), 0.0001);
        Assertions.assertEquals(expected.getHumidityPercentageAvg(), actual.getHumidityPercentageAvg(), 0.0001);
        Assertions.assertEquals(expected.getAtmosphereAtmAvg(), actual.getAtmosphereAtmAvg(), 0.0001);
        Assertions.assertEquals(expected.getLightLevelPercentageAvg(), actual.getLightLevelPercentageAvg(), 0.0001);
        Assertions.assertEquals(expected.getMoisturePercentageAvg(), actual.getMoisturePercentageAvg(), 0.0001);
    }

    @Test
    void testGetAverageForBlock_differentReadings() {
        List<ArduinoDataPoint> arduinoDataPointsInput = Arrays.asList(
                new ArduinoDataPoint(garden, LocalDateTime.now(), 30d, 40d, 1.1d, 60d, 80d),
                new ArduinoDataPoint(garden, LocalDateTime.now(), 10d, 20d, 0.9d, 20d, 20d)
        );
        ArduinoDataBlock expected = new ArduinoDataBlock(LocalDateTime.now(), LocalDateTime.now(),
                20d, 30d, 1.0d, 40d,
                50d);

        ArduinoDataBlock actual = ArduinoGraphResults.getAverageForBlock(arduinoDataPointsInput);

        Assertions.assertEquals(expected.getTemperatureCelsiusAvg(), actual.getTemperatureCelsiusAvg(), 0.0001);
        Assertions.assertEquals(expected.getHumidityPercentageAvg(), actual.getHumidityPercentageAvg(), 0.0001);
        Assertions.assertEquals(expected.getAtmosphereAtmAvg(), actual.getAtmosphereAtmAvg(), 0.0001);
        Assertions.assertEquals(expected.getLightLevelPercentageAvg(), actual.getLightLevelPercentageAvg(), 0.0001);
        Assertions.assertEquals(expected.getMoisturePercentageAvg(), actual.getMoisturePercentageAvg(), 0.0001);
    }

    @Test
    void testGetAverageForBlock_mixedNullValues() {
        List<ArduinoDataPoint> arduinoDataPointsInput = Arrays.asList(
                new ArduinoDataPoint(garden, LocalDateTime.now(), null, null, null, 30d, 20d),
                new ArduinoDataPoint(garden, LocalDateTime.now(), 10d, null, 0.9d, 20d, 20d),
                new ArduinoDataPoint(garden, LocalDateTime.now(), 20d, null, 0.9d, 10d, 20d)
        );
        ArduinoDataBlock expected = new ArduinoDataBlock(LocalDateTime.now(), LocalDateTime.now()
                ,15d, null, 0.9d,
                20d, 20d);


        ArduinoDataBlock actual = ArduinoGraphResults.getAverageForBlock(arduinoDataPointsInput);

        Assertions.assertEquals(expected.getTemperatureCelsiusAvg(), actual.getTemperatureCelsiusAvg(), 0.0001);
        Assertions.assertNull(actual.getHumidityPercentageAvg());
        Assertions.assertEquals(expected.getAtmosphereAtmAvg(), actual.getAtmosphereAtmAvg(), 0.0001);
        Assertions.assertEquals(expected.getLightLevelPercentageAvg(), actual.getLightLevelPercentageAvg(), 0.0001);
        Assertions.assertEquals(expected.getMoisturePercentageAvg(), actual.getMoisturePercentageAvg(), 0.0001);
    }

    @Test
    void testAverageDataPointsOverWeek_allPointsIncluded() {
        // Set Up, add data points every 30 minutes for a month
        List<ArduinoDataPoint> arduinoDataPointsInput = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 8, 24, 14, 30);
        LocalDateTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            arduinoDataPointsInput.add(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    0.9d,
                    60d,
                    70d
            ));
            currentTime = currentTime.plusMinutes(30);
        }

//        List<ArduinoDataBlock> actual = new ArduinoGraphResults(arduinoDataPointsInput).averageDataPointsOverWeek();
//        Assertions.assertEquals(30, actual.size());
//        Assertions.assertEquals(30, actual.get(0).getTemperatureCelsiusAvg(), 0.0001);
//        for (ArduinoDataBlock arduinoDataBlock: actual) {
//            System.out.println(arduinoDataBlock.getStartTime() + " " + arduinoDataBlock.getEndTime());
//        }
//        //Assertions.fail();
    }
}
