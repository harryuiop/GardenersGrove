package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.NavBar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ImageValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Plant;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.exceptions.NoSuchPlantException;
import nz.ac.canterbury.seng302.gardenersgrove.service.*;
import nz.ac.canterbury.seng302.gardenersgrove.utility.ImageStore;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

/**
 * Controller for the view garden page. For viewing a specific garden.
 */
@Controller
public class ViewGardenController extends NavBar {
    Logger logger = LoggerFactory.getLogger(ViewGardenController.class);
    private final GardenService gardenService;
    private final PlantService plantService;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final TagService tagService;
    private final ErrorChecker errorChecker;
    private final WeatherService weatherService;

    /**
     * Spring will automatically call this constructor at runtime to inject the dependencies.
     *
     * @param gardenService A Garden database access object.
     * @param plantService  A Plant database access object.
     * @param userService   A User database access object.
     * @param tagService    A Tag database access object.
     * @param weatherService Object for main interactions with Open-Meteo API
     */
    @Autowired
    public ViewGardenController(GardenService gardenService, PlantService plantService, UserService userService,
                                TagService tagService, FriendshipService friendshipService, ErrorChecker errorChecker,
                                WeatherService weatherService) {
        this.gardenService = gardenService;
        this.plantService = plantService;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.tagService = tagService;
        this.weatherService = weatherService;
        this.errorChecker = errorChecker;
    }

    /**
     * Returns the view page for a specific garden
     *
     * @param garden        The garden being viewed
     * @param editGardenUri The URI for edit garden
     * @param newPlantUri   The URi for new plant form
     * @param plants        The plants in the garden
     * @param owner         Whether the viewer is the owner of the garden or not
     * @param model         Puts the data into the template
     * @param errorMessages Any plant image errors that occurred when re-loading
     * @return The view garden page is displayed to user
     */
    private String loadGardenPage(
                    Garden garden,
                    URI editGardenUri,
                    URI newPlantUri,
                    List<Plant> plants,
                    boolean owner,
                    Model model,
                    String cookies,
                    String... errorMessages
    ) throws InterruptedException {
        this.updateGardensNavBar(model, gardenService, userService);

        if (errorMessages.length > 0) {
            model.addAttribute("tagErrors", errorMessages[0]);
        }

        List<WeatherData> weatherData = weatherService.getWeatherData(garden.getLocation().getLng(), garden.getLocation().getLat());
        int pastDays = weatherService.getPastDaysCount();
        int forecastedDays = weatherService.getForecastDayCount();
        List<WeatherData> shownWeatherData = weatherData.subList(pastDays, pastDays + forecastedDays);

        model.addAttribute("garden", garden);
        model.addAttribute("editGardenUri", editGardenUri.toString());
        model.addAttribute("newPlantUri", newPlantUri.toString());
        model.addAttribute("plants", plants);
        model.addAttribute("owner", owner);
        model.addAttribute("editPlantUriString", EDIT_PLANT_URI_STRING);
        model.addAttribute("monitorGardenUriString", monitorGardenUri(garden.getId()));
        model.addAttribute("uploadPlantImageUriString", UPLOAD_PLANT_IMAGE_URI_STRING);
        model.addAttribute("tags", garden.getTags());
        model.addAttribute("tagFormSubmissionUri", newGardenTagUri(garden.getId()));
        model.addAttribute("makeGardenPublic", makeGardenPublicUri(garden.getId()));
        model.addAttribute("weatherData", shownWeatherData);
        model.addAttribute("advice", weatherService.getWeatherAdvice(weatherData));
        model.addAttribute("isRainy", weatherService.isRainy(weatherData));
        model.addAttribute("popupClosed", cookies);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd MMM yyyy"));
        model.addAttribute("currentDayOfWeek", LocalDate.now().getDayOfWeek());
        model.addAttribute("tomorrowDay", LocalDate.now().plusDays(1).getDayOfWeek());
        return "viewGarden";
    }

