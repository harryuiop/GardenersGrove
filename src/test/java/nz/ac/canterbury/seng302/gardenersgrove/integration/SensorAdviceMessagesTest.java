package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import nz.ac.canterbury.seng302.gardenersgrove.utility.AdviceMessage;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import nz.ac.canterbury.seng302.gardenersgrove.utility.SensorAdviceMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SensorAdviceMessagesTest {

    AdviceRanges adviceRanges;

    static Stream<Arguments> allSensorsInRange() {
        return Stream.of(
                Arguments.of(List.of(
                        Arrays.asList(11d, 15d, 18d, 24d, 25d),
                        Arrays.asList(11d, 15d, 18d, 24d, 25d),
                        Arrays.asList(11d, 15d, 18d, 24d, 25d),
                        Arrays.asList(11d, 15d, 18d, 24d, 25d),
                        Arrays.asList(11d, 15d, 18d, 24d, 25d)
                )),
                Arguments.of(List.of(
                        Arrays.asList(10d, 35d),
                        Arrays.asList(10d, 35d),
                        Arrays.asList(10d, 35d),
                        Arrays.asList(10d, 35d),
                        Arrays.asList(10d, 35d))),
                Arguments.of(List.of(Arrays.asList(null, 25d, null),
                        Arrays.asList(null, 25d, null),
                        Arrays.asList(null, 25d, null),
                        Arrays.asList(null, 25d, null),
                        Arrays.asList(null, 25d, null)))
        );
    }

    static Stream<Arguments> outOfRangeBelow() {
        return Stream.of(
                Arguments.of(List.of(
                        Arrays.asList(9d, 5d, 2d),
                        Arrays.asList(9d, 5d, 2d),
                        Arrays.asList(9d, 5d, 2d),
                        Arrays.asList(9d, 5d, 2d),
                        Arrays.asList(9d, 5d, 2d)
                )),
                Arguments.of(List.of(
                        Arrays.asList(10d, 5d, 35d),
                        Arrays.asList(10d, 5d, 35d),
                        Arrays.asList(10d, 5d, 35d),
                        Arrays.asList(10d, 5d, 35d),
                        Arrays.asList(10d, 5d, 35d))),
                Arguments.of(List.of(Arrays.asList(null, 9d, null),
                        Arrays.asList(null, 9d, null),
                        Arrays.asList(null, 9d, null),
                        Arrays.asList(null, 9d, null),
                        Arrays.asList(null, 9d, null)))
        );
    }

    static Stream<Arguments> outOfRangeAbove() {
        return Stream.of(
                Arguments.of(List.of(
                        Arrays.asList(37d, 45d, 50d),
                        Arrays.asList(37d, 45d, 50d),
                        Arrays.asList(37d, 45d, 50d),
                        Arrays.asList(37d, 45d, 50d),
                        Arrays.asList(37d, 45d, 50d)
                )),
                Arguments.of(List.of(
                        Arrays.asList(10d, 50d, 35d),
                        Arrays.asList(10d, 50d, 35d),
                        Arrays.asList(10d, 50d, 35d),
                        Arrays.asList(10d, 50d, 35d),
                        Arrays.asList(10d, 50d, 35d))),
                Arguments.of(List.of(Arrays.asList(null, 50d, null),
                        Arrays.asList(null, 50d, null),
                        Arrays.asList(null, 50d, null),
                        Arrays.asList(null, 50d, null),
                        Arrays.asList(null, 50d, null)))
        );
    }

    static Stream<Arguments> outOfRangeBelowAndAbove() {
        return Stream.of(

                Arguments.of(List.of(
                        Arrays.asList(37d, 45d, 50d, 8d, 5d, 6d),
                        Arrays.asList(37d, 45d, 50d, 8d, 5d, 6d),
                        Arrays.asList(37d, 45d, 50d, 8d, 5d, 6d),
                        Arrays.asList(37d, 45d, 50d, 8d, 5d, 6d),
                        Arrays.asList(37d, 45d, 50d, 8d, 5d, 6d)
                )),
                Arguments.of(List.of(
                        Arrays.asList(10d, 50d, 35d, 8d),
                        Arrays.asList(10d, 50d, 35d, 8d),
                        Arrays.asList(10d, 50d, 35d, 8d),
                        Arrays.asList(10d, 50d, 35d, 8d),
                        Arrays.asList(10d, 50d, 35d, 8d))),
                Arguments.of(List.of(Arrays.asList(null, 50d, null, 8d),
                        Arrays.asList(null, 50d, null, 8d),
                        Arrays.asList(null, 50d, null, 8d),
                        Arrays.asList(null, 50d, null, 8d),
                        Arrays.asList(null, 50d, null, 8d)))
        );
    }

    @BeforeAll
    public void setUp() {
        adviceRanges = new AdviceRanges();
        adviceRanges.setMaxTemperature(35);
        adviceRanges.setMinTemperature(10);

        adviceRanges.setMaxHumidity(35);
        adviceRanges.setMinHumidity(10);

        adviceRanges.setMaxMoisture(35);
        adviceRanges.setMinMoisture(10);
    }

    @ParameterizedTest
    @MethodSource("allSensorsInRange")
    public void getTemperatureAdvice_inRange_returnIdealAdviceMessage(List<List<Double>> allSensorsInRange) {
        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(
                new FormattedGraphData(allSensorsInRange, new ArrayList<>()), adviceRanges);

        AdviceMessage adviceMessage = sensorAdviceMessages.getTemperatureAdvice();

        Assertions.assertEquals("This garden has an ideal temperature.", adviceMessage.getAdviceMessage());
    }

    @ParameterizedTest
    @MethodSource("allSensorsInRange")
    public void getHumidityAdvice_inRange_returnIdealAdviceMessage(List<List<Double>> allSensorsInRange) {
        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(
                new FormattedGraphData(allSensorsInRange, new ArrayList<>()), adviceRanges);

        AdviceMessage adviceMessage = sensorAdviceMessages.getHumidityAdvice();

        Assertions.assertEquals(adviceMessage.getAdviceMessage(), "This garden has an ideal humidity.");
    }

    @ParameterizedTest
    @MethodSource("allSensorsInRange")
    public void getMoistureAdvice_inRange_returnIdealAdviceMessage(List<List<Double>> allSensorsInRange) {
        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(
                new FormattedGraphData(allSensorsInRange, new ArrayList<>()), adviceRanges);

        AdviceMessage adviceMessage = sensorAdviceMessages.getSoilMoistureAdvice();

        Assertions.assertEquals("This garden has an ideal soil moisture.", adviceMessage.getAdviceMessage());
    }

    @ParameterizedTest
    @MethodSource("outOfRangeBelow")
    public void getTemperatureAdvice_belowRange_returnBelowAdviceMessage(List<List<Double>> outOfRangeBelow) {
        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(
                new FormattedGraphData(outOfRangeBelow, new ArrayList<>()), adviceRanges);

        AdviceMessage adviceMessage = sensorAdviceMessages.getTemperatureAdvice();

        Assertions.assertEquals("A temperature reading in the last 24 hours dropped " +
                        "below the set advice range. Cold temperatures can make plants go dormant or cause damage. " +
                        "Look out for discoloured or wilting leaves, root ball damage, split in steam or trunk and " +
                        "stunted growth. If you notice any of these signs, trim dead roots or repot the plant. " +
                        "Do not fertilize, overwater, or over-trim the plant while it heals.",
                adviceMessage.getAdviceMessage());
    }

    @ParameterizedTest
    @MethodSource("outOfRangeAbove")
    public void getTemperatureAdvice_aboveRange_returnAboveAdviceMessage(List<List<Double>> outOfRangeAbove) {
        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(
                new FormattedGraphData(outOfRangeAbove, new ArrayList<>()), adviceRanges);

        AdviceMessage adviceMessage = sensorAdviceMessages.getTemperatureAdvice();

        Assertions.assertEquals("A temperature reading in the last 24 hours has " +
                        "gone above the set advice range. High temperatures can harm plants by slowing their growth" +
                        " and causing dehydration. This can lead to smaller, low-quality fruits and vegetables. Look " +
                        "for leaf rolling or cupping, wilting, dry leaf edges, sun-scald or bleached leaves. If any of " +
                        "these signs appear, water regularly, mulch, and provide shade. Do not transplant, prune or fertilize.",
                adviceMessage.getAdviceMessage());
    }

    @ParameterizedTest
    @MethodSource("outOfRangeBelowAndAbove")
    public void getTemperatureAdvice_belowAndAboveRange_returnAboveAdviceMessage(List<List<Double>> outOfRangeBelowAndAbove) {
        SensorAdviceMessages sensorAdviceMessages = new SensorAdviceMessages(
                new FormattedGraphData(outOfRangeBelowAndAbove, new ArrayList<>()), adviceRanges);

        AdviceMessage adviceMessage = sensorAdviceMessages.getTemperatureAdvice();

        Assertions.assertEquals("A temperature reading in the last 24 hours dropped " +
                        "below the set advice range. Cold temperatures can make plants go dormant or cause damage. " +
                        "Look out for discoloured or wilting leaves, root ball damage, split in steam or trunk and " +
                        "stunted growth. If you notice any of these signs, trim dead roots or repot the plant. " +
                        "Do not fertilize, overwater, or over-trim the plant while it heals.\n" +
                        "A temperature reading in the last 24 hours has " +
                        "gone above the set advice range. High temperatures can harm plants by slowing their growth" +
                        " and causing dehydration. This can lead to smaller, low-quality fruits and vegetables. Look " +
                        "for leaf rolling or cupping, wilting, dry leaf edges, sun-scald or bleached leaves. If any of " +
                        "these signs appear, water regularly, mulch, and provide shade. Do not transplant, prune or fertilize.",
                adviceMessage.getAdviceMessage());
    }


}
