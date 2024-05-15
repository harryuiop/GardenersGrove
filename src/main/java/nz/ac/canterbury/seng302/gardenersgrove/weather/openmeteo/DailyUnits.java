package nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyUnits {
    @JsonProperty("time")
    private String timestamps;

    @JsonProperty("weather_code")
    private String weatherCodes;

    @JsonProperty("temperature_2m_max")
    private String maximumTemperatures;

    public void setTime(String time) {
        this.timestamps = time;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCodes = weatherCode;
    }

    public void setTemperatureMax(String temperatureMax) {
        this.maximumTemperatures = temperatureMax;
    }

    /**
     * A toString method generated by ChatGPT to override the
     * information getting printed
     * @return a string containing all information about
     * daily units
     */
    @Override
    public String toString() {
        return "DailyUnits{" +
                "timestamps='" + timestamps + '\'' +
                ", weatherCodes='" + weatherCodes + '\'' +
                ", maximumTemperatures='" + maximumTemperatures + '\'' +
                '}';
    }
}
