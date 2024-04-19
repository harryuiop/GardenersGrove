package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(GardenService.class)
class GardenServiceTest {

    @Autowired
    private GardenRepository gardenRepository;

    @Test
    void gardenRepositoryGardenCreation() {
        GardenService gardenService = new GardenService(gardenRepository);

        Location gardenLocation = new Location("New Zealand", "Christchurch");
        Garden garden = gardenService.saveGarden(new Garden("Test Garden", gardenLocation, 100f));
        List<Garden> gardens = gardenRepository.findAll();
        Garden outputGarden = gardens.get(0);

        Assertions.assertEquals(1, gardens.size());
        Assertions.assertEquals(garden.getId(), outputGarden.getId());
        Assertions.assertEquals("Test Garden", outputGarden.getName());
        Assertions.assertEquals(gardenLocation.toString(), outputGarden.getLocation().toString());
        Assertions.assertEquals(100, outputGarden.getSize());
    }
}
