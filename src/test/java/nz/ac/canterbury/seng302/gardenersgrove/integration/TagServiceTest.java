package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.*;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
class TagServiceTest {
    @Autowired
    @SpyBean
    private TagRepository tagRepositorySpy;
    private TagService tagService;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;
    private User user;

    private final int autocompleteLimit = 3;

    private Garden garden1;


    @BeforeEach
    void setUp() {
        tagService = new TagService(tagRepositorySpy);
        if (user == null) {
            user = new User(
                    "test@domain.net",
                    "Test",
                    "User",
                    "Password1!",
                    "2000-01-01"
            );
            userRepository.save(user);
        }
        gardenRepository.deleteAll();
        tagRepositorySpy.deleteAll();
        garden1 = new Garden(user, "Test Garden1", new Location("New Zealand", "Christchurch"), null);
        gardenRepository.save(garden1);
    }

    @Test
    void saveExistingTag() {
        Garden garden2 = new Garden(user, "Test Garden2", new Location("New Zealand", "Christchurch"), null);
        gardenRepository.save(garden2);

        tagService.saveTag("tag", garden1);
        tagService.saveTag("tag", garden2);

        List<Tag> allTags = (List<Tag>) tagRepositorySpy.findAll();
        Assertions.assertEquals(1, allTags.size());
        Assertions.assertEquals(2, allTags.get(0).getGardens().size());
        Assertions.assertTrue(allTags.get(0).getGardens().contains(garden1));
        Assertions.assertTrue(allTags.get(0).getGardens().contains(garden2));
    }

    @Test
    void saveNewTag() {
        tagService.saveTag("tag", garden1);

        List<Tag> allTags = (List<Tag>) tagRepositorySpy.findAll();
        Assertions.assertEquals(1, allTags.size());
        Assertions.assertEquals(1, allTags.get(0).getGardens().size());
        Assertions.assertTrue(allTags.get(0).getGardens().contains(garden1));
    }

    @Test
    void autocompleteSuggestions_noSuggestions() {
        String query = "tag";
        List<Tag> mockedTags = new ArrayList<>();
        Mockito.when(tagRepositorySpy.findByNameContains(anyString())).thenReturn(mockedTags);
        List<String> tagNames = tagService.findAutocompleteSuggestions(query, autocompleteLimit);

        Assertions.assertEquals(0, tagNames.size());
    }

    @Test
    void autocompleteSuggestions_singleSuggestions() {
        String query = "tag";
        List<Tag> mockedTags = new ArrayList<>();
        mockedTags.add(new Tag("tag1", garden1));
        Mockito.when(tagRepositorySpy.findByNameContains(anyString())).thenReturn(mockedTags);
        List<String> tagNames = tagService.findAutocompleteSuggestions(query, autocompleteLimit);

        Assertions.assertEquals(1, tagNames.size());
    }

    @Test
    void autocompleteSuggestions_multipleSuggestionsWithinLimit() {
        String query = "tag";
        List<Tag> mockedTags = new ArrayList<>();
        Tag tag1 = new Tag("tag1", garden1);
        Tag tag2 = new Tag("tag1", garden1);
        mockedTags.add(tag1);
        mockedTags.add(tag2);
        Mockito.when(tagRepositorySpy.findByNameContains(anyString())).thenReturn(mockedTags);
        List<String> tagNames = tagService.findAutocompleteSuggestions(query, autocompleteLimit);

        Assertions.assertEquals(2, tagNames.size());
        Assertions.assertTrue(tagNames.contains(tag1.getName()));
        Assertions.assertTrue(tagNames.contains(tag2.getName()));
    }

    @Test
    void autocompleteSuggestions_multipleSuggestionsAtLimit() {
        String query = "tag";
        List<Tag> mockedTags = new ArrayList<>();
        Tag tag1 = new Tag("tag1", garden1);
        Tag tag2 = new Tag("tag2", garden1);
        Tag tag3 = new Tag("tag3", garden1);
        mockedTags.add(tag1);
        mockedTags.add(tag2);
        mockedTags.add(tag3);
        Mockito.when(tagRepositorySpy.findByNameContains(anyString())).thenReturn(mockedTags);
        List<String> tagNames = tagService.findAutocompleteSuggestions(query, autocompleteLimit);

        Assertions.assertEquals(3, tagNames.size());
        Assertions.assertTrue(tagNames.contains(tag1.getName()));
        Assertions.assertTrue(tagNames.contains(tag2.getName()));
        Assertions.assertTrue(tagNames.contains(tag3.getName()));
    }

    @Test
    void autocompleteSuggestions_multipleSuggestionsExceedingLimit() {
        String query = "tag";
        List<Tag> mockedTags = new ArrayList<>();
        Tag tag1 = new Tag("tag1", garden1);
        Tag tag2 = new Tag("tag2", garden1);
        Tag tag3 = new Tag("tag3", garden1);
        Tag tag4 = new Tag("tag4", garden1);
        mockedTags.add(tag1);
        mockedTags.add(tag2);
        mockedTags.add(tag3);
        mockedTags.add(tag4);
        Mockito.when(tagRepositorySpy.findByNameContains(anyString())).thenReturn(mockedTags);
        List<String> tagNames = tagService.findAutocompleteSuggestions(query, autocompleteLimit);

        Assertions.assertEquals(3, tagNames.size());
        Assertions.assertTrue(tagNames.contains(tag1.getName()));
        Assertions.assertTrue(tagNames.contains(tag2.getName()));
        Assertions.assertTrue(tagNames.contains(tag3.getName()));
    }

}
