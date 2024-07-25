package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GardenRepository extends CrudRepository<Garden, Long> {
    Optional<Garden> findById(long id);

    List<Garden> findAllByOwner(User owner);

    @Query(value = "SELECT * FROM garden WHERE garden.is_garden_public = TRUE ORDER BY garden.time_created DESC LIMIT 10 OFFSET ?1",
            nativeQuery = true)
    List<Garden> findByGardenPublicTrue(int paginationOffset);

    long countByIsGardenPublicTrue();
}