package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Controller
public class ArduinoDataController {

    @Autowired
    private GardenService gardenService;

    @Autowired
    private ArduinoDataPointService dataPointService;

    /**
     * This is the endpoint that the arduino will send data to about the sensor values to be saved into the database.
     */
    @PostMapping
    public void receiveArduinoData() {
        //Get json and parse data and insert into data entity
        String id = "testid";
        LocalDateTime time = LocalDateTime.now();
        double tempCelsius = 0;
        double humidityPercent = 0;
        double atmosphereAtm = 0;
        double lightPercent = 0;
        double moisturePercent = 0;
        Optional<Garden> optionalGarden = gardenService.getGardenByArduinoId(id);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            dataPointService.saveDataPoint(new ArduinoDataPoint(garden, time, tempCelsius, humidityPercent, atmosphereAtm, lightPercent, moisturePercent));
        }
    }

}
