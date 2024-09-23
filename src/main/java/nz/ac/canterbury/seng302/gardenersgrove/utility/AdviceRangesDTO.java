package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AdviceRanges;

/**
 * Data Transfer object to encapsulate advice ranges. This is used for handling any errors.
 * Note: All getters are used in HTML (ignore no usages).
 */
public class AdviceRangesDTO {
    private final double minTemp;
    private final double maxTemp;
    private final double minSoilMoisture;
    private final double maxSoilMoisture;
    private final double minAirPressure;
    private final double maxAirPressure;
    private final double minHumidity;
    private final double maxHumidity;
    private final LightLevel lightLevel;

    /**
     * Construct a DTO object with set parameters
     */
    public AdviceRangesDTO(double minTemp, double maxTemp, double minSoilMoisture,
                           double maxSoilMoisture, double minAirPressure,
                           double maxAirPressure, double minHumidity, double maxHumidity,
                           LightLevel lightLevel) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.minAirPressure = minAirPressure;
        this.maxAirPressure = maxAirPressure;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.lightLevel = lightLevel;
    }

    /**
     * Construct a DTO object from advice ranges entity.
     *
     * @param adviceRanges Entity to construct object from.
     */
    public AdviceRangesDTO(AdviceRanges adviceRanges) {
        this.minTemp = adviceRanges.getMinTemperature();
        this.maxTemp = adviceRanges.getMaxTemperature();
        this.minSoilMoisture = adviceRanges.getMinMoisture();
        this.maxSoilMoisture = adviceRanges.getMaxMoisture();
        this.minAirPressure = adviceRanges.getMinPressure();
        this.maxAirPressure = adviceRanges.getMaxPressure();
        this.minHumidity = adviceRanges.getMinHumidity();
        this.maxHumidity = adviceRanges.getMaxHumidity();
        this.lightLevel = adviceRanges.getLightLevel();
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinSoilMoisture() {
        return minSoilMoisture;
    }

    public double getMaxSoilMoisture() {
        return maxSoilMoisture;
    }

    public double getMinAirPressure() {
        return minAirPressure;
    }

    public double getMaxAirPressure() {
        return maxAirPressure;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public LightLevel getLightLevel() {
        return lightLevel;
    }
}
