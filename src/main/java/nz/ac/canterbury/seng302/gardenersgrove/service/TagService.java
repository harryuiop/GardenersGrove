package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * JPA does not have support for adding a limit parameter
     * so the logic is completed in the service.
     *
     * @param query User entered tag.
     * @param limit Maximum number of suggestions shown
     * @return List of tag name suggestions.
     */
    public List<String> findAutocompleteSuggestions(String query, int limit) {
        List<Tag> tags = tagRepository.findByNameContains(query);
        int subListLimit = Math.min(limit, tags.size());
        return tags.subList(0, subListLimit).stream().map(Tag::getName).toList();
    }

    /**
     * Delete all tags in tag repository.
     */
    public void deleteAll() {
        tagRepository.deleteAll();
    }

    public Tag findByName(String name) {

        return tagRepository.findByName(name);
    }
}