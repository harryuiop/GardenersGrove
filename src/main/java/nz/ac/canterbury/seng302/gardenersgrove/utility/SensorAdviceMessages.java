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


    private AdviceMessageType findAdviceMessageType(List<Double> sensorReadings, double sensorMin, double sensorMax) {
        int nullCount = 0;
        boolean isBelow = false;
        boolean isAbove = false;
        for (Double sensorReading : sensorReadings) {
            if (sensorReading == null) {
                nullCount += 1;
            } else {
                if (sensorReading < sensorMin) isBelow = true;
                if (sensorReading > sensorMax) isAbove = true;
            }
        }
        if (nullCount == sensorReadings.size()) return AdviceMessageType.EMPTY;
        if (isBelow && isAbove) return AdviceMessageType.BELOW_AND_ABOVE;
        if (isBelow) return AdviceMessageType.BELOW;
        if (isAbove) return AdviceMessageType.ABOVE;
        return AdviceMessageType.WITHIN;
    }

    private String getAdvice(String sensorName, List<Double> sensorReadings, double minRange, double maxRange,
                             String belowAdvice, String aboveAdvice) {
        AdviceMessageType adviceMessageType = findAdviceMessageType(sensorReadings, minRange, maxRange);

        return switch (adviceMessageType) {
            case BELOW -> belowAdvice;
            case ABOVE -> aboveAdvice;
            case BELOW_AND_ABOVE -> belowAdvice + "\n" + aboveAdvice;
            case WITHIN -> IDEAL_ADVICE + sensorName + ".";
            default -> "";
        };
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

        if (!advice.isEmpty()) {
            model.addAttribute("temperatureAdvice", advice);
            model.addAttribute("temperatureReference", TEMPERATURE_REFERENCES);
        }
    }

    public void addSoilMoistureAdviceToModel(Model model) {
        String advice = getAdvice("moisture", dayData.getMoisture(),
                adviceRanges.getMinMoisture(), adviceRanges.getMaxMoisture(),
                BELOW_MOISTURE_ADVICE, ABOVE_MOISTURE_ADVICE);

        if (!advice.isEmpty()) {
            model.addAttribute("moistureAdvice", advice);
            model.addAttribute("moistureReference", MOISTURE_REFERENCES);
        }

    }

    public void addHumidityAdviceToModel(Model model) {
        String advice = getAdvice("add", dayData.getHumidity(),
                adviceRanges.getMinHumidity(), adviceRanges.getMaxHumidity(),
                BELOW_HUMIDITY_ADVICE, ABOVE_HUMIDITY_ADVICE);
        if (!advice.isEmpty()) {
            model.addAttribute("humidityAdvice", advice);
            model.addAttribute("humidityReference", HUMIDITY_REFERENCES);
        }
    }


}
