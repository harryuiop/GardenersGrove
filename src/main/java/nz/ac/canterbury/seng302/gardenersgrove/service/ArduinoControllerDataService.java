package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.utility.AdviceMessage;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;
import nz.ac.canterbury.seng302.gardenersgrove.utility.SensorAdviceMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ArduinoDataValidator.*;
import static nz.ac.canterbury.seng302.gardenersgrove.utility.TimeConverter.minutestoTimeString;

/**
 * This is a service class setup to deal with helping HTML models get the correct data from the Arduino.
 */
@Service
public class ArduinoControllerDataService {
    private final ArduinoDataPointService arduinoDataPointService;

    @Autowired
    public ArduinoControllerDataService(ArduinoDataPointService arduinoDataPointService) {
        this.arduinoDataPointService = arduinoDataPointService;
    }

    /**
     * Helper method to add graph data and advice messages to the model.
     *
     * @param model The Thymeleaf model to add information to.
     * @param gardenId The ID number of the garden to get graph data for.
     * @param garden Garden to get advice information from.
     */
    public void addGraphDataAndAdviceMessagesToModel(Model model, Long gardenId, Garden garden) {
        FormattedGraphData dayData = arduinoDataPointService.getDayGraphData(gardenId, LocalDateTime.now());
        FormattedGraphData weekData = arduinoDataPointService.getWeekGraphData(gardenId, LocalDateTime.now());
        FormattedGraphData monthData = arduinoDataPointService.getMonthGraphData(gardenId, LocalDateTime.now());

        model.addAttribute("graphDay", dayData);
        model.addAttribute("graphWeek", weekData);
        model.addAttribute("graphMonth", monthData);


        AdviceRanges adviceRanges = garden.getAdviceRanges();

        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(dayData, adviceRanges);

        AdviceMessage temperatureAdvice = sensorAdviceMessages.getTemperatureAdvice();
        if (temperatureAdvice != null) {
            model.addAttribute("temperatureAdvice", temperatureAdvice.getAdviceMessage());
            model.addAttribute("temperatureReference", temperatureAdvice.getReferenceList());
        }

        AdviceMessage soilMoistureAdvice = sensorAdviceMessages.getSoilMoistureAdvice();
        if (soilMoistureAdvice != null) {
            model.addAttribute("moistureAdvice", soilMoistureAdvice.getAdviceMessage());
            model.addAttribute("moistureReference", soilMoistureAdvice.getReferenceList());
        }
        AdviceMessage humidityAdvice = sensorAdviceMessages.getHumidityAdvice();
        if (humidityAdvice != null) {
            model.addAttribute("humidityAdvice", humidityAdvice.getAdviceMessage());
            model.addAttribute("humidityReference", humidityAdvice.getReferenceList());
        }
        sensorAdviceMessages.addTemperatureAdviceToModel(model);
        sensorAdviceMessages.addSoilMoistureAdviceToModel(model);
        sensorAdviceMessages.addHumidityAdviceToModel(model);
        sensorAdviceMessages.addLightAdviceToModel(model);
    }

    /**
     * Overload helper method to add graph data for two gardens to the model.
     *
     * @param model The Thymeleaf model to add information to.
     * @param gardenId1 The ID number of the garden to get graph data for.
     * @param gardenId2 The ID number of the second garden to get graph data for.
     */
    public void addGraphDataToModel(Model model, Long gardenId1, Long gardenId2) {
        FormattedGraphData dayData = arduinoDataPointService.getDayGraphData(gardenId1, LocalDateTime.now());
        FormattedGraphData monthData = arduinoDataPointService.getMonthGraphData(gardenId1, LocalDateTime.now());

        FormattedGraphData dayDataOther = arduinoDataPointService.getDayGraphData(gardenId2, LocalDateTime.now());
        FormattedGraphData monthDataOther = arduinoDataPointService.getMonthGraphData(gardenId2, LocalDateTime.now());

        model.addAttribute("graphDay", dayData);
        model.addAttribute("graphMonth", monthData);
        model.addAttribute("graphDayOther", dayDataOther);
        model.addAttribute("graphMonthOther", monthDataOther);
    }

