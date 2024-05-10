package nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class was generated by ChatGPT where the JSON response
 * from the API was provided
 */
public class HourlyUnits {
    @JsonProperty("time")
    private String time;

    @JsonProperty("relative_humidity_2m")
    private String relativeHumidity2m;

    // Getters and setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRelativeHumidity2m() {
        return relativeHumidity2m;
    }

    public void setRelativeHumidity2m(String relativeHumidity2m) {
        this.relativeHumidity2m = relativeHumidity2m;
    }

    /**
     * A toString method generated by ChatGPT to override the
     * information getting printed
     * @return a string containing all information about
     * hourly units
     */
    @Override
    public String toString() {
        return "HourlyUnits{" +
                "time='" + time + '\'' +
                ", relativeHumidity2m='" + relativeHumidity2m + '\'' +
                '}';
    }
}

