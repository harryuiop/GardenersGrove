package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TagServiceTest {
    @Autowired
    private TagRepository tagRepository;
    private TagService tagService;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        tagService = new TagService(tagRepository);
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
        tagRepository.deleteAll();
    }

    @Test
    void saveExistingTag() {
        Garden garden1 = new Garden(user, "Test Garden1", null, new Location("New Zealand", "Christchurch"), null);
        Garden garden2 = new Garden(user, "Test Garden2", null, new Location("New Zealand", "Christchurch"), null);
        gardenRepository.save(garden1);
        gardenRepository.save(garden2);

        tagService.saveTag("tag", garden1);
        tagService.saveTag("tag", garden2);

        List<Tag> allTags = (List<Tag>) tagRepository.findAll();
        Assertions.assertEquals(1, allTags.size());
        Assertions.assertEquals(2, allTags.get(0).getGardens().size());
        Assertions.assertTrue(allTags.get(0).getGardens().contains(garden1));
        Assertions.assertTrue(allTags.get(0).getGardens().contains(garden2));
    }

    @Test
    void saveNewTag() {
        Garden garden1 = new Garden(user, "Test Garden1", null, new Location("New Zealand", "Christchurch"), null);
        gardenRepository.save(garden1);

        tagService.saveTag("tag", garden1);

        List<Tag> allTags = (List<Tag>) tagRepository.findAll();
        Assertions.assertEquals(1, allTags.size());
        Assertions.assertEquals(1, allTags.get(0).getGardens().size());
        Assertions.assertTrue(allTags.get(0).getGardens().contains(garden1));
    }

}
