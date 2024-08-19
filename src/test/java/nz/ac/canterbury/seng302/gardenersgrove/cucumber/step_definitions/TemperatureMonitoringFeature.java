package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.RunCucumberTest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Temperature;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.TemperatureService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.lang.Double.parseDouble;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TemperatureMonitoringFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    private Authentication auth;

    private Long gardenId;
    private String graphData;

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
        for (int i=0; i < 31; i++){
            temperatureService.addTemperature(new Temperature(Date.valueOf(LocalDate.now().minusDays(i)), Time.valueOf(LocalTime.of(12,10,33)), 15d));
            temperatureService.addTemperature(new Temperature(Date.valueOf(LocalDate.now().minusDays(i)), Time.valueOf(LocalTime.of(12,40,33)), 9d));
            temperatureService.addTemperature(new Temperature(Date.valueOf(LocalDate.now().minusDays(i)), Time.valueOf(LocalTime.of(13,10,33)), 6d));
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
        graphData = temperatureService.getGraphData(days);
    }

    @Then("I see a a display of the results for the average temperature for each day \\(average taken from each recorded 30 minute period).")
    public void iSeeAADisplayOfTheResultsForTheAverageTemperatureForEachDayAverageTakenFromEachRecordedMinutePeriod() {
        String[] list = graphData.split(",");
        for (String s : list) {
            Assertions.assertEquals(10d, parseDouble(s));
        }
    }

    @Given("I have a garden with a disconnected arduino")
    public void iHaveAGardenWithADisconnectedArduino() {
    }

    @When("I view the garden stats page")
    public void iViewTheGardenStatsPage() {
    }

    @Then("all data that was previously collected still appears in the graph")
    public void allDataThatWasPreviouslyCollectedStillAppearsInTheGraph() {
    }
}
