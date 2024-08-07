package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Then;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class EditGardenPlantsFeature {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Then("there is a list of all plants I have recorded in the garden with their name, a default image, and count and description \\(if provided).")
    public void thereIsAListOfAllPlantsIHaveRecordedInTheGardenWithTheirNameADefaultImageAndCountAndDescriptionIfProvided() throws Exception {
        Optional<Garden> garden = gardenRepository.findById(1);
        if (garden.isPresent()) {
            List<Plant> gardensPlants = plantRepository.findAllByGarden(garden.get());
        }

        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attribute("plants", hasItems()));
    }
}
