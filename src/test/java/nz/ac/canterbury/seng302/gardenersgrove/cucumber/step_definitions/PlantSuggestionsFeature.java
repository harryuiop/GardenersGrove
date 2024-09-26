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
import java.util.ArrayList;
import java.util.List;

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
    String suggestion = """
            You've got a nice range of conditions for your garden! Let me suggest some plants that thrive in this climate:

            **Option 1: Lush & Green:**

            * **Hosta (Various Varieties):** These popular perennials love humidity and partial shade, thriving even with
            fluctuating air pressure.  They'll provide beautiful foliage and come in many colors.
            * **Hydrangea Macrophylla (Bigleaf Hydrangea):** Thriving in humid conditions and partial shade, these beauties
            offer large, showy blooms that add color to your garden.
            * **Japanese Maple (Acer palmatum):** These prized trees are adaptable and prefer well-drained soil with moderate
            moisture levels. They will also benefit from the humidity in your garden.

            **Option 2:  Sunny & Sturdy:**

            * **Clematis (various species):** These climbing vines love sunny spots and can tolerate some shade, but need
            ample moisture to thrive. The diverse varieties offer beautiful flowers that suit a variety of styles.
            * **Lavender (Lavandula angustifolia):**  Known for its fragrant purple blooms, Lavender thrives in full sun with
            well-drained soil and moderate humidity. It's relatively low maintenance and drought-tolerant.
            * **Succulents (various species):**  Many succulent varieties do well under humid conditions. These plants come in
            many forms, shapes, and sizes to suit your preferences.

            **Option 3:   Unique & Adaptable:**

            * **Ajuga reptans (Bugleweed):** This groundcover thrives in the humidity you described with a spread of green
            foliage and beautiful purple flowers, perfect for adding color and texture.
            * **Ferns (various species):**  Many ferns prefer humid environments.  They add a unique textural element to your
            garden and require less frequent watering than many other plants.


             **Important Notes:**

            * **Specific Varieties:** Check local nurseries or online resources for varieties that thrive in the exact
            conditions you describe.
            * **Soil & Drainage:**  Ensure proper soil drainage is available by amending with compost if needed. Avoid heavy
            clay soils which can hold too much moisture and lead to root rot.
            """;

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
            mockedSuggestions.when(() -> GardenPlantSuggestions.getSuggestions(expectedPrompt)).thenReturn(suggestion);
            Assertions.assertEquals(suggestion, getSuggestions(expectedPrompt));
            Assertions.assertNotEquals(suggestion, getSuggestions("H"));
            result = mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(garden.getId())));
        }
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("data is used to suggest 3 plants that would work well in the garden.")
    public void dataIsUsedToSuggestPlantsThatWouldWorkWellInTheGarden() throws Exception {
        List<String> expectResponse = new ArrayList<>();
        expectResponse.add("<b>Hosta (Various Varieties):</b>\nThese popular perennials love humidity and partial shade, thriving even with" +
                " fluctuating air pressure.  They'll provide beautiful foliage and come in many colors. ");
        expectResponse.add("<b>Clematis (various species):</b>\nThese climbing vines love sunny spots and can tolerate some " +
                "shade, but need ample moisture to thrive. The diverse varieties offer beautiful flowers that suit a variety of styles. ");
        expectResponse.add("<b>Ajuga reptans (Bugleweed):</b>\nThis groundcover thrives in the humidity you described with a " +
                "spread of green foliage and beautiful purple flowers, perfect for adding color and texture. ");
        result.andExpect(model().attribute("plantSuggestions", expectResponse));
    }
}
