package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureMockMvc
class GardenFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private GardenRepository gardenRepository;

    @Test
    void testSubmitForm() throws Exception {
        String gardenName = "Test Garden";
        String gardenLocation = "Test Location";
        Float gardenSize = 100.0f;

        mockMvc.perform(MockMvcRequestBuilders.post("/form")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation)
                        .param("gardenSize", gardenSize.toString()))
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
    void submitForm_invalidInput_gardenNotSaved() throws Exception {
        String gardenName = "Test&Garden";
        String gardenLocation = "Test^Location";
        Float gardenSize = -1f;

        mockMvc.perform(MockMvcRequestBuilders.post("/form")
                        .param("gardenName", gardenName)
                        .param("gardenLocation", gardenLocation)
                        .param("gardenSize", gardenSize.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("gardenForm"));

        List<Garden> allGardens = gardenRepository.findAll();
        assertTrue(allGardens.isEmpty());
    }
}