package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.RunCucumberTest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(gardenId))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @When("I choose to see a graph of the temperature in Degree Celsius over the last seven days")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheSevenLastDays() {
        formattedWeekResults = arduinoDataPointService.getWeekGraphData(gardenId,
                LocalDateTime.of(2024, 1, 10, 0, 0));
    }

    @Then("I see a a display of results for the average temperature for the night, morning, afternoon, and evening of each day.")
    public void iSeeAADisplayOfResultsForTheAverageTemperatureForTheNightMorningAfternoonOfEachDay() {
        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(
                        30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, null
                ),
                Arrays.asList(
                        40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, null
                ),
                Arrays.asList(
                        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, null
                ),
                Arrays.asList(
                        60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, null
                ),
                Arrays.asList(
                        70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, null
                )
        );
        Assertions.assertEquals(expected, formattedWeekResults.getSensorReadings());

    }

    @When("I choose to see a graph of the temperature in Degree Celsius over the last day")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheLastDay() {
        formattedDayResults = arduinoDataPointService.getDayGraphData(gardenId,
                LocalDateTime.of(2024, 1, 10, 12, 0));
    }

    @Then("I see a a display of results for the average temperature for each half hour of that day.")
    public void iSeeAADisplayOfResultsForTheAverageTemperatureForEachHalfHourOfThatDay() {
        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                Arrays.asList(70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
        );
        Assertions.assertEquals(expected, formattedDayResults.getSensorReadings());
    }

    @When("I choose to see a graph of the temperature in Degree Celsius over the last thirty days")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheLastThirtyDays() {
        formattedMonthResults = arduinoDataPointService.getMonthGraphData(gardenId,
                LocalDateTime.of(2024, 1, 10, 0, 0));
    }


    @Then("I see a a display of the results for the average temperature for each day.")
    public void iSeeAADisplayOfTheResultsForTheAverageTemperatureForEachDay() {
        List<List<Double>> expected = Arrays.asList(
                Arrays.asList(
                        null, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, 30.0, null
                ),
                Arrays.asList(
                        null, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, null
                ),
                Arrays.asList(
                        null, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, null
                ),
                Arrays.asList(
                        null, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, 60.0, null
                ),
                Arrays.asList(
                        null, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, null
                )
        );
        Assertions.assertEquals(expected, formattedMonthResults.getSensorReadings());
    }
}
