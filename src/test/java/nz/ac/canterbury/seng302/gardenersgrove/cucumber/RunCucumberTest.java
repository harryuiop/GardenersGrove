package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import io.cucumber.java.Before;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.seng302.gardenersgrove.GardenersGroveApplication;
import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.junit.platform.suite.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

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
public class RunCucumberTest {
    @SpyBean
    private FormValuesValidator spyFormValuesValidator;

    @Before
    public void setup() throws Exception {
        Mockito.when(spyFormValuesValidator.checkProfanity(Mockito.anyString())).thenReturn(false);
    }
}