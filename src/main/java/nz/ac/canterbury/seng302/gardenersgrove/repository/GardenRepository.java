package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GardenRepository extends CrudRepository<Garden, Long> {
    Optional<Garden> findById(long id);

    List<Garden> findAll();
}