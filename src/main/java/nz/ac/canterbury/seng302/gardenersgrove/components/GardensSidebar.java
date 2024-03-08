package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.springframework.ui.Model;

public class GardensSidebar {
    public void updateGardensSidebar(Model model, GardenService gardenService) {
        model.addAttribute("gardens", gardenService.getAllGardens());
    }
}
