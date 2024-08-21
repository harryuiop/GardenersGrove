package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArduinoDataPointRepository extends CrudRepository<ArduinoDataPoint, Long> {

    List<ArduinoDataPoint> findAllByGarden(Garden garden);
    
    @Query("SELECT moisturePercent FROM ArduinoDataPoint WHERE id=?1 ORDER BY STR_TO_DATE(time, '%d-%m-%Y %H:%i') DESC") 
     List<ArduinoDataPoint> getGardenArduinoStats(Long id);
}
