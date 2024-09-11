package nz.ac.canterbury.seng302.gardenersgrove.utility;

import java.time.LocalDateTime;

/**
 * Sensor averages in a given period of time.
 * This is used to plot a point on a graph.
 */
public class ArduinoDataBlock {
    private final LocalDateTime endTime;
    private final Double temperatureCelsiusAvg;
    private final Double humidityPercentageAvg;
    private final Double atmosphereAtmAvg;
    private final Double lightLevelPercentageAvg;
    private final Double moisturePercentageAvg;

    /**
     * @param endTime Time of newest arduino reading
     * @param temperatureCelsiusAvg Average of Temperature of block time period
     * @param humidityPercentageAvg Average of Humidity of block time period
     * @param atmosphereAtmAvg Average of Atmosphere of block time period
     * @param lightLevelPercentageAvg Average of Light Level of block time period
     * @param moisturePercentageAvg Average of Moisture of block time period
     */
    public ArduinoDataBlock(LocalDateTime endTime, Double temperatureCelsiusAvg,
                            Double humidityPercentageAvg, Double atmosphereAtmAvg,
                            Double lightLevelPercentageAvg, Double moisturePercentageAvg) {
        this.endTime = endTime;
        this.temperatureCelsiusAvg = temperatureCelsiusAvg;
        this.humidityPercentageAvg = humidityPercentageAvg;
        this.atmosphereAtmAvg = atmosphereAtmAvg;
        this.lightLevelPercentageAvg = lightLevelPercentageAvg;
        this.moisturePercentageAvg = moisturePercentageAvg;
    }

    /**
     * Null constructor, ie: no data in block.
     */
    public ArduinoDataBlock() {
        this.endTime = null;
        this.temperatureCelsiusAvg = null;
        this.humidityPercentageAvg = null;
        this.atmosphereAtmAvg = null;
        this.lightLevelPercentageAvg = null;
        this.moisturePercentageAvg = null;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Double getTemperatureCelsiusAvg() {
        return temperatureCelsiusAvg;
    }

    public Double getHumidityPercentageAvg() {
        return humidityPercentageAvg;
    }

    public Double getAtmosphereAtmAvg() {
        return atmosphereAtmAvg;
    }

    public Double getLightLevelPercentageAvg() {
        return lightLevelPercentageAvg;
    }

    public Double getMoisturePercentageAvg() {
        return moisturePercentageAvg;
    }
}

