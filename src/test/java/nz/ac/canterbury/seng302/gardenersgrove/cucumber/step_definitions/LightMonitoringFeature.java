package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.AdviceSharedState;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import nz.ac.canterbury.seng302.gardenersgrove.utility.FormattedGraphData;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.monitorGardenUri;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class LightMonitoringFeature {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArduinoDataPointService arduinoDataPointService;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private AdviceSharedState adviceSharedState;

    FormattedGraphData formattedLightData;


    @When("I am received a light reading that is above and equal to {int} percent for more than {int} hours")
    public void i_am_received_a_light_reading_that_is_above_and_equal_to_percent_for_more_than_hours(Integer int1, Integer int2) {

        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();



        LocalDateTime currentTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();


        for (double i=0; i <= int2; i += 0.5) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    int1+1d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(25);
        }

        while(currentTime.isBefore(endTime)){
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    int1-1d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(25);
        }
    }

    @Then("I receive a message saying that the garden is currently receiving full sun light")
    public void i_receive_a_message_saying_that_the_garden_is_currently_receiving_full_sun_light() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("lightAdvice",
                        "This garden has full sun lights. This is ideal for most plants, " +
                                "but some plants should avoid full sun lights because it can cause sunburn. If you have any plants that " +
                                "are sensitive to sunlight, consider moving to other places."));
    }
}
