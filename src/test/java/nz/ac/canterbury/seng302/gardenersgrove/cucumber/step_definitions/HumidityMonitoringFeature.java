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
public class HumidityMonitoringFeature {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private AdviceSharedState adviceSharedState;

    @When("the humidity reading has stayed within some optimal temperature range for the past day")
    public void theHumidityReadingHasStayedWithinSomeOptimalTemperatureRangeForThePastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxHumidity(80);
        adviceRanges.setMinHumidity(20);
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

    @Then("I am shown a message saying that the garden is currently at an ideal humidity.")
    public void iAmShownAMessageSayingThatTheGardenIsCurrentlyAtAnIdealHumidity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("humidityAdvice",
                        "This garden has an ideal humidity."));
    }

    @When("the humidity reading has gone below some minimum value in the last day")
    public void theHumidityReadingHasGoneBelowSomeMinimumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxHumidity(80);
        adviceRanges.setMinHumidity(20);
        gardenService.saveGarden(garden);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    5d,
                    1d,
                    50d,
                    60d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message informing me of symptoms to look for when the humidity is too low.")
    public void iAmShownAMessageInformingMeOfSymptomsToLookForWhenTheHumidityIsTooLow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("humidityAdvice",
                        "This garden is below ideal humidity. Look out for wilting," +
                                " stunted growth, smaller leaves, dry tip burn, leaf curl. If any of these persist try using a mister," +
                                " fog unit or sprinkler on your garden."));
    }

    @When("the humidity reading has gone above some maximum value in the last day")
    public void theHumidityReadingHasGoneAboveSomeMaximumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxHumidity(80);
        adviceRanges.setMinHumidity(20);
        gardenService.saveGarden(garden);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    90d,
                    1d,
                    50d,
                    60d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message informing me of symptoms to look for when the humidity is too high.")
    public void iAmShownAMessageInformingMeOfSymptomsToLookForWhenTheHumidityIsTooHigh() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("humidityAdvice",
                        "This garden is above the ideal humidity." +
                                " Look out for soft growth, increase foliar disease, nutrient deficiencies, increased root disease," +
                                " oedema (water spots on leaves, watery swellings or growths that rupture into powdery patches) and edge burn." +
                                " If any if these persist add a fan to your garden."));
    }

    @When("the humidity reading has gone above some maximum and below some minimum value in the last day")
    public void theHumidityReadingHasGoneAboveSomeMaximumAndBelowSomeMinimumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxHumidity(80);
        adviceRanges.setMinHumidity(20);
        gardenService.saveGarden(garden);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime currentTime = startTime;
        int counter = 0;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                garden,
                currentTime,
                30d,
                counter % 2 == 0 ? 5d : 95d,
                1d,
                50d,
                60d
        ));
        counter++;
        currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message informing me of symptoms to look for when the humidity is too high and too low.")
    public void iAmShownAMessageInformingMeOfSymptomsToLookForWhenTheHumidityIsTooHighAndTooLow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("humidityAdvice",
                        "This garden is below ideal humidity. Look out for wilting, stunted growth, smaller leaves, dry tip burn, leaf curl. If any of these persist try using a mister, fog unit or sprinkler on your garden.\n" +
                                "This garden is above the ideal humidity. Look out for soft growth, increase foliar disease, nutrient deficiencies, increased root disease, oedema (water spots on leaves, watery swellings or growths that rupture into powdery patches) and edge burn. If any if these persist add a fan to your garden."));
    }
}
