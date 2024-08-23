package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ArduinoDataPoint lastPointFromGarden(Garden garden) {
        return dataPointRepository.findFirstByGardenOrderByTime(garden);
    }
}
