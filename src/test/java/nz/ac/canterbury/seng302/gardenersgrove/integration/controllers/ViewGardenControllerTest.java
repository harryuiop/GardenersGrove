package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Tag;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.TagRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.TagService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.newGardenTagUri;
import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.viewGardenUri;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
public class ViewGardenControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;
    @SpyBean
    private UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
    }

    @Test
    public void checkUserHasGarden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(viewGardenUri(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
    }

    @Test
    public void userInputInvalidTagName () throws Exception {
        String tagName = "alkals@U)$(*%&(#*!$&@)";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    public void userInputTagNameExceed25Characters () throws Exception {
        String tagName = "This is invalid tag name which will give you a lot of annoy";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                        .param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(tagName);

        Assertions.assertNull(tag);
    }

    @Test
    public void userInputTagNameExceed25CharactersAndHasInvalidCharacters () throws Exception {
        String tagName = "Thi$ i$ inv@lid t@g name with inv@lid ch@r@cter which will give you @ lot of @nnoy";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1)).param("tagName", tagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));

        Tag tag = tagService.findByName(tagName);
        Assertions.assertNull(tag);
    }

    @Test
    public void userInputValidTagName () throws Exception {
        String validTagName = "Invalid tag name";
        mockMvc.perform(MockMvcRequestBuilders.post(newGardenTagUri(1))
                        .param("tagName", validTagName))
                .andExpect(status().isOk())
                .andExpect(view().name("viewGarden"));
        Tag tag = tagService.findByName(validTagName);

        Assertions.assertNotNull(tag);
    }
}
