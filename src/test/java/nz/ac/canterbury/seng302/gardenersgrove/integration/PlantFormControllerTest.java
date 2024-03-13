package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
class PlantFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenService gardenService;

    @BeforeEach
    void setUpDefaultRepository() {
        plantRepository.deleteAll();
    }

    @Test
    void submitForm_noImage_gardenSaved() throws Exception {
        String plantName = "Test Plant";
        int plantCount = 1;
        String plantDescription = "Test Description";
        String plantedDate = "01/01/2020";
        Garden garden = new Garden("Test Garden", "Test Location", 1f);
        gardenService.saveGarden(garden);

        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", Integer.toString(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", garden.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
//         assertEquals(new Date(), plant.getPlantedOn());
        assertNull(plant.getImageFileName());
    }
}
