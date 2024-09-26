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
import nz.ac.canterbury.seng302.gardenersgrove.utility.GardenPlantSuggestions;
import org.junit.jupiter.api.Assertions;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewGardenUri;
import static nz.ac.canterbury.seng302.gardenersgrove.utility.GardenPlantSuggestions.getSuggestions;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class PlantSuggestionsFeature {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    private GardenPlantSuggestions gardenPlantSuggestions;

    String expectedPrompt = "Given me 3 plant suggestions given my garden has, Temperature between -10.0C-42.0C, " +
                            "Moisture between 3.0%-55.0%, Light between 5.0%-57.0%, Air Pressure between 0.1atm-0.62atm, " +
                            "Humidity between 0.0%-52.0%";
    Garden garden;
    User user;
    Authentication auth;
    ResultActions result;

    @Given("there is 14 days worth of data from the Arduino,")
    public void thereIsDaysWorthOfDataFromTheArduino() {
        gardenPlantSuggestions = new GardenPlantSuggestions(arduinoDataPointService);
        user = new User("test@user.com", "Suggest", "Plants", "Password1!", "");
        user.setConfirmation(true);
        userService.addUsers(user);
        garden = new Garden(user, "Garden", "", new Location("New Zealand", "Christchurch"), 1f, true);
        gardenService.saveGarden(garden);
        for (double i=10, j=0; j<=14; i+=4, j++) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(garden, LocalDateTime.now().minusDays((long) j),
                    i-20,i-10,i/100,i-5,i-7));
        }
        auth = RunCucumberTest.authMaker.accept(user.getEmail(), "Password1!", userService);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Assertions.assertEquals(expectedPrompt, gardenPlantSuggestions.getArduinoPrompt(garden.getId()));
    }

    @When("the user clicks suggest plants on the view garden page,")
    public void theUserClicksSuggestPlantsOnTheViewGardenPage() throws Exception {
        try (MockedStatic<GardenPlantSuggestions> mockedSuggestions = mockStatic(GardenPlantSuggestions.class)) {
            mockedSuggestions.when(() -> GardenPlantSuggestions.getSuggestions(expectedPrompt)).thenReturn("3 plants");
            Assertions.assertEquals("3 plants", getSuggestions(expectedPrompt));
            Assertions.assertNotEquals("3 plants", getSuggestions("H"));
            result = mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())));
        }
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("data is used to suggest 3 plants that would work well in the garden.")
    public void dataIsUsedToSuggestPlantsThatWouldWorkWellInTheGarden() throws Exception {
        result.andExpect(model().attribute("plantSuggestions", "3 plants"));
    }
}
