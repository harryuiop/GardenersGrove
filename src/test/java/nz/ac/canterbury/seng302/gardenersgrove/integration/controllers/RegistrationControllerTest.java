package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    @Test
    void submitForm_allValid_userSaved() throws Exception {
        String firstName = "John";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/profile*"));

        List<User> allUsers = userRepository.findAll();
        assertEquals(1, allUsers.size());
        User user = allUsers.get(0);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(dateOfBirth, user.getDob());
    }

    @Test
    void submitForm_lastNameBlankNoSurnameTrue_userSaved() throws Exception {
        String firstName = "John";
        String lastName = "";
        Boolean noSurname = true;
        String email = "jane@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/profile*"));

        List<User> allUsers = userRepository.findAll();
        assertEquals(1, allUsers.size());
        User user = allUsers.get(0);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(dateOfBirth, user.getDob());
    }

    @Test
    void submitForm_invalidFirstName_userNotSaved() throws Exception {
        String firstName = "John ###";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void submitForm_blankLastNameNoSurnameFalse_userNotSaved() throws Exception {
        String firstName = "John";
        String lastName = "   ";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void submitForm_invalidEmailFormat_userNotSaved() throws Exception {
        String firstName = "John-William";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "@jws.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void submitForm_tooYoung_userNotSaved() throws Exception {
        String firstName = "John William";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(12));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void submitForm_tooOld_userNotSaved() throws Exception {
        String firstName = "John";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(121));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void submitForm_invalidPassword_userNotSaved() throws Exception {
        String firstName = "John";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "password";
        String passwordConfirm = "Password100%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void submitForm_missMatchingPasswords_userNotSaved() throws Exception {
        String firstName = "John";
        String lastName = "Smith";
        Boolean noSurname = false;
        String email = "john@smith.co.nz";
        String password = "Password100%";
        String passwordConfirm = "password%";
        String dateOfBirth = String.valueOf(LocalDate.now().minusYears(20));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("email", email)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("noSurname", String.valueOf(noSurname))
                        .param("password", password)
                        .param("passwordConfirm", passwordConfirm)
                        .param("dateOfBirth", dateOfBirth))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));

        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.isEmpty());
    }
}