package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Create new tag if tag does not exist, otherwise reference existing tag.
     *
     * @param tagName name of tag
     */
    public void saveTag(String tagName, Garden garden) {
        if (tagRepository.existsByName(tagName)) {
            Tag foundTag = tagRepository.findByName(tagName);
            tagRepository.findByName(tagName).addGarden(garden);
            tagRepository.save(foundTag);
            return;
        }
        tagRepository.save(new Tag(tagName, garden));
    }

    /**
     * Get tag suggestions based on user input.
     * Used by javascript to show the autocomplete suggestions.
     *
     * @param query User entered tag.
     * @param limit Maximum number of suggestions shown
     * @return List of tag name suggestions.
     */
    public List<String> findAutocompleteSuggestions(String query, int limit) {
        List<Tag> tags = tagRepository.findByNameStartingWith(query);
        List<String> tagStrings = new ArrayList<>();
        for (Tag tag: tags) {
            tagStrings.add(tag.getName());
        }
        return tagStrings.subList(0, Math.min(limit, tagStrings.size()));
    }
    public void deleteAll() {
        tagRepository.deleteAll();
    }
}