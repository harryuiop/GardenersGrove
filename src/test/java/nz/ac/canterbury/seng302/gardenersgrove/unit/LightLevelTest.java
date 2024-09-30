package nz.ac.canterbury.seng302.gardenersgrove.unit;


import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LightLevelTest {


    @Test
    void returnFullSunLevel() {
        double minimumHour = 6;
        LightLevel level = LightLevel.getLightLevel(minimumHour);

        Assertions.assertEquals(LightLevel.FULL_SUN, level);

    }

    @Test
    void returnPartSunLevel() {

        double minimumHour = 5;
        LightLevel level = LightLevel.getLightLevel(minimumHour);

        Assertions.assertEquals(LightLevel.PART_SUN, level);
    }

    @Test
    void returnPartShadeLevel() {
        double minimumHour = 3;
        LightLevel level = LightLevel.getLightLevel(minimumHour);

        Assertions.assertEquals(LightLevel.PART_SHADE, level);
    }

    @Test
    void returnFullShadeLevel() {
        double minimumHour = 0.5;
        LightLevel level = LightLevel.getLightLevel(minimumHour);

        Assertions.assertEquals(LightLevel.FULL_SHADE, level);
    }
}
