package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.time.Duration;
import java.time.LocalDateTime;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoJsonData;
import org.springframework.stereotype.Component;


/**
 * Defines the validation checks for the arduino data to make sure they are realistic.
 */
@Component
public class ArduinoDataValidator {

    final static double MIN_TEMPERATURE = -90.0; // Lowest temp recorded on Earth
    final static double MAX_TEMPERATURE = 60.0; // Highest temp recorded on Earth
    final static double MIN_HUMIDITY = 0.0;
    final static double MAX_HUMIDITY = 100.0;
    final static double MIN_ATMOSPHERE = 80.0; // Lowest barometer reading recorded on Earth
    final static double MAX_ATMOSPHERE = 110.0; // Highest barometer reading recorded on Earth
    final static double MIN_LIGHT = 0.0;
    final static double MAX_LIGHT = 100.0;
    final static double MIN_MOISTURE = 0.0;
    final static double MAX_MOISTURE = 100.0;

    final static int MAX_READING_DELAY = 5;


    /**
     * Returns true if the temperature is within the range MIN_TEMPERATURE to MAX_TEMPERATURE
     *
     * @param temperature the temperature value recorded in the arduino data
     * @return a boolean of whether the temperature is valid or not
     */
    public static boolean checkValidTemperature(double temperature) {
        return temperature >= MIN_TEMPERATURE && temperature <= MAX_TEMPERATURE;
    }

    /**
     * Returns true if the humidity is within the range MIN_HUMIDITY to MAX_HUMIDITY
     *
     * @param humidity the humidity value recorded in the arduino data
     * @return a boolean of whether the humidity is valid or not
     */
    public static boolean checkValidHumidity(double humidity) {
        return humidity >= MIN_HUMIDITY && humidity <= MAX_HUMIDITY;
    }

    /**
     * Returns true if the atmospheric pressure is within the range MIN_ATMOSPHERE to MAX_ATMOSPHERE
     *
     * @param atmosphericPressure the atmospheric pressure value recorded in the arduino data
     * @return a boolean of whether the atmospheric pressure is valid or not
     */
    public static boolean checkValidAtmosphericPressure(double atmosphericPressure) {
        return atmosphericPressure >= MIN_ATMOSPHERE && atmosphericPressure <= MAX_ATMOSPHERE;
    }

    /**
     * Returns true if the light level is within the range MIN_LIGHT to MAX_LIGHT
     * @param lightLevel the light level value recorded in the arduino data
     * @return a boolean of whether the light level is valid or not
     */
    public static boolean checkValidLight(double lightLevel) {
        return lightLevel >= MIN_LIGHT && lightLevel <= MAX_LIGHT;
    }

    /**
     * Returns true if the moisture is within the range MIN_LIGHT to MAX_LIGHT
     * @param moisture the moisture value recorded in the arduino data
     * @return a boolean of whether the moisture is valid or not
     */
    public static boolean checkValidMoisture(double moisture) {
        return moisture >= MIN_MOISTURE && moisture <= MAX_MOISTURE;
    }

    /**
     * Returns true if the difference in time between the reading and the current time is less than MAX_READING_DELAY, else false
     * @param time the LocalDateTime time of data reading
     * @return a boolean of whether the time difference is less than MAX_READING_DELAY
     */
    public static boolean checkValidTime(LocalDateTime time) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration difference = Duration.between(currentTime, time);
        return (difference.toMinutes() < MAX_READING_DELAY);
    }

    public static boolean checkValidSensorData(ArduinoJsonData dataPoint) {
        return checkValidAtmosphericPressure(dataPoint.getAtmosphereAtm()) && checkValidLight(dataPoint.getLightLevelPercentage())
                && checkValidTime(dataPoint.getTime()) && checkValidTemperature(dataPoint.getTemperatureCelsius())
                && checkValidMoisture(dataPoint.getMoisturePercentage()) && checkValidHumidity(dataPoint.getHumidityPercentage());
    }
}
