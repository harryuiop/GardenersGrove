package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoDataBlock;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoGraphResults;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
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
     * Get a garden's most recent data reading from the Arduino.
     *
     * @param garden The garden to the most recent sensor reading of.
     * @return The data reading.
     */
    public ArduinoDataPoint getMostRecentArduinoDataPoint(Garden garden) {
        return dataPointRepository.findFirstByGardenOrderByTimeDesc(garden);
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
        List<ArduinoDataPoint> dataPoints  = dataPointRepository.getArduinoDataPointOverDays(gardenId,
                accessTime.minusDays(days).toLocalDate().atTime(0, 0, 0), accessTime);
        dataPoints.sort(Comparator.comparing(ArduinoDataPoint::getTime));
        return dataPoints;
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
