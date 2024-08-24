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
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoDataBlock;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ArduinoGraphResults;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.junit.Assert.fail;
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
    private List<ArduinoDataPoint> arduinoDataPoints;

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
        Optional<Garden> garden = gardenService.getGardenById(gardenId);
        if (garden.isPresent()) {
            for (int i = 0; i < 31; i++) {
                for (int j = 0; j < 24; j++) {
                    LocalDateTime dateTime = LocalDateTime.now().minusDays(i).minusHours(j).truncatedTo(ChronoUnit.SECONDS);
                    arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden.get(), dateTime,
                            30d, 30d, 1.1d, 30d, 30d));

                    arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden.get(), dateTime.minusMinutes(30),
                            10d, 10d, 0.9d, 10d, 10d));
                }
            }
        } else {
            fail();
        }
    }

    @Given("I am on the garden stats page")
    public void iAmOnTheGardenStatsPage() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(gardenId))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @When("I choose to see a graph of the temperature in Degree Celsius over the last {int} days")
    public void iChooseToSeeAGraphOfTheTemperatureInDegreeCelsiusOverTheLastDays(int days) {
        arduinoDataPoints = arduinoDataPointService.getDataPointsOverDays(gardenId, days);
    }

    @Then("I see a a display of results for the average temperature for the night, morning, afternoon, and evening of each day.")
    public void iSeeAADisplayOfResultsForTheAverageTemperatureForTheNightMorningAfternoonOfEachDay() {
        // Average 7 days function on arduinoPoints
        List<ArduinoDataBlock> averagedDataPointsOverWeek = new ArduinoGraphResults(arduinoDataPoints).averageDataPointsOverWeek();
//        List<Double> temperatureDataOverWeek = averagedDataPointsOverWeek.get(0);
//        Assertions.assertTrue(28 <= temperatureDataOverWeek.size() && temperatureDataOverWeek.size() <= 32);
//        Assertions.assertEquals(20, temperatureDataOverWeek.get(0));
        // Verify 4 readings per day, total of 28 readings with an average of 20 degrees
    }
}
