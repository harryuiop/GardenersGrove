package nz.ac.canterbury.seng302.gardenersgrove.controller;

import ch.qos.logback.core.model.Model;
import nz.ac.canterbury.seng302.gardenersgrove.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class TemperatureDataController {
    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    public TemperatureDataController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @GetMapping("temp-data")
    public List<Map<String, Double>> temperatureData(Model model) {
        return temperatureService.getGraphData(7);
    }
}
