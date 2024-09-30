package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.AdviceSharedState;
import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
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
    private AdviceSharedState adviceSharedState;

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
                .andExpect(MockMvcResultMatchers.model().attribute("moistureAdvice",
                        "This garden has an ideal soil moisture."));
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
                    10d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message that tells me to water my garden")
    public void iAmShownAMessageThatTellsMeToWaterMyGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                    .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("moistureAdvice",
                    "This garden is below the ideal soil moisture. " +
                            "Overly dry soil makes it difficult for plants to absorb moisture decreases the availability of " +
                            "nutrients. If you are watering your garden regularly, try adding compost into your soil, or creating " +
                            "a layer of compost on top of the soil to prevent the sun from evaporating water from the soil's surface."));
    }

    @When("the soil moisture reading has gone above some maximum value in the last day")
    public void theSoilMoistureReadingHasGoneAboveSomeMaximumValueInTheLastDay() {
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
            if (!currentTime.isEqual(startTime.plusMinutes(25))) {
                arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                        garden,
                        currentTime,
                        30d,
                        40d,
                        1d,
                        60d,
                        50d
                ));
            } else {
                arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                        garden,
                        currentTime,
                        30d,
                        40d,
                        1d,
                        60d,
                        90d
                ));
            }
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message providing tips for very moist soil.")
    public void iAmShownAMessageProvidingTipsForVeryMoistSoil() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.model().attribute("moistureAdvice",
            "This garden is above the ideal soil moisture. " +
                    "Overly moist soil can create leaching of nutrients, root rot, fungal problems, and prevent growth. " +
                    "Try adding organic matter/mulch underneath the plant's soil or mix in a speed-treating agent like " +
                    "hydrated lime. You may need to raise or move your garden to ensure proper drainage."));
    }
}
