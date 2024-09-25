package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

public class PlantSuggestions {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;
    @Given("there is 14 days worth of data from the Arduino,")
    public void thereIsDaysWorthOfDataFromTheArduino() {

    }

    @When("the user clicks suggest plants on the view garden page,")
    public void theUserClicksSuggestPlantsOnTheViewGardenPage() {
    }

    @Then("data is used to suggest 3 plants that would work well in the garden.")
    public void dataIsUsedToSuggestPlantsThatWouldWorkWellInTheGarden() {
    }
}
