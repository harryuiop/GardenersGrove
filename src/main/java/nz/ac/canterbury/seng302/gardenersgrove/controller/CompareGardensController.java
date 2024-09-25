package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoControllerDataService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;


/**
 * Controller for the compare garden page. For viewing statistics of your garden and a chosen garden.
 */
@Controller
public class CompareGardensController extends NavBar {

    private final GardenService gardenService;
    private final UserService userService;
    private final ArduinoControllerDataService arduinoControllerDataService;

    @Autowired
    public CompareGardensController(GardenService gardenService, UserService userService, ArduinoControllerDataService arduinoControllerDataService) {
        this.gardenService = gardenService;
        this.userService = userService;
        this.arduinoControllerDataService = arduinoControllerDataService;
    }

    /**
     * Set up compare garden page and display attributes
     *
     * @param yourGardenId The id of your garden to be compared
     * @param theirGardenId The id of the chosen garden to be compared with
     * @param model Puts the data into the template to be viewed
     * @return Thymeleaf HTML template of the compare garden page.
     * @throws NoSuchGardenException Thrown if the garden you are fetching is private and not your own
     */
    @GetMapping(COMPARE_GARDEN_URI_STRING)
    public String displayCompareGardensPage(
        @PathVariable long yourGardenId,
        @PathVariable long theirGardenId,
        Model model
    ) throws NoSuchGardenException {

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
        model.addAttribute("gardenMonitoringUri", monitorGardenUri(yourGarden.getId()));
        model.addAttribute("gardenList", gardenService.getAllGardens());

        arduinoControllerDataService.addGraphDataToModel(model, yourGardenId, theirGardenId);

        return "compareGarden";
    }
}
