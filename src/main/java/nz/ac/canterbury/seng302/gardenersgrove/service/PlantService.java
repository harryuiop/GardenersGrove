package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.annotation.PostConstruct;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    private final GardenService gardenService;

    private final UserService userService;

    @Autowired
    public PlantService(PlantRepository plantRepository, UserService userService, GardenService gardenService) {
        this.plantRepository = plantRepository;
        this.gardenService = gardenService;
        this.userService = userService;
    }

    @PostConstruct
    public void addDefaults() {
        if (gardenService.getGardenById(1L).isPresent()) {
            Garden garden1 = gardenService.getGardenById(1L).get();
            this.savePlant(new Plant("Plant 1", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 2", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 3", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 4", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 5", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 6", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 7", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 8", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 9", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
            this.savePlant(new Plant("Plant 10", 1, "", LocalDate.parse("2000-01-01"), null, garden1));
        }
    }

    public List<Plant> getAllPlantsInGarden(Garden garden) {
        return plantRepository.findAllByGarden(garden);
    }

    public Optional<Plant> getPlantById(Long id) {
        return plantRepository.findById(id);
    }

    public Optional<Plant> getPlantByGardenIdAndPlantId(long gardenId, long plantId) {
        return plantRepository.findByGardenIdAndId(gardenId, plantId);
    }

    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }

    public void deletePlant(Long id) {
        plantRepository.deleteById(id);
    }
}