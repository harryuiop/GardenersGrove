package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArduinoDataPointRepository extends CrudRepository<ArduinoDataPoint, Long> {

    List<ArduinoDataPoint> findAllByGarden(Garden garden);

    @Query("SELECT a FROM ArduinoDataPoint a WHERE a.garden.id=?1")
    List<ArduinoDataPoint> getGardenArduinoStats(Long id);

    @Query("SELECT a FROM ArduinoDataPoint a WHERE a.garden.id=?1")
    List<ArduinoDataPoint> findAllByGardenId(Long id);

    @Query("SELECT a FROM ArduinoDataPoint a WHERE a.garden.id=?1 and a.time >= ?2 and a.time <= ?3")
    List<ArduinoDataPoint> getArduinoDataPointOverDays(Long gardenId, LocalDateTime startDateRange, LocalDateTime endDataRange);

    ArduinoDataPoint findFirstByGardenOrderByTimeDesc(Garden garden);
}
