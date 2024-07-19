package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.LocationRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Random;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.VIEW_GARDEN_URI_STRING;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.newPlantUri;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class PlantControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Autowired
    private LocationRepository locationRepository;

    @SpyBean
    private FormValuesValidator mockFormValuesValidator;

    @BeforeEach
    void setUp() throws ProfanityCheckingException, InterruptedException {
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

        gardenRepository.save(new Garden(user, "Test Garden", null, gardenLocation, null, true));
        plantRepository.deleteAll();

        Mockito.when(mockFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);
    }

    @Test
    void submitForm_allValid_plantSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertNotNull(plant.getImageFileName());
    }

    @Test
    void submitForm_noCount_plantSaved() throws Exception {
        String plantName = "Test Plant";
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertNull(plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertNotNull(plant.getImageFileName());
    }

    @Test
    void submitForm_noDescription_plantSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertNull(plant.getDescription());
        assertNotNull(plant.getImageFileName());
    }

    @Test
    void submitForm_noDate_plantSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantDescription", plantDescription))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertNull(plant.getPlantedOn());
        assertNotNull(plant.getImageFileName());
    }

    @Test
    void submitForm_invalidName_plantNotSaved() throws Exception {
        String plantName = "Test&Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "text"})
    void submitForm_invalidCount_plantNotSaved(String plantCount) throws Exception {
        String plantName = "Test Plant";
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", plantCount)
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_invalidDescription_plantNotSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = new String(new char[513]);
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_invalidGardenId_plantNotSaved() throws Exception {
        String plantName = "Test Plant";
        Integer plantCount = 4;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = -1;


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", String.valueOf(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_noImage_plantSaved() throws Exception {
        String plantName = "Test Plant";
        int plantCount = 1;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] emptyImageBytes = new byte[0];
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, emptyImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", Integer.toString(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                        .andExpect(MockMvcResultMatchers.redirectedUrlPattern(VIEW_GARDEN_URI_STRING));

        List<Plant> allPlants = plantRepository.findAll();
        assertEquals(1, allPlants.size());
        Plant plant = allPlants.get(0);
        assertEquals(plantName, plant.getName());
        assertEquals(plantCount, plant.getCount());
        assertEquals(plantDescription, plant.getDescription());
        assertNull(plant.getImageFileName());
    }

    @Test
    void submitForm_imageTooLarge_plantNotSaved() throws Exception {
        String plantName = "Test Plant";
        int plantCount = 1;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[11_000_000];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.jpg", MediaType.IMAGE_JPEG_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", Integer.toString(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

    @Test
    void submitForm_imageWrongType_plantNotSaved() throws Exception {
        String plantName = "Test Plant";
        int plantCount = 1;
        String plantDescription = "Test Description";
        String plantedDate = "2024-01-01";
        byte[] fakeImageBytes = new byte[10];
        new Random().nextBytes(fakeImageBytes);
        long gardenId = gardenRepository.findAllByOwner(user).get(0).getId();


        mockMvc.perform(MockMvcRequestBuilders.multipart(newPlantUri(gardenId))
                                        .file(new MockMultipartFile("plantImage", "mock.gif", MediaType.IMAGE_GIF_VALUE, fakeImageBytes))
                                        .param("plantName", plantName)
                                        .param("plantCount", Integer.toString(plantCount))
                                        .param("plantDescription", plantDescription)
                                        .param("plantedDate", plantedDate))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.view().name("plantForm"));

        List<Plant> allPlants = plantRepository.findAll();
        assertTrue(allPlants.isEmpty());
    }

}