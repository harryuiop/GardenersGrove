package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArduinoDataPointRepository extends CrudRepository<ArduinoDataPoint, Long> {

    List<ArduinoDataPoint> findAllByGarden(Garden garden);

    // If this is not working on VM it is likely due to the PARSEDATETIME function being database type specific
    @Query("SELECT a FROM ArduinoDataPoint a WHERE a.garden.id=?1 ORDER BY PARSEDATETIME(a.time, 'yyyy-MM-dd HH:mm:ss') DESC")
    List<ArduinoDataPoint> getGardenArduinoStats(Long id);

    @Query("SELECT a FROM ArduinoDataPoint a WHERE a.garden.id=?1")
    List<ArduinoDataPoint> findAllByGardenId(Long id);

    ArduinoDataPoint findFirstByGardenOrderByTime(Garden garden);
}
