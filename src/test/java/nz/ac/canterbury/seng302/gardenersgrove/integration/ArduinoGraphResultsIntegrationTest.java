package nz.ac.canterbury.seng302.gardenersgrove.integration;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Garden;
import nz.ac.canterbury.seng302.gardenersgrove.entity.Location;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.junit.jupiter.api.BeforeAll;

public class ArduinoGraphResultsIntegrationTest {
    private static Garden garden;

    @BeforeAll
    static void setUp() {
        garden = new Garden(
                new User("", "", "", "", ""),
                "", "",
                new Location("", ""),
                1f, true);
    }


    //TODO
}
