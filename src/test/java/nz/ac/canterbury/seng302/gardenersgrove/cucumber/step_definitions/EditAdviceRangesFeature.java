package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.RunCucumberTest;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class EditAdviceRangesFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    private Authentication auth;
    private User user;
    private ResultActions resultActions;

    @Given("I am logged in to gardeners grove")
    public void iAmLoggedInToGardenersGrove() {
        String email = "test@gmail.com";
        user = userService.getUserByEmail(email);
        if (user == null) {
            user = new User(email, "Test", "User", "Password1!", "");
            user.setConfirmation(true);
            userService.addUsers(user);
        }
        auth = RunCucumberTest.authMaker.accept(user.getEmail(), "Password1!", userService);
    }

    @And("I am on the garden monitoring page for my own garden")
    public void iAmOnTheGardenMonitoringPageForMyOwnGarden() throws Exception {
        Location location = new Location("New Zealand", "Auckland");
        Garden garden = new Garden(user, "Test", "", location, null, true);
        gardenService.saveGarden(garden);

        SecurityContextHolder.getContext().setAuthentication(auth);
        resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(monitorGardenUri(garden.getId())).with(csrf())
        );
    }

    @When("I open the settings modal")
    public void iOpenTheSettingsModal() {

    }

    @Then("I can see the advice ranges are pre-populated with the current ranges")
    public void iCanSeeTheAdviceRangesArePrePopulatedWithTheCurrentRanges() throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.model().attributeExists("savedAdviceRanges"));
    }
}
