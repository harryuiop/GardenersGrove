package nz.ac.canterbury.seng302.gardenersgrove.utility;

/**
 * Light level used in the advice ranges.
 */
public enum LightLevel {
    FULL_SHADE("Full Shade"),
    PART_SHADE("Part Shade"),
    PART_SUN("Part Sun"),
    FULL_SUN("Full Sun");

    public final String string;

    LightLevel(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
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
}
