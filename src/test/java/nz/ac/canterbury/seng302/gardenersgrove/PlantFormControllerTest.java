package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class PlantFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @BeforeEach
    void setUp() {
        gardenRepository.save(new Garden("Test Garden", "test location", null));
        plantRepository.deleteAll();
    }

    @Test
    void submitForm_allValid_plantSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        long gardenId = gardenRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertEquals(plantedDate, plant.getPlantedOn());
    }

    @Test
    void submitForm_noCount_plantSaved() throws Exception {
        String plantName = "Test Plant";
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        long gardenId = gardenRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertNull(plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertEquals(plantedDate, plant.getPlantedOn());
    }

    @Test
    void submitForm_noDescription_plantSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantedDate = "2024-01-01";
        long gardenId = gardenRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertNull(plant.getDescription());
        assertEquals(plantedDate, plant.getPlantedOn());
    }

    @Test
    void submitForm_noDate_plantSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        long gardenId = gardenRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertNull(plant.getPlantedOn());
    }

    @Test
    void submitForm_invalidName_plantNotSaved() throws Exception {
        String plantName = "Test&Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        long gardenId = gardenRepository.findAll().get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_invalidCount_gardenNotSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = -1;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        long gardenId = gardenRepository.findAll().get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_invalidDescription_gardenNotSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = new String(new char[513]);
        String plantedDate = "2024-01-01";
        long gardenId = gardenRepository.findAll().get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_invalidDate_gardenNotSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        // Month value exceeds 12, so when parsed with yyyy-MM-dd it will fail
        String plantedDate = "2024-20-02";
        long gardenId = gardenRepository.findAll().get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_invalidGardenId_gardenNotSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        long gardenId = -1;


        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", String.valueOf(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate)
                        .param("gardenId", Long.toString(gardenId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }
}