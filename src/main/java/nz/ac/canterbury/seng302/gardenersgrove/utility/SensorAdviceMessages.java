package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Handles advice messages for sensors if they are outside the expected advice range in the last 24 hours.
 * <p>
 * Possibility of showing two messages if there is a point below and a point above.
 */
public class SensorAdviceMessages {

    private final FormattedGraphData dayData;

    private final AdviceRanges adviceRanges;

    /**
     * Insert the advice here.
     */


    private final String BELOW_TEMPERATURE_ADVICE = "TEMP IS BELOW";
    private final String BELOW_MOISTURE_ADVICE = "HUMIDITY IS BELOW";
    private final String BELOW_HUMIDITY_ADVICE = "HUMIDITY IS BELOW";

    private final String ABOVE_TEMPERATURE_ADVICE = "TEMP IS ABOVE";
    private final String ABOVE_MOISTURE_ADVICE = "MOISTURE IS ABOVE";
    private final String ABOVE_HUMIDITY_ADVICE = "HUMIDITY IS ABOVE";

    private final String IDEAL_ADVICE = "This garden has an ideal ";

    private final List<String> TEMPERATURE_REFERENCES = Arrays.asList("\"some\"", "\"reference\"");
    private final List<String> MOISTURE_REFERENCES = Arrays.asList("\"Joshua\"", "\"reference\"");
    private final List<String> HUMIDITY_REFERENCES = Arrays.asList("\"Winter\"", "\"reference\"");



    public SensorAdviceMessages(FormattedGraphData dayData, AdviceRanges adviceRanges) {
        this.dayData = dayData;
        this.adviceRanges = adviceRanges;
    }


    private boolean isPointBelowMinInLast24Hours(List<Double> sensorReadings, double sensorMin) {
        return sensorReadings.stream().anyMatch(sensorReading -> sensorReading != null && sensorReading < sensorMin);
    }

    private boolean isPointAboveMaxInLast24Hours(List<Double> sensorReadings, double sensorMin) {
        return sensorReadings.stream().anyMatch(sensorReading -> sensorReading != null && sensorReading > sensorMin);
    }

    private String getAdvice(String sensorName, List<Double> sensorReadings, double minRange, double maxRange,
                             String belowAdvice, String aboveAdvice) {
        boolean isSensorBelow = isPointBelowMinInLast24Hours(sensorReadings, minRange);
        boolean isSensorAbove = isPointAboveMaxInLast24Hours(sensorReadings, maxRange);

        String advice = "";

        if (isSensorBelow) advice += belowAdvice;

        if (isSensorAbove) advice += ((!advice.isEmpty()) ? "\n" : "") + aboveAdvice;

        if (!isSensorBelow && !isSensorAbove) return IDEAL_ADVICE + sensorName + ".";

        return advice;
    }

    /**
     * This might later be an object of advice string and references.
     */
    public void addTemperatureAdviceToModel(Model model) {
        String advice = getAdvice("temperature",
                dayData.getTemperature(),
                adviceRanges.getMinTemperature(),
                adviceRanges.getMaxTemperature(),
                BELOW_TEMPERATURE_ADVICE, ABOVE_TEMPERATURE_ADVICE);
        model.addAttribute("temperatureAdvice", advice);
        model.addAttribute("temperatureReference", TEMPERATURE_REFERENCES);
    }

    public void addSoilMoistureAdviceToModel(Model model) {
        String advice = getAdvice("moisture", dayData.getMoisture(),
                adviceRanges.getMinMoisture(), adviceRanges.getMaxMoisture(),
                BELOW_MOISTURE_ADVICE, ABOVE_MOISTURE_ADVICE);
        model.addAttribute("moistureAdvice", advice);
        model.addAttribute("moistureReference", MOISTURE_REFERENCES);

    }

    public void addHumidityAdviceToModel(Model model) {
        String advice = getAdvice("add", dayData.getHumidity(),
                adviceRanges.getMinHumidity(), adviceRanges.getMaxHumidity(),
                BELOW_HUMIDITY_ADVICE, ABOVE_HUMIDITY_ADVICE);
        model.addAttribute("humidityAdvice", advice);
        model.addAttribute("humidityReference", HUMIDITY_REFERENCES);

    }


}
