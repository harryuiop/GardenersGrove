package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;

/**
 * Entity to save user specific advice ranges
 */
@Entity
public class AdviceRanges {

    public static final double DEFAULT_MIN_TEMPERATURE = 10;
    public static final double DEFAULT_MAX_TEMPERATURE = 30;
    public static final double DEFAULT_MIN_MOISTURE = 20;
    public static final double DEFAULT_MAX_MOISTURE = 90;
    public static final double DEFAULT_MIN_PRESSURE = 0.8;
    public static final double DEFAULT_MAX_PRESSURE = 1.1;
    public static final double DEFAULT_MIN_HUMIDITY = 10;
    public static final double DEFAULT_MAX_HUMIDITY = 90;
    public static final LightLevel DEFAULT_LIGHT_LEVEL = LightLevel.PART_SUN;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double minTemperature;

    @Column(nullable = false)
    private double maxTemperature;

    @Column(nullable = false)
    private double minMoisture;

    @Column(nullable = false)
    private double maxMoisture;

    @Column(nullable = false)
    private double minPressure;

    @Column(nullable = false)
    private double maxPressure;

    @Column(nullable = false)
    private double minHumidity;

    @Column(nullable = false)
    private double maxHumidity;

    @Column(nullable = false)
    private LightLevel lightLevel;

    /**
     * Uses the default values specified to initially create the advice ranges.
     */
    public AdviceRanges() {
        this.minTemperature = DEFAULT_MIN_TEMPERATURE;
        this.maxTemperature = DEFAULT_MAX_TEMPERATURE;

        this.minHumidity = DEFAULT_MIN_HUMIDITY;
        this.maxHumidity = DEFAULT_MAX_HUMIDITY;

        this.minMoisture = DEFAULT_MIN_MOISTURE;
        this.maxMoisture = DEFAULT_MAX_MOISTURE;

        this.minPressure = DEFAULT_MIN_PRESSURE;
        this.maxPressure = DEFAULT_MAX_PRESSURE;

        this.lightLevel = DEFAULT_LIGHT_LEVEL;
    }

    public double getId() {
        return id;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinMoisture() {
        return minMoisture;
    }

    public void setMinMoisture(double minMoisture) {
        this.minMoisture = minMoisture;
    }

    public double getMaxMoisture() {
        return maxMoisture;
    }

    public void setMaxMoisture(double maxMoisture) {
        this.maxMoisture = maxMoisture;
    }

    public double getMinPressure() {
        return minPressure;
    }

    public void setMinPressure(double minPressure) {
        this.minPressure = minPressure;
    }

    public double getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(double maxPressure) {
        this.maxPressure = maxPressure;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public LightLevel getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(LightLevel lightLevel) {
        this.lightLevel = lightLevel;
    }

    /**
     * Resets all advice ranges to the default values.
     **/
    public void resetToDefaults() {
        this.minTemperature = DEFAULT_MIN_TEMPERATURE;
        this.maxTemperature = DEFAULT_MAX_TEMPERATURE;

        this.minHumidity = DEFAULT_MIN_HUMIDITY;
        this.maxHumidity = DEFAULT_MAX_HUMIDITY;

        this.minMoisture = DEFAULT_MIN_MOISTURE;
        this.maxMoisture = DEFAULT_MAX_MOISTURE;

        this.minPressure = DEFAULT_MIN_PRESSURE;
        this.maxPressure = DEFAULT_MAX_PRESSURE;

        this.lightLevel = DEFAULT_LIGHT_LEVEL;
    }
}
