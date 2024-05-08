package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.ResponseStatuses.NoSuchGardenException;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.location.CountryCode;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Feature;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.LocationService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@Controller
public class GardenController extends GardensSidebar {
    @Value("${maptiler.api.key}")
    private String apiKey;
    Logger logger = LoggerFactory.getLogger(GardenController.class);
    private final GardenService gardenService;
    private final UserService userService;
    private final LocationService locationService;
    private final MapTilerGeocoding mapTilerGeocoding;
    private String refererUrl;

    /**
     * The PlantFormController constructor need not be called ever.
     * It is autowired in by Spring at run time to inject instances of all the necessary dependencies.
     *
     * @param gardenService   The Garden database access object.
     * @param userService     The User database access object.
     * @param locationService The Location database access object.
     */
    @Autowired
    public GardenController(
            GardenService gardenService,
            UserService userService,
            LocationService locationService,
            MapTilerGeocoding mapTilerGeocoding
    ) {
        this.gardenService = gardenService;
        this.userService = userService;
        this.locationService = locationService;
        this.mapTilerGeocoding = mapTilerGeocoding;
    }

    /**
     * Loads the garden form with the given errors and pre-filled values.
     *
     * @param formFieldErrorMessages A map from form fields to error messages relevant to that field.
     * @param gardenName             The name of the garden to pre-fill the form with.
     * @param gardenSize             The size of the garden to pre-fill the form with.
     * @param country                The country to pre-fill the form with.
     * @param city                   The city to pre-fill the form with.
     * @param streetAddress          The street address to pre-fill the form with.
     * @param suburb                 The suburb to pre-fill the form with.
     * @param postcode               The postcode to pre-fill the form with.
     * @param formSubmissionUri      The URI to submit the form to.
     * @param model                  object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    private String loadGardenForm(
            Map<String, String> formFieldErrorMessages,
            String gardenName,
            Float gardenSize,
            String gardenDescription,
            String country,
            String city,
            String streetAddress,
            String suburb,
            String postcode,
            URI formSubmissionUri,
            Model model
    ) {
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAllAttributes(formFieldErrorMessages);

        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenSize", gardenSize);
        model.addAttribute("gardenDescription", gardenDescription);
        model.addAttribute("country", country);
        model.addAttribute("city", city);
        model.addAttribute("streetAddress", streetAddress);
        model.addAttribute("suburb", suburb);
        model.addAttribute("postcode", postcode);

        model.addAttribute("formSubmissionUri", formSubmissionUri);
        model.addAttribute("previousPage", this.refererUrl);

        return "gardenForm";
    }

    /**
     * Add the necessary attributes to the model for showing the garden location popup.
     *
     * @param gardenId The ID number of the garden.
     * @param model    The map used to pass data through to thymeleaf.
     */
    public void loadLocationErrorMessagePopup(long gardenId, Model model) {
        model.addAttribute("viewGardenUri", viewGardenUri(gardenId));
        model.addAttribute("editGardenUri", editGardenUri(gardenId));
    }

