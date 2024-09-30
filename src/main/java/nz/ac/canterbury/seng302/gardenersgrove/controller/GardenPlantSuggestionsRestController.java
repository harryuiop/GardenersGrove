package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.GardenPlantSuggestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
public class GardenPlantSuggestionsRestController {

    private final GardenPlantSuggestions gardenPlantSuggestions;
    private final GardenService gardenService;

    @Autowired
    public GardenPlantSuggestionsRestController(ArduinoDataPointService arduinoDataPointService, GardenService gardenService) {
        this.gardenService = gardenService;
        gardenPlantSuggestions = new GardenPlantSuggestions(arduinoDataPointService);
    }

    @GetMapping("/ai/suggestions")
    public List<String> getPlantSuggestions(@RequestParam long gardenId) throws InterruptedException {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            return List.of("No garden Found");
        }
        return gardenPlantSuggestions.getPlantSuggestionsForGarden(optionalGarden.get(), true);
    }

}
