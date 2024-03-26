package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;


public class GardensSidebar {
    public void updateGardensSidebar(Model model, GardenService gardenService, UserService userService) {
        model.addAttribute("gardens", gardenService.getAllGardens());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = Integer.parseInt(auth.getName());
        Users user = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", user);
    }
}
