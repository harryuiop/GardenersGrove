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

    private LightLevel(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
