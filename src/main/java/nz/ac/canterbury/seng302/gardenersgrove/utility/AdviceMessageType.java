package nz.ac.canterbury.seng302.gardenersgrove.utility;

/**
 * The type of advice needed (dependent on garden's set advice ranges).
 */
public enum AdviceMessageType {

    /**
     * There is a point below the minimum range in the last 24 hours.
     */
    BELOW,

    /**
     * There is a point above the minimum range in the last 24 hours.
     */
    ABOVE,

    /**
     * All points are within the range in the last 24 hours.
     */
    WITHIN,

    /**
     * There exists points below and above the range in the last 24 hours.
     */
    BELOW_AND_ABOVE,

    /**
     * There are no recorded sensor results in the last 24 hours.
     */
    EMPTY
}
