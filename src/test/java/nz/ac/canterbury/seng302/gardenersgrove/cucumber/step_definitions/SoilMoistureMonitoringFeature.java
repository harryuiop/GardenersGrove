package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.AdviceSharedState;
import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class SoilMoistureMonitoringFeature {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdviceSharedState adviceSharedState;

    private Authentication auth;

    private Long gardenId;

    private ResultActions resultActions;

    private FormattedGraphData formattedWeekResults;

    private FormattedGraphData formattedDayResults;

    private FormattedGraphData formattedMonthResults;


    @When("the soil moisture reading has stayed within some optimal temperature range for the past day")
    public void theSoilMoistureReadingHasStayedWithinSomeOptimalTemperatureRangeForThePastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxMoisture(80);
        adviceRanges.setMinMoisture(20);
        gardenService.saveGarden(garden);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    60d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message saying that the garden is at an ideal moisture.")
    public void iAmShownAMessageSayingThatTheGardenIsAtAnIdealMoisture() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("temperatureAdvice",
                        "This garden is at an ideal moisture."));
    }

    @When("the soil moisture reading has gone below some minimum value in the last day")
    public void theSoilMoistureReadingHasGoneBelowSomeMinimumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxMoisture(80);
        adviceRanges.setMinMoisture(20);
        gardenService.saveGarden(garden);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    60d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(25);
        }

        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                garden,
                LocalDateTime.now().minusHours(5).minusMinutes(3),
                30d,
                10d,
                1d,
                60d,
                50d
        ));
    }

    @Then("I am shown a message that tells me to water my garden")
    public void iAmShownAMessageThatTellsMeToWaterMyGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                    .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("temperatureAdvice",
                    "A temperature reading in the last 24 hours dropped " +
                            "below the set advice range. Cold temperatures can make plants go dormant or cause damage. " +
                            "Look out for discoloured or wilting leaves, root ball damage, split in steam or trunk and " +
                            "stunted growth. If you notice any of these signs, trim dead roots or repot the plant. " +
                            "Do not fertilize, overwrite, or over-trim the plant while it heals."));
    }

    @When("the soil moisture reading has gone above some maximum value in the last day")
    public void theSoilMoistureReadingHasGoneAboveSomeMaximumValueInTheLastDay() {

    }

    @Then("I am shown a message providing tips for very moist soil.")
    public void iAmShownAMessageProvidingTipsForVeryMoistSoil() {
    }
}
