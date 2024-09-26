package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CompareGraphsFeature {
    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;
    private Long garden2Id;

    private Authentication auth;
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
        gardenService.saveGarden(garden);
        garden2Id = garden.getId();
    }

    @When("I navigate to another user's garden monitoring page for one of their gardens")
    public void iNavigateToAnotherUserSGardenMonitoringPageForOneOfTheirGardens() {
    }

    @Then("there is a dropdown containing the list of all my gardens to compare with the viewed garden")
    public void thereIsADropdownContainingTheListOfAllMyGardensToCompareWithTheViewedGarden() {
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
