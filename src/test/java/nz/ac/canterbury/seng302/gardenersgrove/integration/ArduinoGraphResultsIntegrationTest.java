package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoDataBlock;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoGraphResults;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ArduinoGraphResultsIntegrationTest {

    private static final List<ArduinoDataPoint> monthArduinoDataPointsInput = new ArrayList<>();
    private static final List<ArduinoDataPoint> weekArduinoDataPointsInput = new ArrayList<>();

    private static final List<ArduinoDataPoint> dayArduinoDataPointsInput = new ArrayList<>();

    private static LocalDateTime accessTime;

    @BeforeAll
    static void setUp() {
        Garden garden = new Garden(
                new User("", "", "", "", ""),
                "", "",
                new Location("", ""),
                1f, true);

        // Set up dummy data
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 10, 0, 0);
        LocalDateTime dayEnd = LocalDateTime.of(2023, 12, 10, 23, 59);
        LocalDateTime weekEnd = LocalDateTime.of(2023, 12, 17, 12, 50);
        LocalDateTime currentTime = startTime;

        while (currentTime.isBefore(endTime)) {
            ArduinoDataPoint newArduinoDataPoint = new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    0.9d,
                    60d,
                    70d
            );
            monthArduinoDataPointsInput.add(newArduinoDataPoint);
            if (currentTime.isBefore(dayEnd)) dayArduinoDataPointsInput.add(newArduinoDataPoint);
            if (currentTime.isBefore(weekEnd)) weekArduinoDataPointsInput.add(newArduinoDataPoint);
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Test
    void testAverageDataPointsOverWeek() {
        List<ArduinoDataBlock> arduinoDataBlocks = new ArduinoGraphResults(weekArduinoDataPointsInput).
                averageDataIntoBlocks(ArduinoGraphResults::changeQuarterDayBlock);

        Assertions.assertEquals(31, arduinoDataBlocks.size());

        Assertions.assertEquals("2023-12-11T11:50", arduinoDataBlocks.get(5).getEndTime().toString());
        Assertions.assertEquals(30, arduinoDataBlocks.get(5).getTemperatureCelsiusAvg());
        Assertions.assertEquals(40, arduinoDataBlocks.get(5).getHumidityPercentageAvg());
        Assertions.assertEquals(0.9, arduinoDataBlocks.get(5).getAtmosphereAtmAvg(), 0.0001);
        Assertions.assertEquals(60, arduinoDataBlocks.get(5).getLightLevelPercentageAvg());
        Assertions.assertEquals(70, arduinoDataBlocks.get(5).getMoisturePercentageAvg());
    }

    @Test
    void testAverageDataPointsOverDay() {
        List<ArduinoDataBlock> arduinoDataBlocks = new ArduinoGraphResults(dayArduinoDataPointsInput).
                averageDataIntoBlocks(ArduinoGraphResults::changeHalfHourBlock);

        Assertions.assertEquals(48, arduinoDataBlocks.size());

        Assertions.assertEquals("2023-12-10T02:55", arduinoDataBlocks.get(5).getEndTime().toString());
        Assertions.assertEquals(30, arduinoDataBlocks.get(5).getTemperatureCelsiusAvg());
        Assertions.assertEquals(40, arduinoDataBlocks.get(5).getHumidityPercentageAvg());
        Assertions.assertEquals(0.9, arduinoDataBlocks.get(5).getAtmosphereAtmAvg(), 0.0001);
        Assertions.assertEquals(60, arduinoDataBlocks.get(5).getLightLevelPercentageAvg());
        Assertions.assertEquals(70, arduinoDataBlocks.get(5).getMoisturePercentageAvg());
    }

    @Test
    void testAverageDataPointsOverMonth() {
        List<ArduinoDataBlock> arduinoDataBlocks = new ArduinoGraphResults(monthArduinoDataPointsInput).
                averageDataIntoBlocks(ArduinoGraphResults::changeDayBlock);

        Assertions.assertEquals(31, arduinoDataBlocks.size());

        Assertions.assertEquals("2023-12-15T23:45", arduinoDataBlocks.get(5).getEndTime().toString());
        Assertions.assertEquals(30, arduinoDataBlocks.get(5).getTemperatureCelsiusAvg());
        Assertions.assertEquals(40, arduinoDataBlocks.get(5).getHumidityPercentageAvg());
        Assertions.assertEquals(0.9, arduinoDataBlocks.get(5).getAtmosphereAtmAvg(), 0.0001);
        Assertions.assertEquals(60, arduinoDataBlocks.get(5).getLightLevelPercentageAvg());
        Assertions.assertEquals(70, arduinoDataBlocks.get(5).getMoisturePercentageAvg());
    }

    @Test
    void testFormatResultsForDay_standardInput() {
        // Set up
        List<ArduinoDataBlock> arduinoDataBlocksInput = new ArrayList<>();
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 4, 1),
                30d, 40d, 0.9d, 60d, 70d));

        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));

        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 7, 32), 30d, 40d, 0.9d, 60d, 70d));

        accessTime = LocalDateTime.of(2023, 1, 1, 10, 0);

        FormattedGraphData formattedResults = ArduinoGraphResults.formatResultsForDay(arduinoDataBlocksInput, accessTime);

        List<List<Double>> expectedReadings = Arrays.asList(
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 30.0, 30.0, null, null, null, null, null, 30.0, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 40.0, 40.0, null, null, null, null, null, 40.0, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.9, 0.9, null, null, null, null, null, 0.9, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 60.0, 60.0, null, null, null, null, null, 60.0, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 70.0, 70.0, null, null, null, null, null, 70.0, null, null, null, null)
        );

        Assertions.assertEquals(expectedReadings, formattedResults.getSensorReadings());
    }

    @Test
    void testFormatResultsForDay_containsBlocksNotInDay() {
        // Set up
        List<ArduinoDataBlock> arduinoDataBlocksInput = new ArrayList<>();
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2022, 12, 1, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));

        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 4, 1),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 7, 31), 30d, 40d, 0.9d, 60d, 70d));

        accessTime = LocalDateTime.of(2023, 1, 1, 7, 33);

        FormattedGraphData formattedResults = ArduinoGraphResults.formatResultsForDay(arduinoDataBlocksInput, accessTime);

        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 30.0, null, null, null, null, null, null, 30.0),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 40.0, null, null, null, null, null, null, 40.0),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.9, null, null, null, null, null, null, 0.9),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 60.0, null, null, null, null, null, null, 60.0),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 70.0, null, null, null, null, null, null, 70.0)
        );

        Assertions.assertEquals(expected, formattedResults.getSensorReadings());
    }

    @Test
    void testFormatResultsForWeek_standardInput() {
        // Set up
        List<ArduinoDataBlock> arduinoDataBlocksInput = new ArrayList<>();
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 6, 1),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 5, 4, 1),
                30d, 40d, 0.9d, 60d, 70d));

        accessTime = LocalDateTime.of(2023, 1, 6, 7, 33);

        FormattedGraphData formattedResults = ArduinoGraphResults.formatResultsForWeek(arduinoDataBlocksInput, accessTime);

        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(null, null, null, null, null, null, null, null, 30.0, 30.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 30.0, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, 40.0, 40.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 40.0, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, 0.9, 0.9, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.9, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, 60.0, 60.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 60.0, null, null, null, null),
                Arrays.asList(null, null, null, null, null, null, null, null, 70.0, 70.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 70.0, null, null, null, null)
        );

        Assertions.assertEquals(expected, formattedResults.getSensorReadings());
    }

    @Test
    void testFormatResultsForWeek_containsBlockNotInWeek() {
        // Set up
        List<ArduinoDataBlock> arduinoDataBlocksInput = new ArrayList<>();
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2021, 1, 1, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 6, 1),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 5, 4, 1),
                30d, 40d, 0.9d, 60d, 70d));

        accessTime = LocalDateTime.of(2023, 1, 7, 7, 33);

        FormattedGraphData formattedResults = ArduinoGraphResults.formatResultsForWeek(arduinoDataBlocksInput, accessTime);

        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(null, null, null, null, 30.0, 30.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 30.0, null, null, null, null, null, null, null, null),
                Arrays.asList(null, null, null, null, 40.0, 40.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 40.0, null, null, null, null, null, null, null, null),
                Arrays.asList(null, null, null, null, 0.9, 0.9, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.9, null, null, null, null, null, null, null, null),
                Arrays.asList(null, null, null, null, 60.0, 60.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 60.0, null, null, null, null, null, null, null, null),
                Arrays.asList(null, null, null, null, 70.0, 70.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 70.0, null, null, null, null, null, null, null, null)
        );

        Assertions.assertEquals(expected, formattedResults.getSensorReadings());
    }

    @Test
    void testFormatResultsForMonth_standardInput() {
        // Set up
        List<ArduinoDataBlock> arduinoDataBlocksInput = new ArrayList<>();
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2022, 12, 27, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 6, 1),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 5, 4, 1),
                30d, 40d, 0.9d, 60d, 70d));

        accessTime = LocalDateTime.of(2023, 1, 6, 7, 33);

        FormattedGraphData formattedResults = ArduinoGraphResults.formatResultsForMonth(arduinoDataBlocksInput, accessTime);

        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 30.0, null, null, null, null, 30.0, null, null, null, 30.0, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 40.0, null, null, null, null, 40.0, null, null, null, 40.0, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.9, null, null, null, null, 0.9, null, null, null, 0.9, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 60.0, null, null, null, null, 60.0, null, null, null, 60.0, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 70.0, null, null, null, null, 70.0, null, null, null, 70.0, null)
        );


        Assertions.assertEquals(expected, formattedResults.getSensorReadings());
    }

    @Test
    void testFormatResultsForMonth_containsBlockNotInMonth() {
        // Set up
        List<ArduinoDataBlock> arduinoDataBlocksInput = new ArrayList<>();
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2022, 12, 4, 4, 31),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 1, 6, 1),
                30d, 40d, 0.9d, 60d, 70d));
        arduinoDataBlocksInput.add(new ArduinoDataBlock(LocalDateTime.of(2023, 1, 5, 4, 1),
                30d, 40d, 0.9d, 60d, 70d));

        accessTime = LocalDateTime.of(2023, 1, 6, 7, 33);

        FormattedGraphData formattedResults = ArduinoGraphResults.formatResultsForMonth(arduinoDataBlocksInput, accessTime);

        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 30.0, null, null, null, 30.0, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 40.0, null, null, null, 40.0, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.9, null, null, null, 0.9, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 60.0, null, null, null, 60.0, null),
                Arrays.asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 70.0, null, null, null, 70.0, null)
        );

        Assertions.assertEquals(expected, formattedResults.getSensorReadings());
    }
}
