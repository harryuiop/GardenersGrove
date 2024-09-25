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

    private final String BELOW_TEMPERATURE_ADVICE = "";
    private final List<String> BELOW_TEMPERATURE_REFERENCES = Arrays.asList("some", "reference");

    private final String ABOVE_TEMPERATURE_ADVICE = "";
    private final List<String> ABOVE_TEMPERATURE_REFERENCES = Arrays.asList("some", "reference");


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

    /**
     * This might later be an object of advice string and references.
     */
    public void addTemperatureAdviceToModel(Model model) {
        boolean isTemperatureBelow = isPointBelowMinInLast24Hours(dayData.getTemperature(), adviceRanges.getMinTemperature());
        boolean isTemperatureAbove = isPointAboveMaxInLast24Hours(dayData.getTemperature(), adviceRanges.getMinTemperature());

        if (isTemperatureBelow) {
            model.addAttribute("temperatureBelowAdvice", BELOW_TEMPERATURE_ADVICE);
            model.addAttribute("temperatureBelowReferences", BELOW_TEMPERATURE_REFERENCES);
        }

        if (isTemperatureAbove) {
            model.addAttribute("temperatureAboveAdvice", ABOVE_TEMPERATURE_ADVICE);
            model.addAttribute("temperatureAboveReferences", ABOVE_TEMPERATURE_REFERENCES);
        }
    }

}
