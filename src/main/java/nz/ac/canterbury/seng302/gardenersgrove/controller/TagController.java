package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * Get autocomplete suggestions based on users search result.
     *
     * @param query User input for tag field.
     * @return Autocomplete suggestions based on user input.
     * A set max amount of suggestions shown, given by MAX_SUGGESTIONS variable.
     */
    @GetMapping("/show-autocomplete")
    public List<String> getSearchResults(@RequestParam String query) {
        logger.info("GET /show-autocomplete");
        return tagService.findAutocompleteSuggestions(query, MAX_SUGGESTIONS);
    }
}
