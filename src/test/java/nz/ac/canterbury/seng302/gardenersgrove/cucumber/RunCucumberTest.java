package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.seng302.gardenersgrove.GardenersGroveApplication;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.location.MapTilerGeocoding;
import org.junit.platform.suite.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("nz/ac/canterbury/seng302/gardenersgrove/cucumber")
@ConfigurationParameters({
        @ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "nz/ac/canterbury/seng302/gardenersgrove/cucumber/step_definitions"),
        @ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-report/cucumber.html"),
        @ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
})
@ContextConfiguration(classes = GardenersGroveApplication.class)
@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(properties = {"maptiler.api.key=test", "spring.mail.username=test"})
public class RunCucumberTest {
    @SpyBean
    private FormValuesValidator spyFormValuesValidator;

    @MockBean
    private MapTilerGeocoding mapTilerGeocoding;


    @Before
    public void setup() throws Exception {
        Mockito.when(spyFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);
    }

}