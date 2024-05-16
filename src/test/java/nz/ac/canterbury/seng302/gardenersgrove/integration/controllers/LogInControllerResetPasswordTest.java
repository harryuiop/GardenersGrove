package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ResetPasswordTokenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import nz.ac.canterbury.seng302.gardenersgrove.service.ResetPasswordTokenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@SpringBootTest
@WithMockUser(value = "1")
@AutoConfigureMockMvc(addFilters = false)
class LogInControllerResetPasswordTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Autowired
    private UserService userService;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    private String correctTokenStr;
    private String correctNewPassword;

    private String currentPassword;

    @MockBean
    private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        Mockito.when(emailSenderService.sendEmail(Mockito.any(), Mockito.any()))
                .thenReturn(true);

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

        resetPasswordTokenRepository.deleteAll();
        correctTokenStr = "123e4567-e89b-12d3-a456-426614174000";
        resetPasswordTokenService.addToken(correctTokenStr, user.getUserId());
        correctNewPassword = "Garden1!";
        currentPassword = user.getPassword();
    }

    @Test
    void goToResetPasswordPage_incorrectToken_redirectToLogin() throws Exception {
        String tokenStr = "abcd";
        long userId = user.getUserId();
        System.out.println(LOGIN_URI_STRING);
        mockMvc.perform(MockMvcRequestBuilders.get(resetPasswordUri(tokenStr, userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LOGIN_URI_STRING));
    }

    @Test
    void goToResetPasswordPage_incorrectToken_stayOnPage() throws Exception {
        String tokenStr = this.correctTokenStr;
        long userId = user.getUserId();
        mockMvc.perform(MockMvcRequestBuilders.get(resetPasswordUri(tokenStr, userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
    }

    @Test
    void submitResetPassword_incorrectToken_passwordUnchanged() throws Exception {
        String tokenStr = "abcd";
        long userId = user.getUserId();
        String newPassword = this.correctNewPassword;
        String retypePassword = this.correctNewPassword;
        mockMvc.perform(MockMvcRequestBuilders.post(resetPasswordUri(tokenStr, userId))
                .param("newPassword", newPassword)
                .param("retypeNewPassword", retypePassword))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LOGIN_URI_STRING));
        User updatedUser = userService.getUserById(user.getUserId());
        Assertions.assertEquals(this.currentPassword, updatedUser.getPassword());
    }

    @Test
    void submitResetPassword_incorrectUserId_passwordUnchanged() throws Exception {
        String tokenStr = this.correctTokenStr;
        long userId = 0;
        String newPassword = this.correctNewPassword;
        String retypePassword = this.correctNewPassword;
        mockMvc.perform(MockMvcRequestBuilders.post(resetPasswordUri(tokenStr, userId))
                        .param("newPassword", newPassword)
                        .param("retypeNewPassword", retypePassword))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LOGIN_URI_STRING));
        User updatedUser = userService.getUserById(user.getUserId());
        Assertions.assertEquals(this.currentPassword, updatedUser.getPassword());
    }

    @Test
    void submitResetPassword_allValid_passwordChanged() throws Exception {
        String tokenStr = this.correctTokenStr;
        long userId = user.getUserId();
        String newPassword = this.correctNewPassword;
        String retypePassword = this.correctNewPassword;
        mockMvc.perform(MockMvcRequestBuilders.post(resetPasswordUri(tokenStr, userId))
                        .param("newPassword", newPassword)
                        .param("retypeNewPassword", retypePassword))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(LOGIN_URI_STRING));
        User updatedUser = userService.getUserById(user.getUserId());
        Assertions.assertNotEquals(this.currentPassword, updatedUser.getPassword());
    }

    @Test
    void submitResetPassword_passwordsDoNotMatch_passwordUnchanged() throws Exception {
        String tokenStr = this.correctTokenStr;
        long userId = user.getUserId();
        String newPassword = this.correctNewPassword;
        String retypePassword = "DifferentPassword1!";
        mockMvc.perform(MockMvcRequestBuilders.post(resetPasswordUri(tokenStr, userId))
                        .param("newPassword", newPassword)
                        .param("retypeNewPassword", retypePassword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
        User updatedUser = userService.getUserById(user.getUserId());
        Assertions.assertEquals(this.currentPassword, updatedUser.getPassword());
    }

    @Test
    void submitResetPassword_passwordsEmpty_passwordUnchanged() throws Exception {
        String tokenStr = this.correctTokenStr;
        long userId = user.getUserId();
        String newPassword = "";
        mockMvc.perform(MockMvcRequestBuilders.post(resetPasswordUri(tokenStr, userId))
                        .param("newPassword", newPassword)
                        .param("retypeNewPassword", newPassword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
        User updatedUser = userService.getUserById(user.getUserId());
        Assertions.assertEquals(this.currentPassword, updatedUser.getPassword());
    }

    @Test
    void submitResetPassword_weakPassword_passwordUnchanged() throws Exception {
        String tokenStr = this.correctTokenStr;
        long userId = user.getUserId();
        String newPassword = "abcdefghij1234";
        mockMvc.perform(MockMvcRequestBuilders.post(resetPasswordUri(tokenStr, userId))
                        .param("newPassword", newPassword)
                        .param("retypeNewPassword", newPassword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
        User updatedUser = userService.getUserById(user.getUserId());
        Assertions.assertEquals(this.currentPassword, updatedUser.getPassword());
    }
}
