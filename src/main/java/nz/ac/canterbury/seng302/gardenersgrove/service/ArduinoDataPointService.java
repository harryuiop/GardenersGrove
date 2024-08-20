package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import org.springframework.stereotype.Service;

@Service
public class ArduinoDataPointService {

    private ArduinoDataPointRepository dataPointRepository;

    /**
     * Saves a data point to the database
     *
     * @param dataPoint Arduino Data Point object to be saved
     * @return The given data point
     */
    public ArduinoDataPoint saveDataPoint(ArduinoDataPoint dataPoint) {
        return dataPointRepository.save(dataPoint);
    }

}
