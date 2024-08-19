package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Temperature;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Repository interface for accessing temperature entities using Spring's CrudRepository
 */
@Repository
public interface TemperatureRepository extends CrudRepository<Temperature, Long> {
//    /**
//     * Retrieves all Temperature entities
//     * @return A list of Temperature entities
//     */
//    List<Temperature> findALl();

    /**
     * Returns all values from a given data
     * @param date  The date of the readings that are needed
     * @return      A list of all the temperature readings from the given date
     */
    List<Temperature> findByDate(Date date);

    /**
     * Returns all values from a given hour
     * @param time  The hour of when the reading was received
     * @return      A list of the readings from that hour
     */
    List<Temperature> findByTime(Time time);

}
