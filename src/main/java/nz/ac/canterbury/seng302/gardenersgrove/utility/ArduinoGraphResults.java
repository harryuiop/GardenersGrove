package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Construct graph representations of data in day, week, month view.
 */
public class ArduinoGraphResults {

    private final List<ArduinoDataPoint> arduinoDataPoints;
    private static final int HOURS_IN_BLOCK = 6;
    private static final int HOURS_IN_DAY = 24;

    /**
     * @param arduinoDataPoints List of arduino data readings over any period of time.
     */
    public ArduinoGraphResults(List<ArduinoDataPoint> arduinoDataPoints) {
        this.arduinoDataPoints = arduinoDataPoints;
    }

    /**
     * Given a list of data points, they are seperated into blocks for the morning, afternoon,
     * evening, night of each day.
     *
     * @return List of Blocks which contain averages for each sensor and time period.
     */
    public List<ArduinoDataBlock> averageDataPointsOverWeek() {
        List<List<ArduinoDataPoint>> blocks = new ArrayList<>();
        List<ArduinoDataPoint> currentBlock = new ArrayList<>();

        ArduinoDataPoint previousPoint = arduinoDataPoints.get(0);

        for (int i = 0; i < arduinoDataPoints.size(); i++) {
            ArduinoDataPoint arduinoDataPoint = arduinoDataPoints.get(i);

            if (i > 0) previousPoint = arduinoDataPoints.get(i - 1);

            if (changeQuarterDayBlock(arduinoDataPoint.getTime(), previousPoint.getTime())) {
                blocks.add(currentBlock);
                currentBlock.clear();
            }
            currentBlock.add(arduinoDataPoint);
        }
        return blocks.stream().map(ArduinoGraphResults::getAverageForBlock).toList();
    }

    /**
     * Create a data block of sensor averages given all readings in that block.
     *
     * @return Block which contain averages for each sensor and time period.
     */
    public static ArduinoDataBlock getAverageForBlock(List<ArduinoDataPoint> arduinoDataPoints) {
        int dataSize = arduinoDataPoints.size();
        if (dataSize == 0) return new ArduinoDataBlock();

        double tempSum = 0.0, moistureSum = 0.0, atmosphereSum = 0.0, lightSum = 0.0, humiditySum = 0.0;
        int tempCount = 0, moistureCount = 0, atmosphereCount = 0, lightCount = 0, humidityCount = 0;
        for (ArduinoDataPoint arduinoDataPoint : arduinoDataPoints) {
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
                arduinoDataPoints.get(0).getTime(),
                arduinoDataPoints.get(arduinoDataPoints.size() - 1).getTime(),
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
     *
     * @param currentDate  The date tested against
     * @param previousDate Previous date in
     * @return Whether a new block has been reached or not and the previous date
     * is not more than the current data
     */
    public static boolean changeQuarterDayBlock(LocalDateTime currentDate, LocalDateTime previousDate) {
        if (currentDate.isBefore(previousDate)) return false;
        if (currentDate.getDayOfYear() != previousDate.getDayOfYear()) return true;

        // Check hours in a different block
        for (int threshold = HOURS_IN_BLOCK - 1; threshold < HOURS_IN_DAY; threshold += HOURS_IN_BLOCK) {
            if (previousDate.getHour() <= threshold) {
                return currentDate.getHour() > threshold;
            }
        }
        return false;
    }
}