    /**
     * Set up view garden page and display attributes.
     *
     * @param gardenId The id of the garden being viewed
     * @param model    Puts the data into the template to be viewed
     * @return Thymeleaf html template of the view garden page.
     */
    @GetMapping(VIEW_GARDEN_URI_STRING)
    public String displayGarden(
            @PathVariable long gardenId,
            @CookieValue(value="rainPopupSeen", defaultValue = "false") String popupClose,
            Model model
    ) throws NoSuchGardenException, InterruptedException {
        logger.info("GET {}", viewGardenUri(gardenId));


        if(Objects.equals(popupClose, "false")) {
            logger.info("Looks like I did not get cookies from browser");
        } else {
            logger.info("Looks like I got cookies from browser");
        }

        User currentUser = userService.getAuthenticatedUser();


        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);

        if (optionalGarden.isEmpty()
                || optionalGarden.get().getOwner().getId() != currentUser.getId()
                && !optionalGarden.get().isGardenPublic()
                && !friendshipService.areFriends(optionalGarden.get().getOwner(), currentUser)) {
            throw new NoSuchGardenException(gardenId);
        }

        boolean owner = optionalGarden.get().getOwner() == currentUser;
        return loadGardenPage(
                        optionalGarden.get(),
                        editGardenUri(gardenId),
                        newPlantUri(gardenId),
                        plantService.getAllPlantsInGarden(optionalGarden.get()),
                        owner,
                        model,
                        popupClose
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
     * @param gardenId                  The id of the garden being made public
     * @param publicGarden              A string of whether the garden should be public or not
     * @param redirectAttributes        Add attributes to that are still there after the redirect
     * @return                          The edit garden page the user was already on
     * @throws NoSuchGardenException    If the garden can't be found by the given id will throw this error
     */
    @PostMapping(MAKE_GARDEN_PUBLIC_STRING)
    public String makeGardenPublic(@PathVariable long gardenId, @RequestParam(required = false)
                                    String publicGarden,
                                    RedirectAttributes redirectAttributes)
    throws NoSuchGardenException {
        logger.info("POST make public");
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        boolean isGardenPublic = false;
        if (optionalGarden.get().getVerifiedDescription()) {
            isGardenPublic = publicGarden != null && (publicGarden.equals("true"));
        } else {
            redirectAttributes.addFlashAttribute("gardenDescriptionError",
                    "Garden description has not been checked against our language standards. " +
                            "You must edit your description before this garden can be made public");
        }
        optionalGarden.get().setIsGardenPublic(isGardenPublic);
        gardenService.saveGarden(optionalGarden.get());
        return "redirect:" + viewGardenUri(gardenId);
    }

    /**
     * Create new tag for a garden.
     *
     * @param redirectAttributes    Add attributes to that are still there after the redirect
     * @param gardenId              The garden's ID number
     * @param tagName               User inputted tag name
     * @return Redirect to view garden page
     * @throws NoSuchGardenException If garden is not found, either by wrong/unauthorized owner
     *                               or does not exist.
     */
    @PostMapping(NEW_GARDEN_TAG_URI_STRING)
    public String submitGardenTag(
            @PathVariable long gardenId,
            @RequestParam(required = false) String tagName,
            RedirectAttributes redirectAttributes
    ) throws NoSuchGardenException {
        logger.info("POST tags");
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty() || optionalGarden.get().getOwner().getId() != userService.getAuthenticatedUser().getId()) {
            throw new NoSuchGardenException(gardenId);
        }

        Garden garden = optionalGarden.get();
        String errorMessages = errorChecker.tagNameErrors(tagName);
        String lowerTagName = tagName.toLowerCase();

        if (!errorMessages.isEmpty())
            redirectAttributes.addFlashAttribute("tagErrors", errorMessages);
        else if (tagService.findByName(lowerTagName) == null || !tagService.findByName(lowerTagName).getGardens().contains(garden))
            tagService.saveTag(lowerTagName, garden);

        return "redirect:" + viewGardenUri(gardenId);
    }
}


