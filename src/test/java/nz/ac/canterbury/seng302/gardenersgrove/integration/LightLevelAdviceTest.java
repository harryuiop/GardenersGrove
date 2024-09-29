package nz.ac.canterbury.seng302.gardenersgrove.integration;


import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LightLevelAdviceTest {


    @Test
    void returnFullSunLevel() {
        double minimumHour = 6.5;
        LightLevel level = LightLevel.getLightLevel(minimumHour);

        Assertions.assertEquals(LightLevel.FULL_SUN, level);

    }
}