    /**
     * Serves the garden form to the user, passing user information to the HTML.
     *
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping(NEW_GARDEN_URI_STRING)
    public String createGarden(
            @RequestHeader(required = false) String referer,
            Model model
    ) {
        logger.info("GET {}", newGardenUri());

        if (referer == null) {
            referer = VIEW_ALL_GARDENS_URI_STRING;
        }
        this.refererUrl = referer;
        return loadGardenForm(
                Map.of(),
                null, null, null, null, null, null, null, null,
                newGardenUri(),
                model
        );
    }

    /**
     * Submits form and saves the new garden to the database.
     *
     * @param gardenName The name of the garden as input by the user.
     * @param gardenSize The size of the garden as input by the user.
     * @param model      object that passes data through to the HTML.
     * @return thymeleaf HTML template for the new garden.
     */
    @PostMapping(NEW_GARDEN_URI_STRING)
    public String submitNewGarden(
            @RequestParam String gardenName,
            @RequestParam(required = false) Float gardenSize,
            @RequestParam String gardenDescription,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam(required = false) String streetAddress,
            @RequestParam(required = false) String suburb,
            @RequestParam(required = false) String postcode,
            Model model
    ) {
        logger.info("POST {}", newGardenUri());

        Map<String, String> errors = ErrorChecker.gardenFormErrors(
                gardenName, gardenSize, gardenDescription, country, city, streetAddress, suburb, postcode
        );
        if (errors.isEmpty()) {
            Location locationEntity = new Location(country, city);

            boolean locationFound = updateLocationCoordinates(locationEntity, streetAddress, country, city);

            locationEntity.setSuburb(suburb);
            locationEntity.setPostcode(postcode);
            locationEntity.setStreetAddress(streetAddress);
            Garden garden = new Garden(userService.getAuthenticatedUser(), gardenName, locationEntity, gardenSize);
            gardenService.saveGarden(garden);

            if (locationFound) {
                return "redirect:" + viewGardenUri(garden.getId());
            } else {
                loadLocationErrorMessagePopup(garden.getId(), model);
            }
        }
        return loadGardenForm(
                errors,
                gardenName, gardenSize, gardenDescription, country, city, streetAddress, suburb, postcode,
                newGardenUri(),
                model
        );
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
            @RequestHeader(required = false) String referer,
            Model model
    ) throws NoSuchGardenException {
        logger.info("GET {}", editGardenUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty() || optionalGarden.get().getOwner() != userService.getAuthenticatedUser()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        if (referer == null) {
            referer = viewGardenUri(gardenId).toString();
        }
        this.refererUrl = referer;

        Location gardenLocation = garden.getLocation();
        return loadGardenForm(
                Map.of(),
                garden.getName(), garden.getSize(), garden.getDescription(), gardenLocation.getCountry(), gardenLocation.getCity(),
                gardenLocation.getStreetAddress(), gardenLocation.getSuburb(), gardenLocation.getPostcode(),
                editGardenUri(gardenId),
                model
        );
    }

    /**
     * Submits form and saves the updated garden details to the database.
     *
     * @param gardenId   The id of the garden to edit.
     * @param gardenName The name of the garden as input by the user.
     * @param gardenSize The size of the garden as input by the user.
     * @param model      object that passes data through to the HTML.
     * @return thymeleaf HTML template for the new garden.
     */
    @PostMapping(EDIT_GARDEN_URI_STRING)
    public String submitGardenEdit(
            @PathVariable long gardenId,
            @RequestParam String gardenName,
            @RequestParam(required = false) Float gardenSize,
            @RequestParam String gardenDescription,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam(required = false) String streetAddress,
            @RequestParam(required = false) String suburb,
            @RequestParam(required = false) String postcode,
            Model model
    ) throws NoSuchGardenException {
        logger.info("POST {}", editGardenUri(gardenId));

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isEmpty()) {
            throw new NoSuchGardenException(gardenId);
        }
        Garden garden = optionalGarden.get();

        Map<String, String> errors = ErrorChecker.gardenFormErrors(
                gardenName, gardenSize, gardenDescription, country, city, streetAddress, suburb, postcode
        );
        if (errors.isEmpty()) {
            Location locationEntity = garden.getLocation();

            boolean locationFound = true;
            // Get new location from API request
            if (locationEntity.getStreetAddress() == null
                    || !locationEntity.getStreetAddress().equals(streetAddress)
                    || !locationEntity.isCoordinatesSet()) {
                locationFound = updateLocationCoordinates(locationEntity, streetAddress, country, city);
            }
            locationEntity.setCountry(country);
            locationEntity.setSuburb(suburb);
            locationEntity.setCity(city);
            locationEntity.setPostcode(postcode);
            locationEntity.setStreetAddress(streetAddress);
            locationService.saveLocation(locationEntity);

            garden.setName(gardenName);
            garden.setSize(gardenSize);
            garden.setDescription(gardenDescription);
            garden.setLocation(locationEntity);
            gardenService.saveGarden(garden);
            if (locationFound) {
                return "redirect:" + viewGardenUri(garden.getId());
            } else {
                loadLocationErrorMessagePopup(gardenId, model);
            }
        }
        return loadGardenForm(
                errors,
                gardenName, gardenSize, gardenDescription, country, city, streetAddress, suburb, postcode,
                editGardenUri(gardenId),
                model
        );
    }

    /**
     * Call MapTiler API to update location coordinates.
     *
     * @param location      Location Entity to update
     * @param streetAddress Input street address by form box.
     * @param country       Input country by form box.
     * @param city          Input city by form box.
     * @return {@code true} if the coordinates were successfully set, {@code false} otherwise.
     */
    private boolean updateLocationCoordinates(Location location, String streetAddress, String country, String city) {
        // Country code allows more accurate searching by filtering by just that country
        String countryCode = CountryCode.getAlphaTwoCountryCodeFromName(country);

        Feature locationFeature;
        String query;
        if (streetAddress != null) {
            query = streetAddress + " " + city + " " + country;
        } else {
            query = city + " " + country;
        }
        locationFeature = mapTilerGeocoding.getFirstSearchResult(query, countryCode, apiKey);
        if (locationFeature == null) {
            return false;
        }

        location.setLngLat(locationFeature.getCenter());
        return true;
    }
}
