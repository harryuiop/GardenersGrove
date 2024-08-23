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
            int readingHour = arduinoDataPoint.getTime().getHour();

            if (i > 0) previousPoint = arduinoDataPoints.get(i - 1);

            if (changeBlock(readingHour, previousPoint.getTime().getHour())) {
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
     * Given a list of data points returns the average of each colum in the data point
     *
     * @param arduinoDataPoints A list of data points to average.
     * @return A list of average values.
     */
    private List<Double> getAverageForBlock(List<ArduinoDataPoint> arduinoDataPoints) {
        int dataSize = arduinoDataPoints.size();
        if (dataSize == 0) {
            return new ArrayList<>(Arrays.asList(null, null, null, null, null));
        }

        double tempSum = 0.0, moistureSum = 0.0, atmosphereSum = 0.0, lightSum = 0.0, humiditySum = 0.0;
        for (ArduinoDataPoint arduinoDataPoint : arduinoDataPoints) {
            tempSum += arduinoDataPoint.getTempCelsius();
            moistureSum += arduinoDataPoint.getMoisturePercent();
            atmosphereSum += arduinoDataPoint.getAtmosphereAtm();
            lightSum += arduinoDataPoint.getLightPercent();
            humiditySum += arduinoDataPoint.getHumidityPercent();
        }
        return new ArrayList<>(Arrays.asList(
                tempSum / dataSize,
                moistureSum / dataSize,
                atmosphereSum / dataSize,
                lightSum / dataSize,
                humiditySum / dataSize
        ));
    }

    /**
     * Given the hour when the current point was recorded and the hour recorded of the previous point if a new block
     * needs create (0-5, 6-11. 12-17, 18-23) return true
     *
     * @param currentHour  The hour the current point was recorded
     * @param previousHour The hour the previous point was recorded
     * @return Whether a new block has been reached or not
     */
    private boolean changeBlock(int currentHour, int previousHour) {
        return currentHour % 6 == 0 && Math.abs(currentHour - (previousHour % 22)) == 1;
    }
}
