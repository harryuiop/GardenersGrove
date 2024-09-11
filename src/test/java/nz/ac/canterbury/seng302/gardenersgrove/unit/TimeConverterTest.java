package nz.ac.canterbury.seng302.gardenersgrove.unit;

import static nz.ac.canterbury.seng302.gardenersgrove.utility.TimeConverter.minutestoTimeString;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TimeConverterTest {
    @ParameterizedTest
    @CsvSource({
            "0, 0 minute",
            "59, 59 minutes",
            "60, 1 hour",
            "119, 1 hour 59 minutes",
            "120, 2 hours",
            "1439, 23 hours 59 minutes",
            "1440, 1 day",
            "1441, 1 day 1 minute",
            "1500, 1 day 1 hour"
    })
    void testConvertValidMinutesToReadableTime(Long minutes, String expectedTimeString) {
        Assertions.assertEquals(expectedTimeString, minutestoTimeString(minutes));
    }
}
