package nz.ac.canterbury.seng302.gardenersgrove.components;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;


public class GardensSidebar {
    /**
     * This method passes information to the sidebar thymeleaf fragment.
     * Currently, it just passes the currently logged-in user, and a list of all the user's gardens.
     * @param model The portal which feeds information into the HTML.
     * @param gardenService Garden database access object.
     * @param userService User database access object.
     */
    public void updateGardensSidebar(Model model, GardenService gardenService, UserService userService) {
        model.addAttribute("gardens", gardenService.getAllGardens());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int currentPrincipalName = Integer.parseInt(auth.getName());
        User user = userService.getUserById(currentPrincipalName);
        model.addAttribute("user", user);
    }
}
