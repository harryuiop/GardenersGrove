package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerEditTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    private User user;
    @BeforeEach
    void setUp() {
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
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(user);

    }

    @Test
    void editUserProfile_allFieldsValid_profileUpdated() throws Exception {
        String newEmail = "jane@doe1.com";
        String newFirstName = "First";
        String newLastName = "Last";
        boolean noSurname = false;
        String newDateOfBirth = "2000-05-05";
        mockMvc.perform(MockMvcRequestBuilders.post(editProfileUri())
                        .param("email", newEmail)
                        .param("firstName", newFirstName)
                        .param("lastName", newLastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("dateOfBirth", newDateOfBirth))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(VIEW_PROFILE_URI_STRING));

        Assertions.assertEquals(newEmail, user.getEmail());
        Assertions.assertEquals(newFirstName, user.getFirstName());
        Assertions.assertEquals(newLastName, user.getLastName());
        Assertions.assertEquals(newDateOfBirth, user.getDob());
    }

    @ParameterizedTest
    @CsvSource({
            "janedoe2.com, First, Last, false, 2000-05-05",
            "\"\", First, Last, false, 2000-05-05",
            "jane@doe3.com, First%#, Last, false, 2000-05-05",
            "jane@doe4.com, \"\", Last, false, 2000-05-05",
            "jane@doe5.com, First, Last%#, false, 2000-05-05",
            "jane@doe6.com, First, \"\", false, 2000-05-05"

    })
    void editUserProfile_invalidEmailFormat_profileNotUpdated(
            String newEmail, String newFirstName, String newLastName, boolean noSurname, String newDateOfBirth
    ) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(editProfileUri())
                        .param("email", newEmail)
                        .param("firstName", newFirstName)
                        .param("lastName", newLastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("dateOfBirth", newDateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("editProfile"));

        Assertions.assertNotEquals(newEmail, user.getEmail());
        Assertions.assertNotEquals(newFirstName, user.getFirstName());
        Assertions.assertNotEquals(newLastName, user.getLastName());
        Assertions.assertNotEquals(newDateOfBirth, user.getDob());
    }

    @Test
    void editUserProfile_emptyLastNameNoSurnameChecked_profileUpdated() throws Exception {
        String newEmail = "jane@doe7.com";
        String newFirstName = "First";
        String newLastName = "";
        boolean noSurname = true;
        String newDateOfBirth = "2000-05-05";
        mockMvc.perform(MockMvcRequestBuilders.post(editProfileUri())
                        .param("email", newEmail)
                        .param("firstName", newFirstName)
                        .param("lastName", newLastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("dateOfBirth", newDateOfBirth))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(VIEW_PROFILE_URI_STRING));

        Assertions.assertEquals(newEmail, user.getEmail());
        Assertions.assertEquals(newFirstName, user.getFirstName());
        Assertions.assertEquals(newLastName, user.getLastName());
        Assertions.assertEquals(newDateOfBirth, user.getDob());
    }

    @Test
    void editUserProfile_noDate_profileUpdated() throws Exception {
        String newEmail = "jane@doe8.com";
        String newFirstName = "First";
        String newLastName = "Last";
        boolean noSurname = false;
        String newDateOfBirth = "";
        mockMvc.perform(MockMvcRequestBuilders.post(editProfileUri())
                        .param("email", newEmail)
                        .param("firstName", newFirstName)
                        .param("lastName", newLastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("dateOfBirth", newDateOfBirth))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(viewProfileUri().toString()));

        Assertions.assertEquals(newEmail, user.getEmail());
        Assertions.assertEquals(newFirstName, user.getFirstName());
        Assertions.assertEquals(newLastName, user.getLastName());
        Assertions.assertEquals(newDateOfBirth, user.getDob());

    }
}
