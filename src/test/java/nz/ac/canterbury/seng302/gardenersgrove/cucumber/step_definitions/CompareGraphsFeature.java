package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.And;
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
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CompareGraphsFeature {
    @Autowired
    private GardenService gardenService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ArduinoDataPointService arduinoDataPointService;
    private Long gardenId;
    private Long garden2Id;

    private Authentication auth;
    @Given("I have a user that is logged in with a monitored garden")
    public void iHaveAUserThatIsLoggedInWithAMonitoredGarden() {
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
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    @And("I have a garden with a connected Arduino")
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

    @Given("There is another user with a monitored garden and connected arduino")
    public void thereIsAnotherUserWithAMonitoredGardenAndConnectedArduino() {
        String user2Email = "user2@gmail.com";
        User user2;
        if (userService.getUserByEmail(user2Email) == null) {
            user2 = new User(user2Email, "Second", "User", "Password1!", "");
            user2.setConfirmation(true);
            userService.addUsers(user2);
        } else {
            user2 = userService.getUserByEmail(user2Email);
        }

        Location location = new Location("New Zealand", "Auckland");
        Garden garden = new Garden(user2, "Test", "", location, null, true);
        garden.setIsGardenPublic(true);
        gardenService.saveGarden(garden);
        garden2Id = garden.getId();

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

    @When("I navigate to another user's garden monitoring page for one of their gardens")
    public void iNavigateToAnotherUserSGardenMonitoringPageForOneOfTheirGardens() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden2Id))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Then("there is a dropdown containing the list of all my gardens to compare with the viewed garden")
    public void thereIsADropdownContainingTheListOfAllMyGardensToCompareWithTheViewedGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(garden2Id))
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Given("I have a second garden with a connected Arduino")
    public void iHaveASecondGardenWithAConnectedArduino() {
    }

    @When("I navigate to the garden monitoring page for one of my gardens")
    public void iNavigateToTheGardenMonitoringPageForOneOfMyGardens() {
    }

    @Then("there is a dropdown containing all of my other gardens to compare with the viewed garden")
    public void thereIsADropdownContainingAllOfMyOtherGardensToCompareWithTheViewedGarden() {
    }

    @And("I select another of my gardens from the comparison dropdown")
    public void iSelectAnotherOfMyGardensFromTheComparisonDropdown() {
    }

    @Then("I am shown a page with the comparison of both gardens")
    public void iAmShownAPageWithTheComparisonOfBothGardens() {
    }

    @And("I am on the comparison page between two of my gardens")
    public void iAmOnTheComparisonPageBetweenTwoOfMyGardens() {
    }

    @When("I select the No Comparison option from the comparison dropdown")
    public void iSelectTheNoComparisonOptionFromTheComparisonDropdown() {
    }

    @Then("I am taken back to the monitor garden page for my original garden")
    public void iAmTakenBackToTheMonitorGardenPageForMyOriginalGarden() {
    }

}