    /**
     * Helper method to add current sensor readings to HTML model.
     *
     * @param model The Thymeleaf model to add information to.
     * @param garden The Garden to get sensor readings for.
     */
    public void addCurrentSensorReadingsToModel(Model model, Garden garden) {
        String tempReading = "-";
        String moistReading = "-";
        String lightReading = "-";
        String pressureReading = "-";
        String humidReading = "-";
        boolean tempSensorConnected;
        boolean moistSensorConnected;
        boolean lightSensorConnected;
        boolean pressureSensorConnected;
        boolean humidSensorConnected;

        ArduinoDataPoint arduinoDataPoint = arduinoDataPointService.getMostRecentArduinoDataPoint(garden);
        if (arduinoDataPoint != null) {
            if (arduinoDataPoint.getTempCelsius() != null) {
                tempSensorConnected = isTempConnected(arduinoDataPoint.getTempCelsius());
                tempReading = (tempSensorConnected) ? String.format("%.1f", arduinoDataPoint.getTempCelsius()) : "-";
            }
            if (arduinoDataPoint.getMoisturePercent() != null) {
                moistSensorConnected = isMoistConnected(arduinoDataPoint.getMoisturePercent());
                moistReading = (moistSensorConnected) ? String.format("%.0f", arduinoDataPoint.getMoisturePercent()) : "-";
            }
            if (arduinoDataPoint.getLightPercent() != null) {
                lightSensorConnected = isLightConnected(arduinoDataPoint.getLightPercent());
                lightReading = (lightSensorConnected) ? String.format("%.0f", arduinoDataPoint.getLightPercent()) : "-";
            }
            if (arduinoDataPoint.getAtmosphereAtm() != null) {
                pressureSensorConnected = isPressureConnected(arduinoDataPoint.getAtmosphereAtm());
                pressureReading = (pressureSensorConnected) ? String.format("%.3f", arduinoDataPoint.getAtmosphereAtm()) : "-";
            }
            if (arduinoDataPoint.getHumidityPercent() != null) {
                humidSensorConnected = isHumidityConnected(arduinoDataPoint.getHumidityPercent());
                humidReading = (humidSensorConnected) ? String.format("%.0f", arduinoDataPoint.getHumidityPercent()) : "-";
            }
        }
        model.addAttribute("tempReading", tempReading);
        model.addAttribute("moistReading", moistReading);
        model.addAttribute("lightReading", lightReading);
        model.addAttribute("pressureReading", pressureReading);
        model.addAttribute("humidReading", humidReading);
    }

    /**
     * Add device status, and time since last reading to html model.
     *
     * @param model Thy Thymeleaf model to add information to.
     * @param garden The Garden to check device status information of.
     */
    public void addDeviceStatusInformationToModel(Model model, Garden garden) {
        String deviceStatus;
        ArduinoDataPoint lastDataPoint;
        long minutesSinceLastReading;
        String timeSinceLastReading = "";

        if (garden.getArduinoId() == null) {
            deviceStatus = "NOT_LINKED";
        } else {
            lastDataPoint = arduinoDataPointService.getMostRecentArduinoDataPoint(garden);
            if (lastDataPoint == null) {
                deviceStatus = "NO_DATA";
            } else {
                minutesSinceLastReading = Duration.between(LocalDateTime.now(), lastDataPoint.getTime()).abs().toMinutes();
                timeSinceLastReading = minutestoTimeString(minutesSinceLastReading);
                if (minutesSinceLastReading <= 5) {
                    deviceStatus = "UP_TO_DATE";
                } else {
                    deviceStatus = "OUT_OF_DATE";
                }
            }
        }
        model.addAttribute("deviceStatus", deviceStatus);
        model.addAttribute("timeSinceLastReading", timeSinceLastReading);
    }

    /**
     * Add all the data thresholds to the model to be used for the valid ranges
     * on the advice range inputs.
     */
    public void addArduinoDataThresholds(Model model) {
        model.addAttribute("MIN_VALID_TEMP", ArduinoDataValidator.MIN_TEMPERATURE);
        model.addAttribute("MAX_VALID_TEMP", ArduinoDataValidator.MAX_TEMPERATURE);
        model.addAttribute("MIN_VALID_MOISTURE", ArduinoDataValidator.MIN_MOISTURE);
        model.addAttribute("MAX_VALID_MOISTURE", ArduinoDataValidator.MAX_MOISTURE);
        model.addAttribute("MIN_VALID_PRESSURE", ArduinoDataValidator.MIN_ATMOSPHERE);
        model.addAttribute("MAX_VALID_PRESSURE", ArduinoDataValidator.MAX_ATMOSPHERE);
        model.addAttribute("MIN_VALID_HUMIDITY", ArduinoDataValidator.MIN_HUMIDITY);
        model.addAttribute("MAX_VALID_HUMIDITY", ArduinoDataValidator.MAX_HUMIDITY);
        model.addAttribute("LIGHT_LEVELS", Arrays.asList(LightLevel.values()));
    }

}
