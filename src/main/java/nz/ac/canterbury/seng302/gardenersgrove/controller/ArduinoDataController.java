package nz.ac.canterbury.seng302.gardenersgrove.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ArduinoDataPointRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoJsonData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.UnableToFetchWeatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for endpoints used by the arduinos.
 */
@Controller
public class ArduinoDataController {

    @Autowired
    private GardenService gardenService;

    @Autowired
    private ArduinoDataPointService dataPointService;

    @Autowired
    private ArduinoDataPointRepository dataPointRepository;

    Logger logger = LoggerFactory.getLogger(ArduinoDataController.class);

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    private ArduinoDataPointRepository arduinoDataPointRepository;

    /**
     * Endpoint for arduinos to send sensor data to that creates an ArduinoDataPoint element that is saved to the database.
     * @param sensorData the json data to parse
     */
    @PostMapping(ARDUINO_SENSOR_DATA)
    @ResponseBody
    public void receiveArduinoData(@RequestBody String sensorData) throws UnableToFetchWeatherException {
        try {
            ArduinoJsonData response = objectMapper.readValue(sensorData, ArduinoJsonData.class);
            if (ArduinoDataValidator.checkValidSensorData(response)) {
//                logger.info(gardenService.getAllGardens().toString());
//                Optional<Garden> optionalGarden = gardenService.getGardenByArduinoId(response.getId());
                dataPointService.saveDataPoint(new ArduinoDataPoint(gardenService.getGardenByArduinoId(response.getId()), response.getTime(),
                        response.getTemperatureCelsius(), response.getHumidityPercentage(), response.getAtmosphereAtm(),
                        response.getLightLevelPercentage(), response.getMoisturePercentage()));

                Iterable<ArduinoDataPoint> allData = dataPointRepository.findAll();
    }
} catch (JsonProcessingException exception) {
        throw new UnableToFetchWeatherException("Failed to parse JSON response from API", exception);
        }
    }

}
