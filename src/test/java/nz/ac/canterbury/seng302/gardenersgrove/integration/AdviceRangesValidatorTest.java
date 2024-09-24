package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.AdviceRangesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AdviceRangesValidatorTest {
    @ParameterizedTest
    @CsvSource({
            ArduinoDataValidator.MIN_TEMPERATURE + ", " + ArduinoDataValidator.MAX_TEMPERATURE,
            "15.0, 25.0",
            "15.0, 15.1",
            "-2, -1",
    })
    void checkValidTemperatureRange_returnTrue(double minTemp, double maxTemp) {
        Assertions.assertTrue(AdviceRangesValidator.checkRangeValid(minTemp, maxTemp,
                ArduinoDataValidator::checkValidTemperature));
    }

    @ParameterizedTest
    @CsvSource({
            (ArduinoDataValidator.MIN_TEMPERATURE - 0.1) + ", 30",
            "15, " + (ArduinoDataValidator.MAX_TEMPERATURE + 0.1),
            "15.0, 15.0",
            "30.1, 30",
            "-1, -1.1"
    })
    void checkInvalidTemperatureRange_returnFalse(double minTemp, double maxTemp) {
        Assertions.assertFalse(AdviceRangesValidator.checkRangeValid(minTemp, maxTemp,
                ArduinoDataValidator::checkValidTemperature));
    }

    @ParameterizedTest
    @CsvSource({
            ArduinoDataValidator.MIN_ATMOSPHERE + ", " + ArduinoDataValidator.MAX_ATMOSPHERE,
            "0.9, 1.0",
            "0.9, 0.91",
    })
    void checkValidPressureRange_returnTrue(double minPressure, double maxPressure) {
        Assertions.assertTrue(AdviceRangesValidator.checkRangeValid(minPressure, maxPressure,
                ArduinoDataValidator::checkValidAtmosphericPressure));
    }

    @ParameterizedTest
    @CsvSource({
            (ArduinoDataValidator.MIN_ATMOSPHERE - 0.1) + ", 1.05",
            "0.95, " + (ArduinoDataValidator.MAX_ATMOSPHERE + 0.1),
            "1.1, 1.1",
            "1.0, 0.9",
    })
    void checkInvalidPressureRange_returnFalse(double minPressure, double maxPressure) {
        Assertions.assertFalse(AdviceRangesValidator.checkRangeValid(minPressure, maxPressure,
                ArduinoDataValidator::checkValidAtmosphericPressure));
    }

    @ParameterizedTest
    @CsvSource({
            ArduinoDataValidator.MIN_HUMIDITY + ", " + ArduinoDataValidator.MAX_HUMIDITY,
            "50.0, 55.0",
            "50.0, 50.1",
    })
    void checkValidHumidityRange_returnTrue(double minHumidity, double maxHumidity) {
        Assertions.assertTrue(AdviceRangesValidator.checkRangeValid(minHumidity, maxHumidity,
                ArduinoDataValidator::checkValidHumidity));
    }

    @ParameterizedTest
    @CsvSource({
            (ArduinoDataValidator.MIN_HUMIDITY - 0.1) + ", 55.0",
            "40.0, " + (ArduinoDataValidator.MAX_HUMIDITY + 0.1),
            "60.0, 60.0",
            "50.0, 49.0",
    })
    void checkInvalidHumidityRange_returnFalse(double minHumidity, double maxHumidity) {
        Assertions.assertFalse(AdviceRangesValidator.checkRangeValid(minHumidity, maxHumidity, ArduinoDataValidator::checkValidHumidity));
    }

    @ParameterizedTest
    @CsvSource({
            ArduinoDataValidator.MIN_MOISTURE + ", " + ArduinoDataValidator.MAX_MOISTURE,
            "50.0, 55.0",
            "50.0, 50.1",
    })
    void checkValidMoistureRange_returnTrue(double minMoisture, double maxMoisture) {
        Assertions.assertTrue(AdviceRangesValidator.checkRangeValid(minMoisture, maxMoisture,
                ArduinoDataValidator::checkValidMoisture));
    }

    @ParameterizedTest
    @CsvSource({
            (ArduinoDataValidator.MIN_MOISTURE - 0.1) + ", 55.0",
            "40.0, " + (ArduinoDataValidator.MAX_MOISTURE + 0.1),
            "60.0, 60.0",
            "50.0, 49.0",
    })
    void checkInvalidMoistureRange_returnFalse(double minMoisture, double maxMoisture) {
        Assertions.assertFalse(AdviceRangesValidator.checkRangeValid(minMoisture, maxMoisture, ArduinoDataValidator::checkValidMoisture));
    }
}

