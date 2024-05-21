package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchPlantException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
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
import java.util.HashMap;
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
                    boolean owner,
                    Model model,
                    String...errorMessages
    ) {
        this.updateGardensSidebar(model, gardenService, userService);

        if (errorMessages.length > 0) {
            model.addAttribute("tagErrors", errorMessages[0]);
        }

        model.addAttribute("garden", garden);
        model.addAttribute("editGardenUri", editGardenUri.toString());
        model.addAttribute("newPlantUri", newPlantUri.toString());
        model.addAttribute("plants", plants);
        model.addAttribute("owner", owner);
        model.addAttribute("editPlantUriString", EDIT_PLANT_URI_STRING);
        model.addAttribute("uploadPlantImageUriString", UPLOAD_PLANT_IMAGE_URI_STRING);
        model.addAttribute("tags", garden.getTags());
        model.addAttribute("tagFormSubmissionUri", newGardenTagUri(garden.getId()));
        model.addAttribute("makeGardenPublic", makeGardenPublicUri(garden.getId()));
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
        logger.info("{}, {}",
                optionalGarden.get().getOwner(), userService.getAuthenticatedUser());
        if (optionalGarden.isEmpty() ||
                (optionalGarden.get().getOwner() != userService.getAuthenticatedUser() &&
                !optionalGarden.get().getPublicGarden())) {
            throw new NoSuchGardenException(gardenId);
        }
        boolean owner = optionalGarden.get().getOwner() == userService.getAuthenticatedUser();
        return loadGardenPage(
                        optionalGarden.get(),
                        editGardenUri(gardenId),
                        newPlantUri(gardenId),
                        plantService.getAllPlantsInGarden(optionalGarden.get()),
                owner,
                model
        );
    }

    /**
     * Set up view garden page and display attributes but no editing features to friend of owner.
     *
     * @return Thyme leaf html template of the view garden page.
     */
    @GetMapping(VIEW_FRIENDS_GARDEN_URI_STRING)
    public String displayFriendsGarden(
            @PathVariable long gardenId,
            @PathVariable long friendId,
            Model model
    ) throws NoSuchGardenException {
        logger.info("GET {}", viewFriendsGardenUri(friendId, gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty() || !optionalGarden.get().getOwner().getFriends().contains(userService.getAuthenticatedUser())) {
            throw new NoSuchGardenException(gardenId);
        }
        return loadGardenPage(
                optionalGarden.get(),
                editGardenUri(gardenId),
                newPlantUri(gardenId),
                plantService.getAllPlantsInGarden(optionalGarden.get()),
                false,
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

    /**
     * Changes the garden so that it is public (viewable by all)
     * @param gardenId                  The Id of the garden being made public
     * @param publicGarden              A string of whether the garden should be public or not
     * @return                          The edit garden page the user was already on
     * @throws NoSuchGardenException    If the garden can't be found by the given Id will throw this error
     */
    @PostMapping(MAKE_GARDEN_PUBLIC_STRING)
    public String makeGardenPublic(@PathVariable long gardenId, @RequestParam(required = false) String publicGarden, Model model)
    throws NoSuchGardenException {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        boolean isGardenPublic = false;
        if (optionalGarden.get().getVerifiedDescription()) {
            isGardenPublic = publicGarden != null && (publicGarden.equals("true"));
        }
        optionalGarden.get().setPublicGarden(isGardenPublic);
        gardenService.saveGarden(optionalGarden.get());
        return loadGardenPage(
                optionalGarden.get(),
                editGardenUri(gardenId),
                newPlantUri(gardenId),
                plantService.getAllPlantsInGarden(optionalGarden.get()),
                true,
                model
        );
    }

    /**
     * Create new tag for a garden.
     *
     * @param model Model to add attributes to
     * @param gardenId Id of garden
     * @param tagName User inputted tag name
     * @return Redirect to view garden page
     * @throws NoSuchGardenException If garden is not found, either by wrong/unauthorized owner
     * or does not exist.
     */
    @PostMapping(NEW_GARDEN_TAG_URI_STRING)
    public String submitGardenTag(Model model,
                                  @PathVariable long gardenId,
                                  @RequestParam(name = "tagName", required = false) String tagName) throws NoSuchGardenException {
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);

        if (optionalGarden.isEmpty() || optionalGarden.get().getOwner() != userService.getAuthenticatedUser()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();
        String errorMessages = ErrorChecker.tagNameErrors(tagName);


        if (!errorMessages.isEmpty())
            model.addAttribute("tagErrors", errorMessages);
        else if (tagService.findByName(tagName) == null || !tagService.findByName(tagName).getGardens().contains(garden))
            tagService.saveTag(tagName, garden);

        boolean owner = userService.getAuthenticatedUser().getId() == garden.getOwner().getId();
        return loadGardenPage(
                optionalGarden.get(),
                editGardenUri(gardenId),
                newPlantUri(gardenId),
                plantService.getAllPlantsInGarden(optionalGarden.get()),
                owner,
                model,
                errorMessages
        );
    }


}
