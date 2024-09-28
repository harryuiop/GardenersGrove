package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.RunCucumberTest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.*;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TemperatureMonitoringFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    private Authentication auth;

    private Long gardenId;

    private ResultActions resultActions;
    private FormattedGraphData formattedWeekResults;

    private FormattedGraphData formattedDayResults;

    private FormattedGraphData formattedMonthResults;


    @Given("I have a logged in user with a monitored garden")
    public void iHaveALoggedInUserWithAMonitoredGarden() {
        String email = "test@gmail.com";
        User user;
        if (userService.getUserByEmail(email) == null) {
            user = new User(email, "Test", "User", "Password1!", "");
            user.setConfirmation(true);
            userService.addUsers(user);
        } else {
            user = userService.getUserByEmail(email);
        }
        Location location = new Location("New Zealand", "Auckland");
        Garden garden = new Garden(user, "Test", "", location, null, true);
        gardenService.saveGarden(garden);
        gardenId = garden.getId();
        auth = RunCucumberTest.authMaker.accept(user.getEmail(), "Password1!", userService);
    }

    @Given("I have a garden with a connected arduino")
    public void iHaveAGardenWithAConnectedArduino() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        LocalDateTime startTime = LocalDateTime.of(2023, 12, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 10, 0, 0);
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    60d,
                    70d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Given("I am on the garden stats page")
    public void iAmOnTheGardenStatsPage() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(monitorGardenUri(gardenId)).with(csrf())
        );
    }

    @When("I choose to see a graph of the temperature in Degree Celsius over the last seven days")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheSevenLastDays() {
        formattedWeekResults = arduinoDataPointService.getWeekGraphData(gardenId,
                LocalDateTime.of(2024, 1, 10, 0, 0));
    }

    // AC3
    @Then("I see a a display of results for the average temperature for the night, morning, afternoon, and evening of each day.")
    public void iSeeAADisplayOfResultsForTheAverageTemperatureForTheNightMorningAfternoonOfEachDay() {
        List<List<Double>> expectedData = Arrays.asList(
                Arrays.asList(30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0),
                Arrays.asList(40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0),
                Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
                Arrays.asList(60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0),
                Arrays.asList(70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0)
        );

        List<String> expectedLabels = Arrays.asList(
                "\"Jan 3 Wed\"", "\"Jan 4 Thu\"", "\"Jan 5 Fri\"", "\"Jan 6 Sat\"", "\"Jan 7 Sun\"",
                "\"Jan 8 Mon\"", "\"Jan 9 Tue\"", "\"Jan 10 Wed\""
        );

        Assertions.assertEquals(expectedData, formattedWeekResults.getSensorReadings());
        Assertions.assertEquals(expectedLabels, formattedWeekResults.getLabels());
    }

    // AC2
    @When("I choose to see a graph of the temperature in Degree Celsius over the last day")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheLastDay() {
        formattedDayResults = arduinoDataPointService.getDayGraphData(gardenId,
                LocalDateTime.of(2024, 1, 10, 12, 0));
    }

    // AC2
    @Then("I see a display of results for the average temperature for each half hour of that day.")
    public void iSeeADisplayOfResultsForTheAverageTemperatureForEachHalfHourOfThatDay() {
        List<List<Double>> expectedData = Arrays.asList(
                Arrays.asList(30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        );

        List<String> expectedLabels = Arrays.asList(
                "\"Tue 12:30\"", "\"Tue 13:0\"", "\"Tue 13:30\"", "\"Tue 14:0\"", "\"Tue 14:30\"",
                "\"Tue 15:0\"", "\"Tue 15:30\"", "\"Tue 16:0\"", "\"Tue 16:30\"", "\"Tue 17:0\"",
                "\"Tue 17:30\"", "\"Tue 18:0\"", "\"Tue 18:30\"", "\"Tue 19:0\"", "\"Tue 19:30\"",
                "\"Tue 20:0\"", "\"Tue 20:30\"", "\"Tue 21:0\"", "\"Tue 21:30\"", "\"Tue 22:0\"",
                "\"Tue 22:30\"", "\"Tue 23:0\"", "\"Tue 23:30\"", "\"Wed 0:0\"", "\"Wed 0:30\"",
                "\"Wed 1:0\"", "\"Wed 1:30\"", "\"Wed 2:0\"", "\"Wed 2:30\"", "\"Wed 3:0\"",
                "\"Wed 3:30\"", "\"Wed 4:0\"", "\"Wed 4:30\"", "\"Wed 5:0\"", "\"Wed 5:30\"",
                "\"Wed 6:0\"", "\"Wed 6:30\"", "\"Wed 7:0\"", "\"Wed 7:30\"", "\"Wed 8:0\"",
                "\"Wed 8:30\"", "\"Wed 9:0\"", "\"Wed 9:30\"", "\"Wed 10:0\"", "\"Wed 10:30\"",
                "\"Wed 11:0\"", "\"Wed 11:30\"", "\"Wed 12:0\""
        );

        Assertions.assertEquals(expectedData, formattedDayResults.getSensorReadings());
        Assertions.assertEquals(expectedLabels, formattedDayResults.getLabels());
    }

    // AC4
    @When("I choose to see a graph of the temperature in Degree Celsius over the last thirty days")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheLastThirtyDays() {
        formattedMonthResults = arduinoDataPointService.getMonthGraphData(gardenId,
                LocalDateTime.of(2024, 1, 10, 0, 0));
    }

    // AC4
    @Then("I see a a display of the results for the average temperature for each day.")
    public void iSeeAADisplayOfTheResultsForTheAverageTemperatureForEachDay() {
        List<List<Double>> expectedData = Arrays.asList(
                Arrays.asList(30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, null),
                Arrays.asList(40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, null),
                Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, null),
                Arrays.asList(60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, null),
                Arrays.asList(70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, null)
        );

        List<String> expectedLabels = Arrays.asList(
                "\"Dec 11\"", "\"Dec 12\"", "\"Dec 13\"", "\"Dec 14\"", "\"Dec 15\"",
                "\"Dec 16\"", "\"Dec 17\"", "\"Dec 18\"", "\"Dec 19\"", "\"Dec 20\"",
                "\"Dec 21\"", "\"Dec 22\"", "\"Dec 23\"", "\"Dec 24\"", "\"Dec 25\"",
                "\"Dec 26\"", "\"Dec 27\"", "\"Dec 28\"", "\"Dec 29\"", "\"Dec 30\"",
                "\"Dec 31\"", "\"Jan 1\"", "\"Jan 2\"", "\"Jan 3\"", "\"Jan 4\"",
                "\"Jan 5\"", "\"Jan 6\"", "\"Jan 7\"", "\"Jan 8\"", "\"Jan 9\"",
                "\"Jan 10\""
        );

        Assertions.assertEquals(expectedData, formattedMonthResults.getSensorReadings());
        Assertions.assertEquals(expectedLabels, formattedMonthResults.getLabels());
    }

    @When("the temperature reading has stayed within some optimal temperature range for the past day")
    public void theTemperatureReadingHasStayedWithinSomeOptimalTemperatureRangeForThePastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxTemperature(35);
        adviceRanges.setMinTemperature(25);
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
                    70d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I receive a message saying that the garden is currently at an ideal temperature")
    public void i_receive_a_message_saying_that_the_garden_is_currently_at_an_ideal_temperature() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(gardenId))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("temperatureAdvice",
                        "This garden has an ideal temperature."));
    }

    @When("the temperature reading has gone below some minimum value in the last day")
    public void theTemperatureReadingHasGoneBelowSomeMinimumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxTemperature(35);
        adviceRanges.setMinTemperature(31);
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
                    70d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message informing me of symptoms to look for when plants get too cold")
    public void iAmShownAMessageInformingMeOfSymptomsToLookForWhenPlantsGetTooCold() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(gardenId))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("temperatureAdvice",
                        "A temperature reading in the last 24 hours dropped " +
                                "below the set advice range. Cold temperatures can make plants go dormant or cause damage. " +
                                "Look out for discoloured or wilting leaves, root ball damage, split in steam or trunk and " +
                                "stunted growth. If you notice any of these signs, trim dead roots or repot the plant. " +
                                "Do not fertilize, overwater, or over-trim the plant while it heals."));
    }

    @When("the temperature reading has gone above some maximum value in the last day")
    public void theTemperatureReadingHasGoneAboveSomeMaximumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxTemperature(29);
        adviceRanges.setMinTemperature(25);
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
                    70d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I am shown a message informing me of symptoms to look for when plants get too hot")
    public void iAmShownAMessageInformingMeOfSymptomsToLookForWhenPlantsGetTooHot() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(gardenId))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("temperatureAdvice",
                        "A temperature reading in the last 24 hours has " +
                                "gone above the set advice range. High temperatures can harm plants by slowing their growth" +
                                " and causing dehydration. This can lead to smaller, low-quality fruits and vegetables. Look " +
                                "for leaf rolling or cupping, wilting, dry leaf edges, sunscald or bleached leaves. If any of " +
                                "these signs appear, water regularly, mulch, and provide shade. Do not transplant, prune or fertilize."));
    }

    @When("the temperature reading has gone both above the maximum value and below the minimum value in the last day")
    public void theTemperatureReadingHasGoneBothAboveTheMaximumValueAndBelowTheMinimumValueInTheLastDay() {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        AdviceRanges adviceRanges = garden.getAdviceRanges();
        adviceRanges.setMaxTemperature(35);
        adviceRanges.setMinTemperature(25);
        gardenService.saveGarden(garden);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().minusHours(6);
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    60d,
                    70d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                garden,
                currentTime,
                40d,
                40d,
                1d,
                60d,
                70d
        ));
        currentTime = currentTime.plusMinutes(45);
        arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                garden,
                currentTime,
                20d,
                40d,
                1d,
                60d,
                70d
        ));
    }

    @Then("I am shown a message informing me of the plant symptoms that occur during high temperature fluctuations")
    public void iAmShownAMessageInformingMeOfThePlantSymptomsThatOccurDuringHighTemperatureFluctuations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(gardenId))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("temperatureAdvice",
                        "A temperature reading in the last 24 hours dropped " +
                                "below the set advice range. Cold temperatures can make plants go dormant or cause damage. " +
                                "Look out for discoloured or wilting leaves, root ball damage, split in steam or trunk and " +
                                "stunted growth. If you notice any of these signs, trim dead roots or repot the plant. " +
                                "Do not fertilize, overwater, or over-trim the plant while it heals." +
                                "\nA temperature reading in the last 24 hours has " +
                                "gone above the set advice range. High temperatures can harm plants by slowing their growth" +
                                " and causing dehydration. This can lead to smaller, low-quality fruits and vegetables. Look " +
                                "for leaf rolling or cupping, wilting, dry leaf edges, sunscald or bleached leaves. If any of " +
                                "these signs appear, water regularly, mulch, and provide shade. Do not transplant, prune or fertilize."));
    }

    @Then("I see the current temperature")
    public void iSeeTheCurrentTemperature() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("tempReading"));
    }
}
