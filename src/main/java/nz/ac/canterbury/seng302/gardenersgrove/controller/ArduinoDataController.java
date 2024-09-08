package nz.ac.canterbury.seng302.gardenersgrove.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoJsonData;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.UnableToFetchWeatherException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * Send sensor data to web page for the last month
     *
     * @param response HttpServletResponse
     * @param term term to get data
     * @param gardenId garden Id to get sensor data
     * @param dataType type of data (such as temperature and air pressure) to get
     */
    @GetMapping(SENSOR_DATA_RESPONSE)
    public void responseMonthData(HttpServletResponse response,
                                        @PathVariable(name = "term") String term,
                                        @PathVariable(name = "gardenId") long gardenId,
                                        @PathVariable(name = "dataType") String dataType) {
        try {
            FormattedGraphData formattedData = null;
            List<Double> data = null;

            switch (term) {
                case "month" -> formattedData = dataPointService.getMonthGraphData(gardenId, LocalDateTime.now());
                case "week" -> formattedData = dataPointService.getWeekGraphData(gardenId, LocalDateTime.now());
                case "day" -> formattedData = dataPointService.getDayGraphData(gardenId, LocalDateTime.now());
                default -> throw new InvalidParameterException();
            }

            switch (dataType) {
                case "temperature" -> data = formattedData.getTemperature();
                case "humidity" -> data = formattedData.getHumidity();
                case "atmosphere" -> data = formattedData.getAtmosphere();
                case "light" -> data = formattedData.getLight();
                case "moisture" -> data = formattedData.getMoisture();
                default -> throw new InvalidParameterException();
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");


            String json = objectMapper.writeValueAsString(Map.of("data", data));
            response.getWriter().write(json);

        } catch (IOException e) {
            // in case an exception happens during execution of writeValueAsString and getWriter methods
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (InvalidParameterException invalidParameterException) {
            // Wrong path variable values provided
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
