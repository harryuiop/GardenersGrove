package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoublePredicate;

/**
 * Validate the advice ranges (for sensor readings in a garden)
 */
public class AdviceRangesValidator {

    /**
     * Check if there are errors when user changes the advice ranges for their garden.
     *
     * @return A key - value map of errors, with the key being the name of the attribute to
     * pass into the model and the value being the error message string.
     */
    public static Map<String, String> checkAdviceRanges(double minTemp, double maxTemp, double minMoisture, double maxMoisture,
                                                        double minPressure, double maxPressure, double minHumidity, double maxHumidity) {
        boolean temperatureValid = checkRangeValid(minTemp, maxTemp, ArduinoDataValidator::checkValidTemperature);
        boolean moistureValid = checkRangeValid(minMoisture, maxMoisture, ArduinoDataValidator::checkValidMoisture);
        boolean pressureValid = checkRangeValid(minPressure, maxPressure, ArduinoDataValidator::checkValidAtmosphericPressure);
        boolean humidityValid = checkRangeValid(minHumidity, maxHumidity, ArduinoDataValidator::checkValidHumidity);

        HashMap<String, String> errors = new HashMap<>();

        if (!temperatureValid) {
            errors.put("temperatureError", genericErrorMessage("Temperature",
                    ArduinoDataValidator.MIN_TEMPERATURE, ArduinoDataValidator.MAX_TEMPERATURE));
        }
        if (!moistureValid) {
            errors.put("moistureError", genericErrorMessage("Soil Moisture",
                    ArduinoDataValidator.MIN_MOISTURE, ArduinoDataValidator.MAX_MOISTURE));
        }
        if (!pressureValid) {
            errors.put("pressureError", genericErrorMessage("Air Pressure",
                    ArduinoDataValidator.MIN_ATMOSPHERE, ArduinoDataValidator.MAX_ATMOSPHERE));
        }
        if (!humidityValid) {
            errors.put("humidityError", genericErrorMessage("Humidity",
                    ArduinoDataValidator.MIN_HUMIDITY, ArduinoDataValidator.MAX_HUMIDITY));
        }
        return errors;
    }

    /**
     * Helper method to validate each sensor reading.
     *
     * @param minValue User input min range for sensor
     * @param maxValue User input max range for sensor
     * @param validator Validation method used
     *
     * @return if the range is valid or not
     */
    private static boolean checkRangeValid(double minValue, double maxValue, DoublePredicate validator) {
        return validator.test(minValue) && validator.test(maxValue) && maxValue > minValue;
    }

    /**
     * Generic error message for each sensor.
     *
     * @param sensorName Name of sensor
     * @param minVal Lowest possible value sensor reading can be
     * @param maxVal Highest possible value sensor reading can be
     *
     * @return The error message string for that sensor
     */
    private static String genericErrorMessage(String sensorName, double minVal, double maxVal) {
        return String.format("%s must be between %.2f and %.2f and the minimum must be less than the maximum.",
                sensorName, minVal, maxVal);
    }
}
