package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import nz.ac.canterbury.seng302.gardenersgrove.entity.ResetPasswordToken;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.ResetPasswordTokenRepository;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.ResetPasswordTokenService;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
class LogInControllerResetPasswordTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    private User user;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    private String correctTokenStr;

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

        resetPasswordTokenRepository.deleteAll();
        correctTokenStr = "123e4567-e89b-12d3-a456-426614174000";
        resetPasswordTokenService.addToken(correctTokenStr, user.getUserId());
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
}
