package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * To be discussed withn team....
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PlantFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    private final int BYTES_IN_KBS = 1024;

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

        mockMvc.perform(MockMvcRequestBuilders.post("/plantform")
                        .param("plantName", plantName)
                        .param("plantCount", Integer.toString(plantCount))
                        .param("plantDescription", plantDescription)
                        .param("plantedDate", plantedDate))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/demo?plantId=*"));


        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        // assertEquals(new Date(), plant.getPlantedOn());
        assertEquals(plant.getImage().length, 0);
    }

    private byte[] fakeByteArray(int sizeInMB) {
        int sizeInBytes = sizeInMB * BYTES_IN_KBS * BYTES_IN_KBS;
        byte[] fakeByteArray = new byte[sizeInBytes];
        Arrays.fill(fakeByteArray, (byte) 0);
        return fakeByteArray;
    }

}
