package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
import nz.ac.canterbury.seng302.gardenersgrove.utility.LightLevel;

/**
 * Entity to save user specific advice ranges
 */
@Entity
public class AdviceRanges {

    private final static long DEFAULT_MIN_TEMPERATURE = 10;
    private final static long DEFAULT_MAX_TEMPERATURE = 10;
    private final static long DEFAULT_MIN_MOISTURE = 10;
    private final static long DEFAULT_MAX_MOISTURE = 10;
    private final static long DEFAULT_MIN_PRESSURE = 10;
    private final static long DEFAULT_MAX_PRESSURE = 10;
    private final static long DEFAULT_MIN_HUMIDITY = 10;
    private final static long DEFAULT_MAX_HUMIDITY = 10;
    private final static LightLevel DEFAULT_LIGHT_LEVEL = LightLevel.PART_SUN;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long minTemperature;

    @Column(nullable = false)
    private long maxTemperature;

    @Column(nullable = false)
    private long minMoisture;

    @Column(nullable = false)
    private long maxMoisture;

    @Column(nullable = false)
    private long minPressure;

    @Column(nullable = false)
    private long maxPressure;

    @Column(nullable = false)
    private long minHumidity;

    @Column(nullable = false)
    private long maxHumidity;

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

    public Long getId() {
        return id;
    }

    public long getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(long minTemperature) {
        this.minTemperature = minTemperature;
    }

    public long getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(long maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public long getMinMoisture() {
        return minMoisture;
    }

    public void setMinMoisture(long minMoisture) {
        this.minMoisture = minMoisture;
    }

    public long getMaxMoisture() {
        return maxMoisture;
    }

    public void setMaxMoisture(long maxMoisture) {
        this.maxMoisture = maxMoisture;
    }

    public long getMinPressure() {
        return minPressure;
    }

    public void setMinPressure(long minPressure) {
        this.minPressure = minPressure;
    }

    public long getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(long maxPressure) {
        this.maxPressure = maxPressure;
    }

    public long getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(long minHumidity) {
        this.minHumidity = minHumidity;
    }

    public long getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(long maxHumidity) {
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
