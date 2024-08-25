package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoDataBlock;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoGraphResults;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * @param accessTime Time data is accessed.
     * @return The data points for the garden after the given day
     */
    private List<ArduinoDataPoint> getDataPointsOverDays(Long gardenId, int days, LocalDateTime accessTime) {
        return dataPointRepository.getArduinoDataPointOverDays(gardenId,
                accessTime.minusDays(days).toLocalDate().atTime(0, 0, 0), accessTime);
    }

    /**
     * Retrieve results to send to graph for a week view
     * @param gardenId The garden I want the data points from
     * @param accessTime DateTime results are requested
     * @return Results formatted to be in graph
     */
    public FormattedGraphData getWeekGraphData(Long gardenId, LocalDateTime accessTime) {
        int daysInWeek = 7;
        List<ArduinoDataPoint> arduinoDataPoints = getDataPointsOverDays(gardenId, daysInWeek, accessTime);
        List<ArduinoDataBlock> arduinoDataBlocks = new ArduinoGraphResults(arduinoDataPoints)
                .averageDataIntoBlocks(ArduinoGraphResults::changeQuarterDayBlock);
        return ArduinoGraphResults.formatResultsForWeek(arduinoDataBlocks, accessTime);
    }

    /**
     * Retrieve results to send to graph for a day view
     *
     * @param gardenId The garden I want the data points from
     * @param accessTime DateTime results are requested
     * @return Results formatted to be in graph
     */
    public FormattedGraphData getDayGraphData(Long gardenId, LocalDateTime accessTime) {
        List<ArduinoDataPoint> arduinoDataPoints = dataPointRepository.getArduinoDataPointOverDays(gardenId,
                accessTime.minusDays(1), accessTime);
        List<ArduinoDataBlock> arduinoDataBlocks = new ArduinoGraphResults(arduinoDataPoints)
                .averageDataIntoBlocks(ArduinoGraphResults::changeHalfHourBlock);
        return ArduinoGraphResults.formatResultsForDay(arduinoDataBlocks, accessTime);
    }

    /**
     * Retrieve results to send to graph for a month view
     *
     * @param gardenId The garden I want the data points from
     * @param accessTime DateTime results are requested
     * @return Results formatted to be in graph
     */
    public FormattedGraphData getMonthGraphData(Long gardenId, LocalDateTime accessTime) {
        int daysInMonth = 30;
        List<ArduinoDataPoint> arduinoDataPoints = getDataPointsOverDays(gardenId, daysInMonth, accessTime);
        List<ArduinoDataBlock> arduinoDataBlocks = new ArduinoGraphResults(arduinoDataPoints)
                .averageDataIntoBlocks(ArduinoGraphResults::changeDayBlock);
        return ArduinoGraphResults.formatResultsForMonth(arduinoDataBlocks, accessTime);
    }

}
