package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void deleteAll() {
        tagRepository.deleteAll();
    }
}