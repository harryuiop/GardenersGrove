package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.browsePublicGardensUri;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublicGardensControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GardenRepository gardenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagService tagService;
    private Garden garden1;
    private Garden garden2;
    private Garden garden3;

    @BeforeAll
    void setUp() {
        User user1 = userRepository.save(new User("email1@gmail.com", "fname1", "lname1", "Password1!", "01-01-2000"));
        User user2 = userRepository.save(new User("email2@gmail.com", "fname2", "lname2", "Password1!", "01-01-2000"));
        User user3 = userRepository.save(new User("email3@gmail.com", "fname3", "lname3", "Password1!", "01-01-2000"));

        Garden gardenInit1 = new Garden(user1, "garden1", "description1", new Location("New Zealand", "Wellington"), (float) 10, true);
        gardenInit1.setIsGardenPublic(true);
        Garden gardenInit2 = new Garden(user2, "garden2", "description2", new Location("New Zealand", "Wellington"), (float) 10, true);
        gardenInit2.setIsGardenPublic(true);
        Garden gardenInit3 = new Garden(user3, "garden3", "description3", new Location("New Zealand", "Wellington"), (float) 10, true);
        gardenInit3.setIsGardenPublic(true);

        garden1 = gardenRepository.save(gardenInit1);
        garden2 = gardenRepository.save(gardenInit2);
        garden3 = gardenRepository.save(gardenInit3);
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void checkTagFilterReturnsTwo() throws Exception {
        tagService.saveTag("tag1", garden1);
        tagService.saveTag("tag1", garden2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(browsePublicGardensUri())
            .param("hiddenTagName", "tag1"))
            .andExpect(status().isOk())
            .andReturn();

            ModelAndView modelAndView = result.getModelAndView();
            assert modelAndView != null;
            ModelMap modelMap = modelAndView.getModelMap();

            ArrayList<String> gardens = (ArrayList<String>) modelMap.get("gardenList");
            Assertions.assertEquals(2, gardens.size());
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void checkMultipleTagFilterReturnsTwo() throws Exception {
        tagService.saveTag("tag2", garden2);
        tagService.saveTag("tag3", garden3);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(browsePublicGardensUri())
                        .param("hiddenTagName", "tag2,tag3"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        ModelMap modelMap = modelAndView.getModelMap();

        ArrayList<String> gardens = (ArrayList<String>) modelMap.get("gardenList");
        Assertions.assertEquals(2, gardens.size());
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void checkSearchParameterFilterReturnsOne() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(browsePublicGardensUri())
                        .param("searchParameter", "garden1"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        ModelMap modelMap = modelAndView.getModelMap();

        ArrayList<String> gardens = (ArrayList<String>) modelMap.get("gardenList");
        Assertions.assertEquals(1, gardens.size());
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void checkSearchParameterAndTagsFilterReturnsOne() throws Exception {
        tagService.saveTag("tag1", garden1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(browsePublicGardensUri())
                        .param("hiddenTagName", "tag1")
                        .param("searchParameter", "garden1"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        ModelMap modelMap = modelAndView.getModelMap();

        ArrayList<String> gardens = (ArrayList<String>) modelMap.get("gardenList");
        Assertions.assertEquals(1, gardens.size());
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void checkSearchParameterFilterReturnsZero() throws Exception {
        tagService.saveTag("tag1", garden1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(browsePublicGardensUri())
                        .param("hiddenTagName", "tag2"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        ModelMap modelMap = modelAndView.getModelMap();

        ArrayList<String> gardens = (ArrayList<String>) modelMap.get("gardenList");
        Assertions.assertEquals(0, gardens.size());
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void checkSearchParameterAndTagsFilterReturnsZero() throws Exception {
        tagService.saveTag("tag1", garden1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(browsePublicGardensUri())
                        .param("hiddenTagName", "tag1")
                        .param("searchParameter", "garden2"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assert modelAndView != null;
        ModelMap modelMap = modelAndView.getModelMap();

        ArrayList<String> gardens = (ArrayList<String>) modelMap.get("gardenList");
        Assertions.assertEquals(0, gardens.size());
    }


}
