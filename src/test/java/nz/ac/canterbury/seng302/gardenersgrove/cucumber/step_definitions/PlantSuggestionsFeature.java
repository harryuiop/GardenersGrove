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
import java.util.Arrays;
import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.utility.GardenPlantSuggestions.getSuggestions;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    String expectedPrompt = "Give a response of 3 plants in the form 1. Plant Name : plant description, with no extra text before or after, " +
            "suggestion plants that are suitable for these given environment factors; , Temperature between -10.0C-42.0C, " +
            "Moisture between 3.0%-55.0%, Light between 5.0%-57.0%, Air Pressure between 0.1atm-0.62atm, Humidity between 0.0%-52.0%";
    String suggestion = """
            1. **Succulents:** These plants thrive in dry climates with minimal water and well-draining soil.  Examples include:  * Aloe vera*, * Echeveria*, * Sedum*.
            
            2. **Cacti:** Known for their ability to survive extreme temperatures, cacti are also drought-tolerant and prefer bright, sunny locations. Consider * Echinocactus*, * Opuntia*, * Agave*.
            
            3. **Bonsai Trees:** While not strictly a single type, bonsai trees represent miniature versions of species adapted for specific environments.  These can be grown with varying levels of humidity and light depending on the chosen species. Examples include: * Japanese Maple*, * Olive Tree*, * Pine Tree*.
            
            """;

    Garden garden;
    User user;
    Authentication auth;
    ResultActions result;

    @Given("there is 14 days worth of data from the Arduino,")
    public void thereIsDaysWorthOfDataFromTheArduino() {
        expectedPrompt = "Give a response of 3 plants in the form 1. Plant Name : plant description, with no extra text before or after, " +
                "suggestion plants that are suitable for these given environment factors; , Temperature between -10.0C-42.0C, " +
                "Moisture between 3.0%-55.0%, Light between 5.0%-57.0%, Air Pressure between 0.1atm-0.62atm, Humidity between 0.0%-52.0%";
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

    @When("the user clicks 'Plant Suggestions' on the view garden page,")
    public void theUserClicksSuggestPlantsOnTheViewGardenPage() throws Exception {
        try (MockedStatic<GardenPlantSuggestions> mockedSuggestions = mockStatic(GardenPlantSuggestions.class)) {
            mockedSuggestions.when(() -> GardenPlantSuggestions.getSuggestions(expectedPrompt)).thenReturn(suggestion);
            Assertions.assertEquals(suggestion, getSuggestions(expectedPrompt));
            Assertions.assertNotEquals(suggestion, getSuggestions("H"));
            result = mockMvc.perform(MockMvcRequestBuilders.get("/ai/suggestions?gardenId="+garden.getId()));
        }
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Then("data is used to suggest 3 plants that would work well in the garden.")
    public void dataIsUsedToSuggestPlantsThatWouldWorkWellInTheGarden() throws Exception {
        System.out.println(gardenPlantSuggestions.parseSuggestions(suggestion));
        List<String> correctSuggestions = new ArrayList<>();
        correctSuggestions.add("<b></b>");
        correctSuggestions.add("""
                <b>Succulents:</b> These plants thrive in dry climates with minimal water and well-draining soil.  Examples include:   Aloe vera,  Echeveria,  Sedum.

                """);
        correctSuggestions.add("""
                <b>Cacti:</b> Known for their ability to survive extreme temperatures, cacti are also drought-tolerant and prefer bright, sunny locations. Consider  Echinocactus,  Opuntia,  Agave.

                """);
        correctSuggestions.add("""
                <b>Bonsai Trees:</b> While not strictly a single type, bonsai trees represent miniature versions of species adapted for specific environments.  These can be grown with varying levels of humidity and light depending on the chosen species. Examples include:  Japanese Maple,  Olive Tree,  Pine Tree.

                """);
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0]").value(correctSuggestions.get(0)));
        result.andExpect(jsonPath("$[1]").value(correctSuggestions.get(1)));
        result.andExpect(jsonPath("$[2]").value(correctSuggestions.get(2)));
        result.andExpect(jsonPath("$[3]").value(correctSuggestions.get(3)));

    }

    @Given("there is no Arduino connected \\(or less than {int} days of data) but there is a valid location for the garden,")
    public void thereIsNoArduinoConnectedOrLessThanDaysOfDataButThereIsAValidLocationForTheGarden(int arg0) {
        gardenPlantSuggestions = new GardenPlantSuggestions(arduinoDataPointService);
        user = new User("test2@user.com", "Suggest", "Plants", "Password1!", "");
        user.setConfirmation(true);
        userService.addUsers(user);
        Location gardenLocation = new Location("New Zealand", "Christchurch");
        gardenLocation.setLngLat(Arrays.asList(43.5320, 172.6366)); // Setting the location to mock location API response
        garden = new Garden(user, "Garden", "", gardenLocation, 1f, true);
        gardenService.saveGarden(garden);
        expectedPrompt = String.format(
                "give me 3 plant suggestions for a garden in %s" +
                        "[insert plant name]: [insert plant description that has 2-3 sentences]" +
                        "note: please do not include the texts 'Plant Name' or 'Plant Description'",
                garden.getLocation());
        auth = RunCucumberTest.authMaker.accept(user.getEmail(), "Password1!", userService);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Assertions.assertFalse(arduinoDataPointService.checkFourteenDaysOfData(garden.getId()));
    }
}
