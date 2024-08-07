package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.seng302.gardenersgrove.GardenersGroveApplication;
import nz.ac.canterbury.seng302.gardenersgrove.authentication.CustomAuthenticationProvider;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.suite.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("nz/ac/canterbury/seng302/gardenersgrove/cucumber")
@ConfigurationParameters({
        @ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "nz.ac.canterbury.seng302.gardenersgrove.cucumber"),
        @ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html"),
        @ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
})
@CucumberContextConfiguration
@SpringBootTest(classes = GardenersGroveApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(properties = {"maptiler.api.key=test", "spring.mail.username=test"})
public class RunCucumberTest {

    @SpyBean
    private FormValuesValidator spyFormValuesValidator;

    @MockBean
    private MapTilerGeocoding mapTilerGeocoding;

    @Autowired
    UserService userService;

    public static TriConsumer<String, String, UserService> authMaker;

    @BeforeAll
    public static void beforeAll() {
        // Sets up authenticated user, call this instead of rewriting in every file
        authMaker = (String email, String password, UserService userService) -> {
            User user;
            if (userService.getUserByEmail(email) == null) {
                user = new User(email, "Jane", "Doe", password, "");
                user.setConfirmation(true);
                userService.addUsers(user);
            } else {
                user = userService.getUserByEmail(email);
            }
            CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(userService);
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getEmail(), password);
            Authentication auth = customAuthenticationProvider.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(auth);
            Assertions.assertEquals(userService.getAuthenticatedUser().getUserId(), user.getUserId());
            return auth;
        };
    }

    @Before
    public void setup() throws Exception {
        Mockito.when(spyFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);
    }
}