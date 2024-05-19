package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import io.cucumber.junit.platform.engine.Constants;
import nz.ac.canterbury.seng302.gardenersgrove.GardenersGroveApplication;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("uc/seng301/petbattler/asg3/cucumber")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME,value = "nz/ac/canterbury/seng302/gardenersgrove/cucumber/step_definitions")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME,value = "pretty, html:target/cucumber-report/cucumber.html")
@SpringBootApplication
@ContextConfiguration(classes = GardenersGroveApplication.class)
public class RunCucumberTest {
}