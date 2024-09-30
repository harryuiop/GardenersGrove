package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles advice messages for sensors based on the set advice range.
 * Possibility of showing two messages if there is a point below and a point above.
 * No messages if there are no sensor readings.
 */
public class SensorAdviceMessages {

    private final FormattedGraphData dayData;

    private final AdviceRanges adviceRanges;

    private static final String BELOW_TEMPERATURE_ADVICE = "A temperature reading in the last 24 hours dropped " +
            "below the set advice range. Cold temperatures can make plants go dormant or cause damage. " +
            "Look out for discoloured or wilting leaves, root ball damage, split in steam or trunk and " +
            "stunted growth. If you notice any of these signs, trim dead roots or repot the plant. " +
            "Do not fertilize, overwater, or over-trim the plant while it heals.";
    private static final String ABOVE_TEMPERATURE_ADVICE = "A temperature reading in the last 24 hours has " +
            "gone above the set advice range. High temperatures can harm plants by slowing their growth" +
            " and causing dehydration. This can lead to smaller, low-quality fruits and vegetables. Look " +
            "for leaf rolling or cupping, wilting, dry leaf edges, sun-scald or bleached leaves. If any of " +
            "these signs appear, water regularly, mulch, and provide shade. Do not transplant, prune or fertilize.";
    private static final String BELOW_MOISTURE_ADVICE = "This garden is below the ideal soil moisture. " +
            "Overly dry soil makes it difficult for plants to absorb moisture decreases the availability of " +
            "nutrients. If you are watering your garden regularly, try adding compost into your soil, or creating " +
            "a layer of compost on top of the soil to prevent the sun from evaporating water from the soil's surface.";
    private static final String ABOVE_MOISTURE_ADVICE = "This garden is above the ideal soil moisture. " +
            "Overly moist soil can create leaching of nutrients, root rot, fungal problems, and prevent growth. " +
            "Try adding organic matter/mulch underneath the plant's soil or mix in a speed-treating agent like " +
            "hydrated lime. You may need to raise or move your garden to ensure proper drainage.";
    private static final String BELOW_HUMIDITY_ADVICE = "This garden is below ideal humidity. Look out for wilting," +
            " stunted growth, smaller leaves, dry tip burn, leaf curl. If any of these persist try using a mister," +
            " fog unit or sprinkler on your garden.";
    private static final String ABOVE_HUMIDITY_ADVICE = "This garden is above the ideal humidity." +
            " Look out for soft growth, increase foliar disease, nutrient deficiencies, increased root disease," +
            " oedema (water spots on leaves, watery swellings or growths that rupture into powdery patches) and edge burn." +
            " If any if these persist add a fan to your garden.";

    private static final String IDEAL_ADVICE = "This garden has an ideal ";

    private final List<String> temperatureReferences = new ArrayList<>();
    private final List<String> moistureReferences = new ArrayList<>();
    private final List<String> humidityReferences = new ArrayList<>();


    /**
     * Construct sensor advice object.
     *
     * @param dayData      Contains lists of sensor readings in the last 24 hours.
     * @param adviceRanges Garden's advice ranges (min/max for sensors)
     */
    public SensorAdviceMessages(FormattedGraphData dayData, AdviceRanges adviceRanges) {
        this.dayData = dayData;
        this.adviceRanges = adviceRanges;

        temperatureReferences.add("\"Let's Grow a Garden! Part 1: Temperature, A Key Factor for Growing Our Food\" : \"https://www.womeninagscience.org/post/let-s-grow-a-garden-part-1-temperature-a-key-factor-for-growing-our-food#:~:text=Temperature%20is%20an%20important%20factor,from%2040%20to%2085%20F.\"");
        temperatureReferences.add("\"5 Warning Signs Your Plants Are Experiencing Cold Shock\" : \"https://garrettchurchill.com/5-warning-signs-your-plants-are-experiencing-cold-shock/\"");
        temperatureReferences.add("\"Heat wave in the garden: How to identify and prevent heat stress in plants\" : \"https://extension.oregonstate.edu/gardening/flowers-shrubs-trees/heat-wave-garden-how-identify-prevent-heat-stress-plants#:~:text=Above%20104%C2%B0F%2C%20many,activity%20and%20growth%20in%20plants.\"");

        moistureReferences.add("\"DIY Natural\" : \"https://diynatural.com/soil-moisture/\"");
        moistureReferences.add("\"Evergreen Seeds\" : \"https://www.evergreenseeds.com/how-to-fix-dry-soil/#The_Role_of_Organic_Matter_in_Soil_Structure\"");
        moistureReferences.add("\"Gardening Know How\" : \"https://www.gardeningknowhow.com/garden-how-to/soil-fertilizers/waterlogged-soil-fixes\"");

        humidityReferences.add("\"The Green Pages: Humidity, temperature and watering\" : \"https://espacepourlavie.ca/en/humidity-temperature-and-watering#:~:text=Most%20plants%20grow%20best%20with,tolerate%20humidity%20levels%20below%2025%25\"");
        humidityReferences.add("\"7 Mistakes Growers Make with Humidity\" : \"https://www.shogunfertilisers.com/en/growing-information/blogs/7-mistakes-growers-make-with-humidity/#:~:text=Generally%2C%20as%20the%20temperature%20increases,aim%20for%2055%2D70%25.\"");
    }


    /**
     * Helper method to identify the type of advice needed.
     *
     * @param sensorReadings List of sensor readings for a particular sensor
     * @param sensorMin      Minimum advice range for that sensor
     * @param sensorMax      Maximum advice range for that sensor
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
     * @param sensorName     Name of sensor. This is what is shown to the user.
     * @param sensorReadings List of sensor readings for that sensor.
     * @param minRange       Minimum advice range for that sensor
     * @param maxRange       Maximum advice range for that sensor
     * @param belowAdvice    Message for a point being below the advice range for that sensor
     * @param aboveAdvice    Message for a point being above the advice range for that sensor
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
     *
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
            model.addAttribute("temperatureReference", this.temperatureReferences);
        }
    }

    /**
     * Add soil moisture advice and references to the model.
     *
     * @param model Model to add attributes to
     */
    public void addSoilMoistureAdviceToModel(Model model) {
        String advice = getAdvice("soil moisture", dayData.getMoisture(),
                adviceRanges.getMinMoisture(), adviceRanges.getMaxMoisture(),
                BELOW_MOISTURE_ADVICE, ABOVE_MOISTURE_ADVICE);

        if (!advice.isEmpty()) {
            model.addAttribute("moistureAdvice", advice);
            model.addAttribute("moistureReference", this.moistureReferences);
        }
    }

    /**
     * Add humidity advice and references to the model.
     *
     * @param model Model to add attributes to
     */
    public void addHumidityAdviceToModel(Model model) {
        String advice = getAdvice("humidity", dayData.getHumidity(),
                adviceRanges.getMinHumidity(), adviceRanges.getMaxHumidity(),
                BELOW_HUMIDITY_ADVICE, ABOVE_HUMIDITY_ADVICE);
        if (!advice.isEmpty()) {
            model.addAttribute("humidityAdvice", advice);
            model.addAttribute("humidityReference", this.humidityReferences);
        }
    }
}
