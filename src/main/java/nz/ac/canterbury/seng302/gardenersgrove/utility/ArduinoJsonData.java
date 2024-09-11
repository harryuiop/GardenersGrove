package nz.ac.canterbury.seng302.gardenersgrove.utility;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class helps convert json data sent by the arduino into values that can be put in the ArdiunoDataPoint entity and saved to the database.
 */
public class ArduinoJsonData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("temperatureCelsius")
    private Double temperatureCelsius;

    @JsonProperty("humidityPercentage")
    private double humidityPercentage;

    @JsonProperty("atmosphereAtm")
    private double atmosphereAtm;

    @JsonProperty("lightLevelPercentage")
    private double lightLevelPercentage;

    @JsonProperty("moisturePercentage")
    private double moisturePercentage;

    @JsonProperty("time")
    private String time;

    public String getId() {
        return id;
    }

    public Double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getHumidityPercentage() {
        return humidityPercentage;
    }

    public double getAtmosphereAtm() {
        return atmosphereAtm;
    }

    public double getLightLevelPercentage() {
        return lightLevelPercentage;
    }

    public double getMoisturePercentage() {
        return moisturePercentage;
    }

    public LocalDateTime getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(time, formatter);
    }
}
