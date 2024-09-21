package nz.ac.canterbury.seng302.gardenersgrove.controller.validation;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.COMPARE_GARDEN_URI_STRING;

@Controller
public class CompareGardensController extends NavBar {

    private final GardenService gardenService;
    private final UserService userService;

    @Autowired
    public CompareGardensController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    @GetMapping(COMPARE_GARDEN_URI_STRING)
    public String displayCompareGardensPage(
            @PathVariable long yourGardenId,
            @PathVariable long theirGardenId,
            Model model
    ) throws NoSuchGardenException, InterruptedException {

        this.updateGardensNavBar(model, gardenService, userService);
        User currentUser = userService.getAuthenticatedUser();

        Optional<Garden> optionalYourGarden = gardenService.getGardenById(yourGardenId);
        Optional<Garden> optionalTheirGarden = gardenService.getGardenById(theirGardenId);

        if (optionalYourGarden.isEmpty() || optionalTheirGarden.isEmpty()) {
            throw new NoSuchGardenException(yourGardenId);
        }

        Garden yourGarden = optionalYourGarden.get();
        Garden theirGarden = optionalTheirGarden.get();

        boolean notOwnerYourGarden = yourGarden.getOwner().getId() != currentUser.getId();
        boolean yourGardenPrivate = !yourGarden.isGardenPublic();
        boolean theirGardenPrivate = !theirGarden.isGardenPublic();

        if (notOwnerYourGarden && (yourGardenPrivate || theirGardenPrivate) ) {
            throw new NoSuchGardenException(yourGardenId);
        }

        model.addAttribute("yourGarden", yourGarden);
        model.addAttribute("theirGarden", theirGarden);
        model.addAttribute("owner", yourGarden.getOwner() == currentUser);

        return "compareGarden";
    }
}
