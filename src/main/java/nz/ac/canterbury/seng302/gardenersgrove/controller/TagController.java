package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.location.CountryCode;
import nz.ac.canterbury.seng302.gardenersgrove.location.map_tiler_response.SearchResult;
import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Called through javascript for showing autocomplete suggestions for tags.
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    Logger logger = LoggerFactory.getLogger(TagController.class);
    private final TagService tagService;

    private static final int MAX_SUGGESTIONS = 3;

    /**
     * Constructor for TagController.
     *
     * @param tagService for finding tag suggestions
     */
    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/show-autocomplete")
    public List<String> getSearchResults(@RequestParam String query) {
        logger.info("GET /show-autocomplete");
        return tagService.findAutocompleteSuggestions(query, MAX_SUGGESTIONS);
    }
}
