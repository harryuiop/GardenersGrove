package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ArduinoDataPointService {

    private final ArduinoDataPointRepository dataPointRepository;

    private static final int HOURS_IN_BLOCK = 6;
    private static final int HOURS_IN_DAY = 24;


    @Autowired
    public ArduinoDataPointService(ArduinoDataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    /**
     * Saves a data point to the database
     *
     * @param dataPoint Arduino Data Point object to be saved
     */
    public void saveDataPoint(ArduinoDataPoint dataPoint) {
        dataPointRepository.save(dataPoint);
    }

    /**
     * Takes in a list of all ArduinoDataPoints objects attached to a garden and returns the most
     * recent datapoint based on date element
     *
     * @param gardenId A list of datapoints attached to a garden inside the database
     * @return The most recent data point
     */
    public ArduinoDataPoint getMostRecentArduinoDataPoint(Long gardenId) {
        return dataPointRepository.getGardenArduinoStats(gardenId).getFirst();
    }

    /**
     * Given a garden and a number of days return all the data points for the given garden over the past number of days
     *
     * @param gardenId The garden I want the data points from
     * @param days     The number of days worth of data I want
     * @return The data points for the garden after the given day
     */
    public List<ArduinoDataPoint> getDataPointsOverDays(Long gardenId, int days) {
        return dataPointRepository.getArduinoDataPointOverDays(gardenId, LocalDateTime.now().minusDays(days).toLocalDate().atTime(0, 0, 0), LocalDateTime.now());
    }

    /**
     * Given a list of data points, they are seperated into blocks based on the time taken currently every 6 hours and
     * then the average value of each part of the data point (temperature, humidity, etc...) inside the given time blacks
     * then a list containing the averages of each block are inside a list so there is a list of averages per part of the
     * data points.
     *
     * @param arduinoDataPoints A List of data points.
     * @return A List with lists per part of the data point containing the averages per given block.
     */
    public List<List<Double>> averageDataPointsOverWeek(List<ArduinoDataPoint> arduinoDataPoints) {
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

        List<List<Double>> resultData = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Collections.nCopies(blocks.size(), null)),
                new ArrayList<>(Collections.nCopies(blocks.size(), null)),
                new ArrayList<>(Collections.nCopies(blocks.size(), null)),
                new ArrayList<>(Collections.nCopies(blocks.size(), null)),
                new ArrayList<>(Collections.nCopies(blocks.size(), null))
        ));


        for (int i = 0; i < blocks.size(); i++) {
            List<Double> allSensorReadingsForBlock = getAverageForBlock(blocks.get(i));
            for (int j = 0; j < allSensorReadingsForBlock.size(); j++) {
                resultData.get(j).set(i, allSensorReadingsForBlock.get(j));
            }
        }

        return resultData;
    }

    /**
     * Given a list of data points returns the average results for each sensor value in order:
     * temperature, humidity, atmosphere, light, moisture
     *
     * @param arduinoDataPoints A list of data points to average.
     * @return A list of size 5 containing average values for each sensor in order:
     * temperature, humidity, atmosphere, light, moisture
     */
    public static List<Double> getAverageForBlock(List<ArduinoDataPoint> arduinoDataPoints) {
        int dataSize = arduinoDataPoints.size();
        if (dataSize == 0) return new ArrayList<>(Arrays.asList(null, null, null, null, null));

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

        return new ArrayList<>(Arrays.asList(
                tempCount > 0 ? tempSum / tempCount : null,
                humidityCount > 0 ? humiditySum / humidityCount : null,
                atmosphereCount > 0 ? atmosphereSum / atmosphereCount : null,
                lightCount > 0 ? lightSum / lightCount : null,
                moistureCount > 0 ? moistureSum / moistureCount : null
        ));
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
