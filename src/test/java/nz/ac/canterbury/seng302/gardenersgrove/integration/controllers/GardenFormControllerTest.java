package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
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
@AutoConfigureMockMvc(addFilters = false)
class GardenFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenRepository gardenRepository;

    @BeforeEach
    void setUp() {
        gardenRepository.deleteAll();
    }

    @Test
    void submitForm_allValid_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";
        float gardenSize = 100.0f;

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation)
                        .param("gardenSize", Float.toString(gardenSize)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(gardenName, garden.getName());
        assertEquals(gardenLocation, garden.getLocation());
        assertEquals(gardenSize, garden.getSize());
    }

    @Test
    void submitForm_invalidName_gardenNotSaved() throws Exception {
        String gardenName = "Test&Garden";
        String gardenLocation = "Test Location";
        float gardenSize = 4f;

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation)
                        .param("gardenSize", Float.toString(gardenSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidLocation_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test^Location";
        float gardenSize = 4f;

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation)
                        .param("gardenSize", Float.toString(gardenSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_invalidSize_gardenNotSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";
        float gardenSize = -1f;

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation)
                        .param("gardenSize", Float.toString(gardenSize)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }

    @Test
    void submitForm_noSize_gardenSaved() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";

        mockMvc.perform(MockMvcRequestBuilders.post("/gardenform")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/view-garden?gardenId=*"));


        List<Garden> allGardens = gardenRepository.findAll();
        assertEquals(1, allGardens.size());
        Garden garden = allGardens.get(0);
        assertEquals(gardenName, garden.getName());
        assertEquals(gardenLocation, garden.getLocation());
        assertNull(garden.getSize());
    }
}