package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchPlantException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.PlantService;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ImageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the view garden page. For viewing a specific garden.
 */
@Controller
public class ViewGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);

    private final GardenService gardenService;
    private final PlantService plantService;
    private final UserService userService;
    private final TagService tagService;

    /**
     * Spring will automatically call this constructor at runtime to inject the dependencies.
     *
     * @param gardenService A Garden database access object.
     * @param plantService  A Plant database access object.
     * @param userService   A User database access object.
     */
    @Autowired
    public ViewGardenController(GardenService gardenService, PlantService plantService, UserService userService, TagService tagService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.userService = userService;
        this.tagService = tagService;
    }

    private String loadGardenPage(
                    Garden garden,
                    URI editGardenUri,
                    URI newPlantUri,
                    List<Plant> plants,
                    Model model
    ) {
        this.updateGardensSidebar(model, gardenService, userService);

        model.addAttribute("garden", garden);
        model.addAttribute("editGardenUri", editGardenUri.toString());
        model.addAttribute("newPlantUri", newPlantUri.toString());
        model.addAttribute("plants", plants);
        model.addAttribute("editPlantUriString", EDIT_PLANT_URI_STRING);
        model.addAttribute("uploadPlantImageUriString", UPLOAD_PLANT_IMAGE_URI_STRING);
        model.addAttribute("tags", garden.getTags());
        model.addAttribute("tagFormSubmissionUri", newGardenTagUri(garden.getId()));
        return "viewGarden";
    }

    /**
     * Set up view garden page and display attributes.
     *
     * @return Thyme leaf html template of the view garden page.
     */
    @GetMapping(VIEW_GARDEN_URI_STRING)
    public String displayGarden(
                    @PathVariable long gardenId,
                    Model model
    ) throws NoSuchGardenException {
        logger.info("GET {}", viewGardenUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty() || optionalGarden.get().getOwner() != userService.getAuthenticatedUser()) {
            throw new NoSuchGardenException(gardenId);
        }
        return loadGardenPage(
                        optionalGarden.get(),
                        editGardenUri(gardenId),
                        newPlantUri(gardenId),
                        plantService.getAllPlantsInGarden(optionalGarden.get()),
                        model
        );
    }

    /**
     * Handles requests to change a plant's image from the view garden page.
     * Changes the plant image and has user feedback for size and type if the new image is invalid.
     *
     * @param imageFile          New Image file.
     * @param plantId            the ID of the plant to attach the image to.
     * @param gardenId           The ID of the garden the plant sits within.
     * @param redirectAttributes object that passes data through to page we redirect to.
     * @return Thymeleaf HTML template for the view garden page.
     */
    @PostMapping(UPLOAD_PLANT_IMAGE_URI_STRING)
    public String submitPlantImage(
                    @PathVariable long gardenId,
                    @PathVariable long plantId,
                    @RequestParam(name = "plantImage", required = false) MultipartFile imageFile,
                    RedirectAttributes redirectAttributes
    ) throws NoSuchPlantException {
        logger.info("POST {}", uploadPlantImageUri(gardenId, plantId));

        Optional<Plant> optionalPlant = plantService.getPlantByGardenIdAndPlantId(gardenId, plantId);
        if (optionalPlant.isEmpty()) {
            throw new NoSuchPlantException(
                            "Unable to find plant with id " + plantId + " in garden with id " + gardenId + "."
            );
        }
        Plant plant = optionalPlant.get();

        ImageValidator imageValidator = new ImageValidator(imageFile);

        if (imageValidator.isValid()) {
            try {
                String fileName = ImageStore.storeImage(imageFile);
                plant.setImageFileName(fileName);
                plantService.savePlant(plant);
            } catch (IOException error) {
                logger.error("Error saving plant image", error);
                redirectAttributes.addFlashAttribute("plantImageUploadError", "Image upload failed. Please try again.");
            }
        } else {
            redirectAttributes.addFlashAttribute("selectedPlantId", plantId);
            for (Map.Entry<String, String> entry : imageValidator.getErrorMessages().entrySet()) {
                redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue());
            }
        }

        return "redirect:" + viewGardenUri(gardenId);
    }

    @PostMapping(NEW_GARDEN_TAG_URI_STRING)
    public String submitGardenTag(Model model,
                                  @PathVariable long gardenId,
                                  @RequestParam(name = "tagName", required = false) String tagName) throws NoSuchGardenException {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty() || optionalGarden.get().getOwner() != userService.getAuthenticatedUser()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();
        tagService.saveTag(new Tag(tagName, garden));
        return "redirect:" + viewGardenUri(gardenId);
    }


}
