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

    @Query(value = "SELECT * FROM garden WHERE is_garden_public = TRUE AND LOWER(name) LIKE CONCAT('%', LOWER(?2), '%')" +
            "ORDER BY time_created DESC LIMIT 10 OFFSET ?1", nativeQuery = true)
    List<Garden> findByGardenPublicTrueWithSearchName(int paginationOffset, String searchGardenName);


    long countByIsGardenPublicTrue();

    @Query(value = "SELECT COUNT(*) FROM garden WHERE is_garden_public = TRUE AND LOWER(name) LIKE CONCAT('%', LOWER(?1), '%')", nativeQuery = true)
    long countByIsGardenPublicTrueWithGardenNameSearch(String searchGardenName);
}