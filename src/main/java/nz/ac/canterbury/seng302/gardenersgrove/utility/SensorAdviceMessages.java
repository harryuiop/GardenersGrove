package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

/**
 * Handles advice messages for sensors based on the set advice range.
 * Possibility of showing two messages if there is a point below and a point above.
 * No messages if there are no sensor readings.
 */
public class SensorAdviceMessages {

    private final FormattedGraphData dayData;

    private final AdviceRanges adviceRanges;

    private final String BELOW_TEMPERATURE_ADVICE = "A temperature reading in the last 24 hours dropped " +
            "below the set advice range. Cold temperatures can make plants go dormant or cause damage. Look out" +
            " for discoloured or wilting leaves, root ball damage, split in steam or trunk and stunted growth. If you" +
            " notice any of these signs trim dead roots or repot the plant, do not fertilize, overwater, or over trim " +
            "the plant while it heals.";
    private final String BELOW_MOISTURE_ADVICE = "HUMIDITY IS BELOW";
    private final String BELOW_HUMIDITY_ADVICE = "HUMIDITY IS BELOW";

    private final String ABOVE_TEMPERATURE_ADVICE = "A temperature reading in the last 24 hours gone above" +
            " the set advice range. High temperatures can harm plants by slowing their growth and causing dehydration." +
            " This can lead to smaller, low quality fruits and vegetables. Look for leaf rolling or cupping, wilting, " +
            "dry leaf edges, sunscald or bleached leaves. If any of these signs appear, then water regularly, mulch," +
            " provide shade and provide humidity. Do not transplant, prune or fertilize.";
    private final String ABOVE_MOISTURE_ADVICE = "MOISTURE IS ABOVE";
    private final String ABOVE_HUMIDITY_ADVICE = "HUMIDITY IS ABOVE";

    private final String IDEAL_ADVICE = "This garden has an ideal ";

    private final List<String> TEMPERATURE_REFERENCES = Arrays.asList("\"https://www.womeninagscience.org/post/let-s-grow-a-garden-part-1-temperature-a-key-factor-for-growing-our-food#:~:text=Temperature%20is%20an%20important%20factor,from%2040%20to%2085%20F.\"",
            "\"https://garrettchurchill.com/5-warning-signs-your-plants-are-experiencing-cold-shock/\"",
            "\"https://extension.oregonstate.edu/gardening/flowers-shrubs-trees/heat-wave-garden-how-identify-prevent-heat-stress-plants#:~:text=Above%20104%C2%B0F%2C%20many,activity%20and%20growth%20in%20plants.\"");
    private final List<String> MOISTURE_REFERENCES = Arrays.asList("\"moisturereferenceone\"", "\"moisturereferencetwo\"");
    private final List<String> HUMIDITY_REFERENCES = Arrays.asList("\"some\"", "\"reference\"");


    /**
     * Construct sensor advice object.
     * @param dayData Contains lists of sensor readings in the last 24 hours.
     * @param adviceRanges Garden's advice ranges (min/max for sensors)
     */
    public SensorAdviceMessages(FormattedGraphData dayData, AdviceRanges adviceRanges) {
        this.dayData = dayData;
        this.adviceRanges = adviceRanges;
    }


    /**
     * Helper method to identify the type of advice needed.
     *
     * @param sensorReadings List of sensor readings for a particular sensor
     * @param sensorMin Minimum advice range for that sensor
     * @param sensorMax Maximum advice range for that sensor
     * @return The type of advice needed.
     */
    private AdviceMessageType findAdviceMessageType(List<Double> sensorReadings, double sensorMin, double sensorMax) {
        boolean isAllNull = true;
        boolean isBelow = false;
        boolean isAbove = false;
        for (Double sensorReading : sensorReadings) {
            if (sensorReading != null) {
                isAllNull = false;
                if (sensorReading < sensorMin) isBelow = true;
                if (sensorReading > sensorMax) isAbove = true;
            }
        }
        if (isAllNull) return AdviceMessageType.EMPTY;
        if (isBelow && isAbove) return AdviceMessageType.BELOW_AND_ABOVE;
        if (isBelow) return AdviceMessageType.BELOW;
        if (isAbove) return AdviceMessageType.ABOVE;
        return AdviceMessageType.WITHIN;
    }

    /**
     * Helper method to get the advice message.
     *
     * @param sensorName Name of sensor. This is what is shown to the user.
     * @param sensorReadings List of sensor readings for that sensor.
     * @param minRange Minimum advice range for that sensor
     * @param maxRange Maximum advice range for that sensor
     * @param belowAdvice Message for a point being below the advice range for that sensor
     * @param aboveAdvice Message for a point being above the advice range for that sensor
     * @return an advice message to show to the user
     */
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
     * Add temperature advice and references to the model.
     * @param model Model to add attributes to
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

    /**
     * Add soil moisture advice and references to the model.
     * @param model Model to add attributes to
     */
    public void addSoilMoistureAdviceToModel(Model model) {
        String advice = getAdvice("soil moisture", dayData.getMoisture(),
                adviceRanges.getMinMoisture(), adviceRanges.getMaxMoisture(),
                BELOW_MOISTURE_ADVICE, ABOVE_MOISTURE_ADVICE);

        if (!advice.isEmpty()) {
            model.addAttribute("moistureAdvice", advice);
            model.addAttribute("moistureReference", MOISTURE_REFERENCES);
        }

    }

    /**
     * Add humidity advice and references to the model.
     * @param model Model to add attributes to
     */
    public void addHumidityAdviceToModel(Model model) {
        String advice = getAdvice("humidity", dayData.getHumidity(),
                adviceRanges.getMinHumidity(), adviceRanges.getMaxHumidity(),
                BELOW_HUMIDITY_ADVICE, ABOVE_HUMIDITY_ADVICE);
        if (!advice.isEmpty()) {
            model.addAttribute("humidityAdvice", advice);
            model.addAttribute("humidityReference", HUMIDITY_REFERENCES);
        }
    }
}
