package nz.ac.canterbury.seng302.gardenersgrove.utility;

/**
 * Light level used in the advice ranges.
 */
public enum LightLevel {
    FULL_SHADE("Full Shade", 0),
    PART_SHADE("Part Shade", 2),
    PART_SUN("Part Sun",  4),
    FULL_SUN("Full Sun", 6),

    UNKNOWN("Unknown", 0);

    private final String string;
    private final int minimumHour;

    LightLevel(String string, int minimumHour) {
        this.string = string;

        this.minimumHour = minimumHour;
    }

    @Override
    public String toString() {
        return string;
    }

    public int getMinimumHour() {
        return this.minimumHour;
    }

    /**
     * Get the light level from a string value. Used in processing data from the form.
     *
     * @param displayName Name of light level
     * @return The light level enum the string corresponds to
     */
    public static LightLevel fromDisplayName(String displayName) {
        for (LightLevel level : LightLevel.values()) {
            if (level.toString().equalsIgnoreCase(displayName)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown light level: " + displayName);
    }

    /**
     * Get the light level from totalHour value
     *
     * @param totalHour integer value that represent total hours that sensor value is more than 50 percent in 24 hours
     * @return The light level enum the string corresponds to
     */
    public static LightLevel getLightLevel(Double totalHour) {

        if (totalHour == null)  return LightLevel.UNKNOWN;

        for (LightLevel level : LightLevel.values()) {
            if (totalHour >= level.getMinimumHour()) {
                return level;
            }
        }
        throw new IllegalArgumentException(String.format("The Hour value %f is not valid%n", totalHour));
    }
}
