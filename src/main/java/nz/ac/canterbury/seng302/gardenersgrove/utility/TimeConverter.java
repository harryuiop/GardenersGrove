package nz.ac.canterbury.seng302.gardenersgrove.utility;

import java.time.Duration;

/**
 * A time converter to convert inconvenient time values
 * to readable ones
 */
public class TimeConverter {

    /**
     * Converts the given time in minutes to a readable time
     * @param minutes given as a Long type
     * @return a string of readable time in the format ?day(s), ?hour(s), ?minute(s)
     */
    public static String minutestoTimeString(Long minutes) {
        if (minutes == null) {
            return "Invalid Value";
        }

        Duration duration = Duration.ofMinutes(minutes);

        // Extract days, hours, and minutes from the duration
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long remainingMinutes = duration.toMinutesPart();

        // Build the readable time string
        StringBuilder readableTime = new StringBuilder();

        if (days > 0) {
            readableTime.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            readableTime.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }
        if (remainingMinutes > 0 || readableTime.isEmpty()) {
            readableTime.append(remainingMinutes).append(" minute").append(remainingMinutes > 1 ? "s" : "");
        }

        return readableTime.toString().trim();
    }
}
