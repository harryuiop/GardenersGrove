package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
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