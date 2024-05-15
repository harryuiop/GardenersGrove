package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.LocationRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.VIEW_GARDEN_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.editPlantUri;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class PlantControllerEditTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    private final String originalPlantName = "Test Plant";
    private final int originalPlantCount = 1;

    private final String originalPlantDescription = "description";
    private final LocalDate originalPlantedDate = LocalDate.now();

    private User user;

    @BeforeEach
    void setUp() {
        if (user == null) {
            user = new User(
                            "test@domain.net",
                            "Test",
                            "User",
                            "Password1!",
                            "2000-01-01"
            );
            userRepository.save(user);
        }
        gardenRepository.deleteAll();
        locationRepository.deleteAll();

        Location gardenLocation = new Location("New Zealand", "Christchurch");
        gardenLocation.setStreetAddress("90 Ilam Road");
        gardenLocation.setSuburb("Ilam");
        gardenLocation.setPostcode("8041");

        Garden garden = gardenRepository.save(new Garden(user, "Test Garden", null, gardenLocation, null));
        plantRepository.deleteAll();
        plantRepository.save(new Plant(
                        originalPlantName, originalPlantCount, originalPlantDescription, originalPlantedDate, null, garden
        ));
    }

    @Test
    void submitForm_noChange_plantUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] emptyImageBytes = new byte[0];

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString())
                                        .param("gardenId", Long.toString(plant.getGarden().getId()))
                                        .param("plantId", Long.toString(plant.getId())))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));


        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_noCount_plantUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] emptyImageBytes = new byte[0];

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));


        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertNull(updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_noDescription_plantUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] emptyImageBytes = new byte[0];

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));


        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertNull(updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_noDate_plantUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] emptyImageBytes = new byte[0];

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plant.getDescription()))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));


        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertNull(updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_invalidName_plantNotUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        String invalidPlantName = "Test&Plant";
        byte[] emptyImageBytes = new byte[0];

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", invalidPlantName)
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));


        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_invalidCount_plantNotUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        int plantCount = -1;
        byte[] emptyImageBytes = new byte[0];


        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_invalidDescription_plantNotUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        String plantDescription = new String(new char[513]);
        byte[] emptyImageBytes = new byte[0];

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_imageTooLarge_plantNotUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] fakeImageBytes = new byte[11_000_000];
        new Random().nextBytes(fakeImageBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_imageWrongType_plantNotUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] fakeImageBytes = new byte[11_000_000];
        new Random().nextBytes(fakeImageBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.gif", MediaType.IMAGE_GIF_VALUE, fakeImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNull(updatedPlant.getImageFileName());
    }

    @Test
    void submitForm_newImage_plantUpdated() throws Exception {
        Plant plant = plantRepository.findAll().get(0);
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart(editPlantUri(plant.getGarden().getId(), plant.getId()))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plant.getName())
                                        .param("plantCount", String.valueOf(plant.getCount()))
                                        .param("plantDescription", plant.getDescription())
                                        .param("plantedDate", plant.getPlantedOn().toString()))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant updatedPlant = plantRepository.findAll().get(0);

        assertEquals(originalPlantName, updatedPlant.getName());
        assertEquals(originalPlantCount, updatedPlant.getCount());
        assertEquals(originalPlantDescription, updatedPlant.getDescription());
        assertEquals(originalPlantedDate, updatedPlant.getPlantedOn());
        assertNotNull(updatedPlant.getImageFileName());
    }


}