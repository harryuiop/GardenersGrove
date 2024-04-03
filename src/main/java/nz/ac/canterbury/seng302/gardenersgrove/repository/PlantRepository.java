package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PlantRepository extends CrudRepository<Plant, Long> {
    @NonNull
    List<Plant> findAll();

    List<Plant> findAllByGarden(Garden garden);
}