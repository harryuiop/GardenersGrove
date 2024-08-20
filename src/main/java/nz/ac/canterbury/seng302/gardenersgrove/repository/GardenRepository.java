package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GardenRepository extends CrudRepository<Garden, Long> {
    Optional<Garden> findById(long id);

    Optional<Garden> findByArduinoId(String id);

    List<Garden> findAllByOwner(User owner);

    @Query(value = "SELECT * FROM garden WHERE garden.is_garden_public = TRUE ORDER BY garden.time_created DESC LIMIT 10 OFFSET ?1",
            nativeQuery = true)
    List<Garden> findByGardenPublicTrue(int paginationOffset);

    @Query(value = "SELECT DISTINCT g.* " +
                   "FROM garden g " +
                   "LEFT JOIN plant p ON g.id = p.garden_id " +
                   "WHERE g.is_garden_public = TRUE " +
                   "AND (LOWER(g.name) LIKE CONCAT('%', LOWER(?2), '%') OR LOWER(p.name) LIKE CONCAT('%', LOWER(?2), '%')) " +
                   "ORDER BY g.time_created DESC " +
                   "LIMIT 10 OFFSET ?1", nativeQuery = true)
    List<Garden> findByGardenPublicTrueWithSearchParameter(int paginationOffset, String searchParameter);

    long countByIsGardenPublicTrue();

    @Query(value = "SELECT COUNT(DISTINCT g.id) " +
            "FROM garden g " +
            "LEFT JOIN plant p ON g.id = p.garden_id " +
            "WHERE g.is_garden_public = TRUE " +
            "AND (LOWER(g.name) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(p.name) LIKE CONCAT('%', LOWER(?1), '%'))", nativeQuery = true)
    long countByIsGardenPublicTrueWithGardenNameSearch(String searchParameter);

}