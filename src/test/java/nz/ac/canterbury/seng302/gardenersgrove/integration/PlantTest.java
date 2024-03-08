package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

/**
 * To be discussed withn team....
 */
@DataJpaTest
@Import(PlantService.class)
public class PlantTest {

    private static PlantRepository plantRepositoryMock;
    private static PlantService plantService;

    @BeforeAll
    public static void setup() {
        plantRepositoryMock = Mockito.mock(PlantRepository.class);
        plantService = new PlantService(plantRepositoryMock);
    }

    @Test
    void AddPlantImage_GivenImageValid_PlantSaved () {
        byte[] imageBytes = fakeByteArray(1);
        Plant plant = new Plant("TestPlant", 1, "Test Description", new Date(), imageBytes);
        Mockito.when(plantRepositoryMock.save(Mockito.any())).thenReturn(plant);

        plantService.savePlant(plant);

        // Verify plant has been saved
        Mockito.verify(plantRepositoryMock).save(plant);
        Assertions.assertNotNull(plant.getImage());
    }

    @Test
    void AddPlantImage_GivenImageInvalid_PlantNotSaved () {
        byte[] imageBytes = fakeByteArray(11);
        Plant plant = new Plant("TestPlant", 1, "Test Description", new Date(), imageBytes);

        plantService.savePlant(plant);

        // Verify plant has not been saved
        Mockito.verify(plantRepositoryMock, Mockito.never()).save(Mockito.any());
    }

    private byte[] fakeByteArray(int sizeInMB) {
        int sizeInBytes = sizeInMB * 1024 * 1024; // 1 MB = 1024 * 1024 bytes
        byte[] fakeByteArray = new byte[sizeInBytes];
        Arrays.fill(fakeByteArray, (byte) 0); // Fill the array with zeros
        return fakeByteArray;
    }

}
