package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
     * Takes in a list of all ArdunioDataPoints objects attached to a garden and returns the most 
     * recent datapoint based on date element
     *
     * @param gardenId A list of datapoints attached to a garden inside the database
     * @return The most recent data point
     */
    public ArduinoDataPoint getMostRecentArduinoDataPoint(Long gardenId) {
        return dataPointRepository.getGardenArduinoStats(gardenId).getFirst();
    }
}
