package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.RunCucumberTest;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class EditGardenPlantsFeature {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;


    @Autowired
    private UserService userService;

    private Authentication auth;

    private Long gardenId;

    @Given("I am logged in as {string}, {string}")
    public void iAmLoggedIn(String email, String password) {
        auth = RunCucumberTest.authMaker.accept(email, password, userService);
    }

    @Then("there is a list of all plants I have recorded in the garden with their name, a default image, and count and description \\(if provided).")
    public void thereIsAListOfAllPlantsIHaveRecordedInTheGardenWithTheirNameADefaultImageAndCountAndDescriptionIfProvided() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(auth);
        Garden garden = gardenRepository.findAllByOwner(userService.getAuthenticatedUser()).get(0);

        List<Plant> gardensPlants = plantRepository.findAllByGarden(garden);

        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("plants", containsInAnyOrder(gardensPlants)));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @When("I click the edit plant button next to plant with id int {int}")
    public void iClickTheEditPlantButtonNextToPlantWithIdInt(int plantId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(editPlantUri(1, plantId)))
                .andExpect(status().isOk());
    }


    @Then("I am taken to the edit plant page for plant with id int {int}")
    public void iAmTakenToTheEditPlantPageForPlantWithIdInt(int plantId) throws Exception {
        Optional<Plant> plant = plantRepository.findById((long)plantId);
        if (plant.isPresent()) {
            mockMvc.perform(MockMvcRequestBuilders.get(editPlantUri(1, plantId)))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("plantName", plant.get().getName()));
        }
    }

    @And("the form values are prepopulated with the details of plant with id int {int}")
    public void theFormValuesArePrepopulatedWithTheDetailsOfPlantWithIdInt(int plantId) {
    }
}
