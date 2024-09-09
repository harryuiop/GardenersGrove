package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublicGardensController {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GardenService gardenService;
    @Autowired
    private GardenRepository gardenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    private final Location location = new Location("New Zealand", "Wellington");

    @BeforeAll
    void setUp() {
        User user1 = userService.addUsers(new User("email1@gmail.com", "fname1", "lname1", "password", "01-01-2000"));
        User user2 = userService.addUsers(new User("email2@gmail.com", "fname2", "lname2", "password", "01-01-2000"));
        User user3 = userService.addUsers(new User("email3@gmail.com", "fname3", "lname3", "password", "01-01-2000"));

        Garden garden1 = gardenService.saveGarden(new Garden(user1, "garden1", "description1", location, (float) 10, true));
        Garden garden2 = gardenService.saveGarden(new Garden(user2, "garden2", "description2", location, (float) 10, true));
        Garden garden3 = gardenService.saveGarden(new Garden(user3, "garden3", "description3", location, (float) 10, true));

        tagService.saveTag("tag1", garden1);
        tagService.saveTag("tag2", garden2);
        tagService.saveTag("tag3", garden3);
    }


}
