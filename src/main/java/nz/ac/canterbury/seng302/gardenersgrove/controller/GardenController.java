package nz.ac.canterbury.seng302.gardenersgrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@Controller
public class GardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final GardenService gardenService;
    private final UserService userService;

    /**
     * The PlantFormController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param gardenService The Garden database access object.
     * @param userService   The User database access object.
     */
    @Autowired
    public GardenController(GardenService gardenService, UserService userService) {
        this.gardenService = gardenService;
        this.userService = userService;
    }

    /**
     * Loads the garden form with the given errors and pre-filled values.
     *
     * @param gardenNameError     The error message for the garden name form field.
     * @param gardenLocationError The error message for the garden location form field.
     * @param gardenSizeError     The error message for the garden size form field.
     * @param gardenId            The ID of the garden to submit the form for.
     * @param gardenName          The name of the garden to pre-fill the form with.
     * @param gardenLocation      The location of the garden to pre-fill the form with.
     * @param gardenSize          The size of the garden to pre-fill the form with.
     * @param previousPage        The previous page to redirect to.
     * @param model               object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    private String loadGardenForm(
                    String gardenNameError,
                    String gardenLocationError,
                    String gardenSizeError,
                    String gardenId,
                    String gardenName,
                    String gardenLocation,
                    Float gardenSize,
                    URI formSubmissionUri,
                    String previousPage,
                    Model model
    ) {
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAttribute("gardenNameError", gardenNameError);
        model.addAttribute("gardenLocationError", gardenLocationError);
        model.addAttribute("gardenSizeError", gardenSizeError);

        model.addAttribute("gardenId", gardenId);
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenLocation", gardenLocation);
        model.addAttribute("gardenSize", gardenSize);

        model.addAttribute("formSubmissionUri", formSubmissionUri);
        model.addAttribute("previousPage", previousPage);
        return "gardenForm";
    }

    /**
     * Serves the garden form to the user, passing user information to the HTML.
     *
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping(NEW_GARDEN_URI_STRING)
    public String createGarden(HttpServletRequest request, Model model) {
        logger.info("GET {}", newGardenUri());
        return loadGardenForm(
                        "", "", "",
                        "new", null, null, null,
                        newGardenUri(), request.getHeader("Referer"),
                        model
        );
    }

    /**
     * Submits form and saves the new garden to the database.
     *
     * @param gardenName     The name of the garden as input by the user.
     * @param gardenLocation The location of the garden as input by the user.
     * @param gardenSize     The size of the garden as input by the user.
     * @param model          object that passes data through to the HTML.
     * @return thymeleaf HTML template for the new garden.
     */
    @PostMapping(NEW_GARDEN_URI_STRING)
    public String submitNewGarden(
                    @RequestParam(name = "gardenName") String gardenName,
                    @RequestParam(name = "gardenLocation") String gardenLocation,
                    @RequestParam(name = "gardenSize", required = false) Float gardenSize,
                    HttpServletRequest request,
                    Model model
    ) {
        logger.info("POST {}", newGardenUri());

        Map<String, String> errors = ErrorChecker.gardenFormErrors(gardenName, gardenLocation, gardenSize);
        if (!errors.isEmpty()) {
            return loadGardenForm(
                            errors.getOrDefault("gardenNameError", ""),
                            errors.getOrDefault("gardenLocationError", ""),
                            errors.getOrDefault("gardenSizeError", ""),
                            "new", gardenName, gardenLocation, gardenSize,
                            newGardenUri(), request.getHeader("Referer"),
                            model
            );
        }

        Garden garden = new Garden(gardenName, gardenLocation, gardenSize);
        gardenService.saveGarden(garden);
        return "redirect:" + viewGardenUri(garden.getId());
    }

    /**
     * Serves the plant form to the user, passing user/garden information to the HTML,
     * with existing plant information pre-filled.
     *
     * @param gardenId The id of the plant to edit.
     * @param model    object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping(EDIT_GARDEN_URI_STRING)
    public String editGarden(
                    @PathVariable Long gardenId,
                    HttpServletRequest request,
                    Model model
    ) throws NoSuchGardenException {
        logger.info("GET {}", editGardenUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        return loadGardenForm(
                        "", "", "",
                        gardenId.toString(), garden.getName(), garden.getLocation(), garden.getSize(),
                        editGardenUri(gardenId), request.getHeader("Referer"),
                        model
        );
    }

    /**
     * Submits form and saves the updated garden details to the database.
     *
     * @param gardenId       The id of the garden to edit.
     * @param gardenName     The name of the garden as input by the user.
     * @param gardenLocation The location of the garden as input by the user.
     * @param gardenSize     The size of the garden as input by the user.
     * @param model          object that passes data through to the HTML.
     * @return thymeleaf HTML template for the new garden.
     */
    @PostMapping(EDIT_GARDEN_URI_STRING)
    public String submitGardenEdit(
                    @PathVariable long gardenId,
                    @RequestParam(name = "gardenName") String gardenName,
                    @RequestParam(name = "gardenLocation") String gardenLocation,
                    @RequestParam(name = "gardenSize", required = false) Float gardenSize,
                    HttpServletRequest request,
                    Model model
    ) throws NoSuchGardenException {
        logger.info("POST {}", editGardenUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        Map<String, String> errors = ErrorChecker.gardenFormErrors(gardenName, gardenLocation, gardenSize);
        if (!errors.isEmpty()) {
            return loadGardenForm(
                            errors.getOrDefault("gardenNameError", ""),
                            errors.getOrDefault("gardenLocationError", ""),
                            errors.getOrDefault("gardenSizeError", ""),
                            String.valueOf(gardenId), gardenName, gardenLocation, gardenSize,
                            editGardenUri(gardenId), request.getHeader("Referer"),
                            model
            );
        }

        garden.setName(gardenName);
        garden.setLocation(gardenLocation);
        garden.setSize(gardenSize);

        gardenService.saveGarden(garden);
        return "redirect:" + viewGardenUri(garden.getId());
    }
}
