package nz.ac.canterbury.seng302.gardenersgrove.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoJsonData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.UnableToFetchWeatherException;
import org.springframework.web.bind.annotation.*;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for endpoints used by the Arduino.
 */
@RestController
public class ArduinoDataController {
    private final GardenService gardenService;

    private final ArduinoDataPointService dataPointService;

    ArduinoDataController(GardenService gardenService, ArduinoDataPointService dataPointService) {
        this.gardenService = gardenService;
        this.dataPointService = dataPointService;
    }

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Endpoint for Arduino to send sensor data to that creates an ArduinoDataPoint element that is saved to the database.
     *
     * @param sensorData the json data to parse
     */
    @PostMapping(ARDUINO_SENSOR_DATA)
    @ResponseBody
    public void receiveArduinoData(@RequestBody String sensorData) throws UnableToFetchWeatherException {
        try {
            ArduinoJsonData response = objectMapper.readValue(sensorData, ArduinoJsonData.class);
            if (ArduinoDataValidator.checkValidSensorData(response)) {
                dataPointService.saveDataPoint(new ArduinoDataPoint(gardenService.getGardenByArduinoId(response.getId()), response.getTime(),
                        response.getTemperatureCelsius(), response.getHumidityPercentage(), response.getAtmosphereAtm(),
                        response.getLightLevelPercentage(), response.getMoisturePercentage()));
            }
        } catch (JsonProcessingException exception) {
                throw new UnableToFetchWeatherException("Failed to parse JSON response from API", exception);
        }
    }
}
