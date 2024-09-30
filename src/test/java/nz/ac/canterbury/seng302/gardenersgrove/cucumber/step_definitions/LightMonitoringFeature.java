package nz.ac.canterbury.seng302.gardenersgrove.cucumber.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.gardenersgrove.cucumber.AdviceSharedState;
import nz.ac.canterbury.seng302.gardenersgrove.entity.ArduinoDataPoint;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.service.ArduinoDataPointService;
import nz.ac.canterbury.seng302.gardenersgrove.service.GardenService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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



    @When("I am received a light reading that is above and equal to {int} percent for more than {int} hours")
    public void i_am_received_a_light_reading_that_is_above_and_equal_to_percent_for_more_than_hours(Integer minimumLightPercent, Integer minimumHour) {

        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        LocalDateTime currentTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        for (double i=0; i <= minimumHour; i += 0.5) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    51d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(30);
        }

        while(currentTime.isBefore(endTime)){
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    49d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(30);
        }

        int lightDataSize = arduinoDataPointService.getDayGraphData(garden.getId(), endTime).getLight().size();
        Assertions.assertEquals(48, lightDataSize);
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


    @When("I am received a light reading that is above and equal to {int} percent for more than {int} hours and less than {int} hours")
    public void i_am_received_a_light_reading_that_is_above_and_equal_to_percent_for_more_than_hours_and_less_than_hours(Integer minimumLightPercentage, Integer minimumHour, Integer upBoundHour) {

        Optional<Garden> optionalGarden = gardenService.getGardenById(adviceSharedState.getGardenId());
        if (optionalGarden.isEmpty()) {
            Assertions.fail();
        }
        Garden garden = optionalGarden.get();

        Random random = new Random();
        double randomHour = random.nextDouble(minimumHour, upBoundHour);

        LocalDateTime currentTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        for (double i=0; i <= randomHour; i += 0.5) {
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    51d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(30);
        }

        while(currentTime.isBefore(endTime)){
            arduinoDataPointService.saveDataPoint(new ArduinoDataPoint(
                    garden,
                    currentTime,
                    30d,
                    40d,
                    1d,
                    49d,
                    50d
            ));
            currentTime = currentTime.plusMinutes(30);
        }
        int lightDataSize = arduinoDataPointService.getDayGraphData(garden.getId(), endTime).getLight().size();
        Assertions.assertEquals(48, lightDataSize);

    }
    @Then("I receive a message saying that the garden is currently receiving partial sun light")
    public void i_receive_a_message_saying_that_the_garden_is_currently_receiving_partial_sun_light() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("lightAdvice",
                        "This garden has partially sun lights. This is ideal for most plants, " +
                                "however some plants that need to be in shade may need some care as they might be sensitive on sun lights. " +
                                "Please consider moving them to other places to avoid sunburn."));
    }

    @Then("I receive a message saying that the garden is currently receiving partial shade light")
    public void i_receive_a_message_saying_that_the_garden_is_currently_receiving_partial_shade_light() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("lightAdvice",
                        "This garden is in partially shaded area. This is ideal for most plants, " +
                                " however some plants might need to have some attention if the plants need sun lights but did not get it so far."));
    }

    @Then("I receive a message saying that the garden is currently receiving full shade light")
    public void i_receive_a_message_saying_that_the_garden_is_currently_receiving_full_shade_light() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(monitorGardenUri(adviceSharedState.getGardenId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("lightAdvice",
                        "This garden is in full shaded area. This is ideal for some plants that " +
                                "need to be in shade. Please pay attention to the plants and turn on full spectrum lights if you have."));
    }

}
