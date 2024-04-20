package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.ErrorChecker;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.location.CountryCode;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.Feature;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.LocationService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Controller for garden form. For creating new gardens.
 * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
 */
@Controller
public class GardenFormController extends GardensSidebar {

    @Value("${maptiler.api.key}")
    private String apiKey;

    Logger logger = LoggerFactory.getLogger(GardenFormController.class);
    private final GardenService gardenService;
    private final LocationService locationService;
    private final UserService userService;
    private final ErrorChecker gardenValidator;

    private long gardenId = 0;

    @Autowired
    public GardenFormController(GardenService gardenService, LocationService locationService, UserService userService) {
        this.gardenService = gardenService;
        this.locationService = locationService;
        this.userService = userService;
        this.gardenValidator = new ErrorChecker();
    }

    /**
     * Gets form to be displayed, and passes previous form values to the HTML.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML gardenForm template.
     */
    @GetMapping("/gardenform")
    public String form(Model model) {
        logger.info("GET /form");
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAttribute("gardenNameError", "");
        model.addAttribute("gardenLocationError", "");
        model.addAttribute("gardenSizeError", "");
        return "gardenForm";
    }


    /**
     * Submits form and saves the garden to the database.
     * @param gardenName The name of the garden as input by the user.
     * @param gardenSize The size of the garden as input by the user.
     * @param country Country of Garden as input from user.
     * @param city City of Garden as input from user.
     * @param streetAddress Street Address (Street with optional number) as input from user.
     * @param suburb Suburb of Garden as input from user or autogenerated from street address.
     * @param postcode Postcode of Garden as input from user or autogenerated from street address.
     * @param model object that passes data through to the HTML.
     * @return thymeleaf HTML template to redirect to.
     */
    @PostMapping("/gardenform")
    public String submitForm(@RequestParam(name = "gardenName") String gardenName,
                             @RequestParam(name = "gardenSize", required = false) Float gardenSize,
                             @RequestParam(name = "country") String country,
                             @RequestParam(name = "city") String city,
                             @RequestParam(name = "streetAddress", required = false) String streetAddress,
                             @RequestParam(name = "suburb", required = false) String suburb,
                             @RequestParam(name = "postcode", required = false) String postcode,
                             @RequestParam(name = "ignoreApiCall", required = false) Boolean ignoreApiCall,
                             Model model) {
        logger.info("POST /form");
        if (ignoreApiCall == null) ignoreApiCall = false;
        // This needs the gardenLocation removed
        Map<String, String> errors = gardenValidator.gardenFormErrors(gardenName, gardenSize,
                country, city, streetAddress, suburb, postcode);

        if (errors.isEmpty()) {
            boolean showLocationNotFoundBox = true;
            Location locationEntity = new Location(country, city);
            if (!ignoreApiCall) {
                MapTilerGeocoding mapTilerGeocoding = new MapTilerGeocoding();
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
                if (locationFeature != null) {
                    locationEntity.setLngLat(locationFeature.getCenter());
                    showLocationNotFoundBox = false;
                }
            }

            locationEntity.setSuburb(suburb);
            locationEntity.setPostcode(Integer.parseInt(postcode));
            locationEntity.setStreetAddress(streetAddress);

            locationService.saveLocation(locationEntity);
            Garden garden = new Garden(gardenName, locationEntity, gardenSize);
            gardenService.saveGarden(garden);
            gardenId = garden.getId();
            if (showLocationNotFoundBox) {
                model.addAttribute("noLocationFound", true);
            } else {
                return "redirect:/view-garden?gardenId=" + gardenId;
            }
        }
        else {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
        }
        this.updateGardensSidebar(model, gardenService, userService);
        model.addAttribute("gardenName", gardenName);
        model.addAttribute("gardenSize", gardenSize);
        model.addAttribute("country", country);
        model.addAttribute("city", city);
        model.addAttribute("streetAddress", streetAddress);
        model.addAttribute("suburb", suburb);
        model.addAttribute("postcode", postcode);
        return "gardenForm";
    }

    /**
     * Go to view garden page if garden has been created.
     *
     * @return thymeleaf HTML gardenForm template or view garden redirect.
     */
    @PostMapping("/view-garden-from-create")
    public String goToViewGarden(Model model) {
        logger.info("POST /goToViewGardenForm");
        if (gardenId > 0) {
            return "redirect:/view-garden?gardenId=" + gardenId;
        }
        logger.info("Garden id not set, returning to garden form page.");
        model.addAttribute("noLocationFound", false);
        return "gardenForm";
    }

    /**
     * Go to edit garden page if garden has been created.
     *
     * @return thymeleaf HTML gardenForm template or edit garden redirect.
     */
    @PostMapping("/edit-garden-redirect")
    public String goToEditGarden(Model model) {
        logger.info("POST /goToEditGardenForm");
        if (gardenId > 0) {
            return "redirect:/edit-garden?gardenId=" + gardenId;
        }
        logger.info("Garden id not set (Garden not created), returning to garden form page.");
        model.addAttribute("noLocationFound", false);
        return "gardenForm";
    }
}
