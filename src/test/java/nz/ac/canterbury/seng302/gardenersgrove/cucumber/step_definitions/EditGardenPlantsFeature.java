package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import ch.qos.logback.core.model.Model;
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
import org.assertj.core.api.CollectionAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attributeExists("plants"))
                .andReturn();

        ModelMap model = Objects.requireNonNull(mvcResult.getModelAndView()).getModelMap();
        List<Plant> plants = (List<Plant>) model.get("plants");

        List<Long> gardensPlantsIds = gardensPlants.stream().map(Plant::getId).toList();
        List<Long> plantsIds = plants.stream().map(Plant::getId).toList();

        Assertions.assertTrue(gardensPlantsIds.containsAll(plantsIds) && plantsIds.containsAll(gardensPlantsIds));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
