package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Temperature;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class TemperatureService {
    private final TemperatureRepository temperatureRepository;

    @Autowired
    public TemperatureService(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    public void addTemperature(Temperature temperature) {
        temperatureRepository.save(temperature);
    }

    public List<Temperature> getAllTemperatures() {
        return temperatureRepository.findALl();
    }

    public List<Temperature> getTemperaturesByDate(Date date) {
        return temperatureRepository.findByDate(date);
    }

    public List<Temperature> getTemperaturesByHour(Time hour) {
        return temperatureRepository.findByTime(hour);
    }
}
