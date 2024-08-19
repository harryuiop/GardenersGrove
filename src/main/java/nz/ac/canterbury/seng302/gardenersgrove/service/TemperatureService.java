package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Temperature;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Service
public class TemperatureService {
    private final TemperatureRepository temperatureRepository;

    @Autowired
    public TemperatureService(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;

     }

    /**
     * Adds a new reading to the repository
     * @param temperature   The reading as a Temperature entity
     */
    public void addTemperature(Temperature temperature) {
        temperatureRepository.save(temperature);
    }

//    public List<Temperature> getAllTemperatures() {
//        return temperatureRepository.findALl();
//    }

    /**
     * Returns a list of Temperature from a specific sql date
     * @param date  A given date in sql form
     * @return      A list of temperature entities from with the same date
     */
    public List<Temperature> getTemperaturesByDate(Date date) {
        return temperatureRepository.findByDate(date);
    }

    /**
     * Returns a list of temperatures from a given hour
     * @param hour  The hour of which we are looking for the data from
     * @return      A list of temperature entities that were created in that hour
     */
    public List<Temperature> getTemperaturesByHour(Time hour) {
        return temperatureRepository.findByTime(hour);
    }

    /**
     * Given a date can return the average temperature for that day
     * @param date  The date that the readings were taken on
     * @return      A double of the mean temperature for the day
     */
    public double getAverageDailyTemperature(Date date) {
        List<Temperature> temperatures = getTemperaturesByDate(date);
        double sum = 0;
        for (Temperature temperature : temperatures) {
            sum += temperature.getTemperature();
        }
        return sum / temperatures.size();
    }

    /**
     * Gets the average daily temperature from a given number of days and converts them
     * into a string to pass through to the model for graphing
     * @param days  the number of days of records to collect
     * @return      a string in with commas between each daily average
     */
    public String getGraphData(int days) {
        StringBuilder list = new StringBuilder();
        for (int i = days; i >= 0; i--) {
            double averageDailyTemperature = getAverageDailyTemperature(Date.valueOf(LocalDate.now().minusDays(i)));
            list.append(String.format("%.2f,", averageDailyTemperature));
        }
        return list.toString();
    }
}
