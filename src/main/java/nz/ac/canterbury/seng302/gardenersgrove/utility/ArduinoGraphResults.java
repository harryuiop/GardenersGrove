package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Construct graph representations of data in day, week, month view.
 */
public class ArduinoGraphResults {

    private final List<ArduinoDataPoint> arduinoDataPoints;
    private static final int HOURS_IN_BLOCK = 6;
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HALF_HOUR = 30;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 30;
    private static final int NUM_SENSORS = 5;

    private static final String[] MONTH_STRINGS = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private static final String[] DAY_STRINGS = {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    private enum LabelType {
        DAY,
        MONTH,
        WEEK
    }


    /**
     * @param arduinoDataPoints List of arduino data readings over any period of time,
     *                          in ascending order by time.
     */
    public ArduinoGraphResults(List<ArduinoDataPoint> arduinoDataPoints) {
        this.arduinoDataPoints = arduinoDataPoints;
    }

    /**
     * Given a list of data points, separate them into blocks of time,
     * finding the average sensor readings in each block.
     *
     * @param blockChangeCondition conditional method to check if change in time warrants a new
     *                             block construction.
     * @return List of Blocks which contain averages for each sensor and time period.
     * Note this is ascending order by time.
     */
    public List<ArduinoDataBlock> averageDataIntoBlocks(BiPredicate<LocalDateTime, LocalDateTime> blockChangeCondition) {
        List<List<ArduinoDataPoint>> blocks = new ArrayList<>();
        List<ArduinoDataPoint> currentBlock = new ArrayList<>();

        if (arduinoDataPoints.isEmpty()) return new ArrayList<>();

        ArduinoDataPoint previousPoint = arduinoDataPoints.get(0);

        for (int i = 0; i < arduinoDataPoints.size(); i++) {
            ArduinoDataPoint arduinoDataPoint = arduinoDataPoints.get(i);

            if (i > 0) previousPoint = arduinoDataPoints.get(i - 1);

            if (blockChangeCondition.test(arduinoDataPoint.getTime(), previousPoint.getTime())) {
                blocks.add(currentBlock);
                currentBlock = new ArrayList<>();
            }
            currentBlock.add(arduinoDataPoint);
        }

        if (!currentBlock.isEmpty()) blocks.add(currentBlock);

        System.out.println("RAHHH");
        for (List<ArduinoDataPoint> arduinoDataPoints1: blocks) {
            System.out.println(arduinoDataPoints1.stream().map(a -> a.getTempCelsius()).toList());
        }

        return blocks.stream().map(ArduinoGraphResults::getAverageForBlock).toList();
    }

    /**
     * Create a data block of sensor averages given all readings in that block.
     * Current Date must be after previous date.
     *
     * @param pointsInBlock All arduino points within block
     * @return Block which contain averages for each sensor and time period.
     */
    public static ArduinoDataBlock getAverageForBlock(List<ArduinoDataPoint> pointsInBlock) {
        int dataSize = pointsInBlock.size();
        if (dataSize == 0) return new ArduinoDataBlock();

        double tempSum = 0.0, moistureSum = 0.0, atmosphereSum = 0.0, lightSum = 0.0, humiditySum = 0.0;
        int tempCount = 0, moistureCount = 0, atmosphereCount = 0, lightCount = 0, humidityCount = 0;
        for (ArduinoDataPoint arduinoDataPoint : pointsInBlock) {
            if (arduinoDataPoint.getTempCelsius() != null) {
                tempSum += arduinoDataPoint.getTempCelsius();
                tempCount += 1;
            }
            if (arduinoDataPoint.getHumidityPercent() != null) {
                humiditySum += arduinoDataPoint.getHumidityPercent();
                humidityCount += 1;
            }
            if (arduinoDataPoint.getAtmosphereAtm() != null) {
                atmosphereSum += arduinoDataPoint.getAtmosphereAtm();
                atmosphereCount += 1;
            }
            if (arduinoDataPoint.getLightPercent() != null) {
                lightSum += arduinoDataPoint.getLightPercent();
                lightCount += 1;
            }
            if (arduinoDataPoint.getMoisturePercent() != null) {
                moistureSum += arduinoDataPoint.getMoisturePercent();
                moistureCount += 1;
            }
        }

        return new ArduinoDataBlock(
                pointsInBlock.get(pointsInBlock.size() - 1).getTime(),
                tempCount > 0 ? tempSum / tempCount : null,
                humidityCount > 0 ? humiditySum / humidityCount : null,
                atmosphereCount > 0 ? atmosphereSum / atmosphereCount : null,
                lightCount > 0 ? lightSum / lightCount : null,
                moistureCount > 0 ? moistureSum / moistureCount : null
        );
    }

    /**
     * Check if the change in times are in a new quarter day block
     * This is for checking how distribute data over each quarter of a day.
     * Current Date must be after previous date.
     *
     * @param currentDate  The date tested against
     * @param previousDate Previous date tested against
     * @return Whether a new block has been reached
     */
    public static boolean changeQuarterDayBlock(LocalDateTime currentDate, LocalDateTime previousDate) {
        if (changeDayBlock(currentDate, previousDate)) return true;

        // Check hours in a different block
        for (int threshold = HOURS_IN_BLOCK - 1; threshold < HOURS_IN_DAY; threshold += HOURS_IN_BLOCK) {
            if (previousDate.getHour() <= threshold) {
                return currentDate.getHour() > threshold;
            }
        }
        return false;
    }

    /**
     * Check if the change in times are a different day.
     * Current Date must be after previous date.
     *
     * @param currentDate  The date tested against
     * @param previousDate Previous date tested against
     * @return Whether a new block has been reached
     */
    public static boolean changeDayBlock(LocalDateTime currentDate, LocalDateTime previousDate) {
        return currentDate.getDayOfYear() != previousDate.getDayOfYear() ||
                currentDate.getYear() != previousDate.getYear();
    }

    /**
     * Check if the change in times are in a new 1/48th of a day (ie: new half hour).
     *
     * @param currentDate  The date tested against
     * @param previousDate Previous date tested against
     * @return Whether a new block has been reached
     */
    public static boolean changeHalfHourBlock(LocalDateTime currentDate, LocalDateTime previousDate) {
        if (currentDate.getHour() != previousDate.getHour()
                || changeDayBlock(currentDate, previousDate)) return true;

        int currentDateMinutes = currentDate.getMinute();
        int previousDateMinutes = previousDate.getMinute();
        return (currentDateMinutes < MINUTES_IN_HALF_HOUR && previousDateMinutes >= MINUTES_IN_HALF_HOUR)
                || (currentDateMinutes >= MINUTES_IN_HALF_HOUR && previousDateMinutes < MINUTES_IN_HALF_HOUR);
    }

    /**
     * Format results to send to graph for a week view
     * Time accessed must be greater than arduino data blocks.
     *
     * @param arduinoDataBlocks processed arduino data blocks
     * @param accessDate DateTime results are requested in ascending order by time
     * @return Results formatted to be in graph
     */
    public static FormattedGraphData formatResultsForWeek(List<ArduinoDataBlock> arduinoDataBlocks, LocalDateTime accessDate) {
        int size = DAYS_IN_WEEK * (HOURS_IN_DAY / HOURS_IN_BLOCK) + (accessDate.getHour() / HOURS_IN_BLOCK);
        LocalDateTime startTime = accessDate.minusDays(DAYS_IN_WEEK).toLocalDate().atTime(HOURS_IN_BLOCK, 0, 0);

        return formatResultsGeneric(filterBlocksInTimeFrame(arduinoDataBlocks, startTime.minusHours(HOURS_IN_BLOCK), accessDate),
                size, startTime, Duration.ofHours(HOURS_IN_BLOCK), LabelType.WEEK);
    }

    /**
     * Format results to send to graph for a day view
     * Time accessed must be greater than arduino data blocks.
     *
     * @param arduinoDataBlocks processed arduino data blocks
     * @param accessDate DateTime results are requested in ascending order by time
     * @return Results formatted to be in graph
     */
    public static FormattedGraphData formatResultsForDay(List<ArduinoDataBlock> arduinoDataBlocks, LocalDateTime accessDate) {
        int size = HOURS_IN_DAY * 2;
        LocalDateTime startTime = accessDate.minusDays(1).plusMinutes(MINUTES_IN_HALF_HOUR);

        return formatResultsGeneric(filterBlocksInTimeFrame(arduinoDataBlocks, startTime.minusMinutes(MINUTES_IN_HALF_HOUR), accessDate),
                size, startTime, Duration.ofMinutes(MINUTES_IN_HALF_HOUR), LabelType.DAY);
    }

    /**
     * Format results to send to graph for a month view
     * Time accessed must be greater than arduino data blocks.
     *
     * @param arduinoDataBlocks processed arduino data blocks in ascending order by time
     * @param accessDate DateTime results are requested
     * @return Results formatted to be in graph
     */
    public static FormattedGraphData formatResultsForMonth(List<ArduinoDataBlock> arduinoDataBlocks, LocalDateTime accessDate) {
        int size = DAYS_IN_MONTH + 1;
        LocalDateTime startTime = accessDate.toLocalDate().minusDays(DAYS_IN_MONTH).atTime(0, 0, 0);

        return formatResultsGeneric(filterBlocksInTimeFrame(arduinoDataBlocks, startTime.minusDays(1), accessDate),
                size, startTime, Duration.ofDays(1), LabelType.MONTH);
    }


    /**
     * Helper function to format results, avoiding code duplication.
     *
     * @param arduinoDataBlocks List of Arduino Data Blocks in ascending time order
     * @param size Expected number of graph points, this is not necessarily equal to
     *            data blocks size (due to possible null values), > 0
     * @param startTime Beginning of search time
     * @param durationStep Expected time increase per block
     * @param labelType Type of label to correctly format
     * @return Results formatted to be in graph
     */
    private static FormattedGraphData formatResultsGeneric(List<ArduinoDataBlock> arduinoDataBlocks,
                                                           int size, LocalDateTime startTime,
                                                           TemporalAmount durationStep, LabelType labelType) {

        List<List<Double>> resultList = new ArrayList<>();
        for (int j = 0; j < NUM_SENSORS; j++) {
            resultList.add(new ArrayList<>());
        }

        List<String> labels = new ArrayList<>();

        LocalDateTime dateToCheck = startTime;
        int arduinoIndex = 0;

        LocalDateTime minDateTime = dateToCheck.minus(durationStep);
        for (int i = 0; i < size; i++) {

            // Only update if the block is contained in time period
            // For example, if arduino is disconnected for a block period, the result will be null
            if (arduinoIndex < arduinoDataBlocks.size() &&
                    arduinoDataBlocks.get(arduinoIndex).getEndTime().isBefore(dateToCheck) &&
                    arduinoDataBlocks.get(arduinoIndex).getEndTime().isAfter(minDateTime)
            ) {

                ArduinoDataBlock arduinoDataBlock = arduinoDataBlocks.get(arduinoIndex);

                resultList.get(0).add(arduinoDataBlock.getTemperatureCelsiusAvg());
                resultList.get(1).add(arduinoDataBlock.getHumidityPercentageAvg());
                resultList.get(2).add(arduinoDataBlock.getAtmosphereAtmAvg());
                resultList.get(3).add(arduinoDataBlock.getLightLevelPercentageAvg());
                resultList.get(4).add(arduinoDataBlock.getMoisturePercentageAvg());

                arduinoIndex += 1;
            } else {
                for (int j = 0; j < NUM_SENSORS; j++) {
                    resultList.get(j).add(null);
                }
            }

            String labelToAdd = '"' + getLabelForDate(dateToCheck, labelType) + '"';
            if (!labels.contains(labelToAdd)) labels.add(labelToAdd);

            dateToCheck = dateToCheck.plus(durationStep);
        }
        return new FormattedGraphData(resultList, labels);
    }

    /**
     * Ensure all Data Blocks are within a timeframe.
     *
     * @param arduinoBlocks All arduino blocks
     * @param startFrame Time all arduino blocks must be after
     * @param endFrame Time all arduino blocks must be before
     * @return Arduino blocks within time frame.
     */
    private static List<ArduinoDataBlock> filterBlocksInTimeFrame(List<ArduinoDataBlock> arduinoBlocks,
                                                                  LocalDateTime startFrame, LocalDateTime endFrame) {
        return arduinoBlocks.stream()
                .filter(block -> block.getEndTime().isBefore(endFrame) && block.getEndTime().isAfter(startFrame))
                .toList();
    }

    private static String getLabelForDate(LocalDateTime dateToFormat, LabelType labelType) {
        return switch (labelType) {
            case DAY ->
                DAY_STRINGS[dateToFormat.getDayOfWeek().getValue() - 1] + " " +
                        dateToFormat.getHour() + ":" + dateToFormat.getMinute();
            case MONTH ->
                    MONTH_STRINGS[dateToFormat.getMonth().getValue() - 1] + " " + dateToFormat.getDayOfMonth();
            case WEEK ->
                MONTH_STRINGS[dateToFormat.getMonth().getValue() - 1] + " " + dateToFormat.getDayOfMonth()
                        + " " + DAY_STRINGS[dateToFormat.getDayOfWeek().getValue() - 1];
            default -> throw new IllegalArgumentException("Unknown label type: " + labelType);
        };
    }
}
