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
    private LightLevel lightLevel; // Could be an enum


    /**
     * JPA requires a no args constructor.
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


}