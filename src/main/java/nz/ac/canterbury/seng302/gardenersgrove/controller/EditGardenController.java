package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.components.GardensSidebar;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * Controller for editing garden form.
 */
@Controller
public class EditGardenController extends GardensSidebar {
    Logger logger = LoggerFactory.getLogger(EditGardenController.class);
    private final ErrorChecker gardenValidator = new ErrorChecker();

    private final GardenService gardenService;
    private final UserService userService;
    private final LocationService locationService;

    /**
     * Note the @link{Autowired} annotation giving us access to the @link{FormService} class automatically
     * @param gardenService the linking agent
     */
    @Autowired
    public EditGardenController(GardenService gardenService, UserService userService, LocationService locationService) {
        this.gardenService = gardenService;
        this.userService = userService;
        this.locationService = locationService;
    }

    /**
     * Submit edit garden form and update garden entity.
     *
     * @param gardenName user entered garden name
     * @param gardenSize user entered garden size
     * @param country user entered country
     * @param city user entered city
     * @param streetAddress user entered street address
     * @param suburb user entered suburb
     * @param postcode user entered postcode
     * @param ignoreApiCall optional parameter to ignore api call (usually used in tests)
     * @param model represents the results to from thymeleaf
     *
     * @return if the submission is valid, redirects to the garden view page, if invalid reloads the same edit page
     */
    @PostMapping("/edit-garden")
    public String submitForm(@RequestParam(name="gardenName") String gardenName,
                             @RequestParam(name = "gardenSize", required=false) Float gardenSize,
                             @RequestParam(name = "country") String country,
                             @RequestParam(name = "city") String city,
                             @RequestParam(name = "streetAddress", required = false) String streetAddress,
                             @RequestParam(name = "suburb", required = false) String suburb,
                             @RequestParam(name = "postcode", required = false) String postcode,
                             @RequestParam(name = "gardenId") Long gardenId,
                             @RequestParam(name = "ignoreApiCall", required = false) Boolean ignoreApiCall,
                             Model model) {
        logger.info("POST /edit-garden");
        Map<String, String> errors = gardenValidator.gardenFormErrors(gardenName, gardenSize,
                country, city, streetAddress, suburb, postcode);
        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (errors.isEmpty()) {
            if (optionalGarden.isPresent()) {
                Garden garden = optionalGarden.get();
                Location locationEntity = garden.getLocation();

                // Get new location from API request
                if (locationEntity.getStreetAddress() == null ||
                        !locationEntity.getStreetAddress().equals(streetAddress)) {
                    if (!ignoreApiCall) {
                        updateLocationCoordinates(locationEntity, streetAddress, country, city);
                    }
                }

                locationEntity.setCountry(country);
                locationEntity.setSuburb(suburb);
                locationEntity.setCity(city);
                locationEntity.setPostcode(Integer.parseInt(postcode));
                locationEntity.setStreetAddress(streetAddress);
                locationService.saveLocation(locationEntity);

                garden.setName(gardenName);
                garden.setSize(gardenSize);
                garden.setLocation(locationEntity);
                gardenService.saveGarden(garden);
            }
        } else {
            this.updateGardensSidebar(model, gardenService, userService);
            for (Map.Entry<String, String> error : errors.entrySet()) {
                model.addAttribute(error.getKey(), error.getValue());
            }
            model.addAttribute("gardenName", gardenName);
            model.addAttribute("gardenSize", gardenSize);
            model.addAttribute("country", country);
            model.addAttribute("city", city);
            model.addAttribute("streetAddress",streetAddress);
            model.addAttribute("suburb", suburb);
            model.addAttribute("postcode", postcode);
            model.addAttribute("gardenId", gardenId);
            return "editGarden";
        }
        return "redirect:/view-garden?gardenId=" + gardenId;
    }

    /**
     * Call MapTiler API to update location coordinates.
     *
     * @param location Location Entity to update
     * @param streetAddress Input street address by form box.
     * @param country Input country by form box.
     * @param city Input city by form box.
     */
    private void updateLocationCoordinates(Location location, String streetAddress, String country, String city) {
        // Country code allows more accurate searching by filtering by just that country
        String countryCode = CountryCode.getAlphaTwoCountryCodeFromName(country);

        Feature locationFeature;
        String query;
        if (streetAddress != null) {
            query = streetAddress + " " + city + " " + country;
        } else {
            query = city + " " + country;
        }
        RestTemplate restTemplate = new RestTemplate();
        locationFeature = restTemplate.getForObject("/maptiler/get-first-search-result?query=" + query + "&countryCode=" + countryCode,
                Feature.class);
        if (locationFeature != null) location.setLngLat(locationFeature.getCenter());
    }




    /**
     * @param model (map-like) representation of results to be used by thymeleaf
     * @param gardenId represents the identifier for the garden in the database
     * @return thymeleaf editGarden
     */
    @GetMapping("/edit-garden")
    public String home(@RequestParam(name = "gardenId") Long gardenId, Model model) {
        logger.info("GET /edit-garden");
        this.updateGardensSidebar(model, gardenService, userService);

        Optional<Garden> optionalGarden = gardenService.getGardenById(gardenId);
        if (optionalGarden.isPresent()) {
            Garden garden = optionalGarden.get();
            model.addAttribute("gardenName", garden.getName());
            model.addAttribute("gardenSize", garden.getSize());
            model.addAttribute("country", garden.getLocation().getCountry());
            model.addAttribute("city", garden.getLocation().getCity());
            model.addAttribute("streetAddress", garden.getLocation().getStreetAddress());
            model.addAttribute("suburb", garden.getLocation().getSuburb());
            model.addAttribute("postcode", garden.getLocation().getPostcode());
            model.addAttribute("gardenId", gardenId);
        }
        return "editGarden";
    }
}
