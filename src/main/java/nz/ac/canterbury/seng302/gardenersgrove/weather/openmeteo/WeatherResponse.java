package nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

@java.lang.SuppressWarnings("all")
public class WeatherResponse {
    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("generationtime_ms")
    private double timeToGenerate;
    @JsonProperty("utc_offset_seconds")
    private int utcOffetTime;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;
    @JsonProperty("elevation")
    private double elevation;
    @JsonProperty("hourly_units")
    private HourlyUnits hourlyUnits;
    @JsonProperty("hourly")
    private HourlyWeather hourly;

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", generationtime_ms=" + timeToGenerate +
                ", utc_offset_seconds=" + utcOffetTime +
                ", timezone='" + timezone + '\'' +
                ", timezone_abbreviation='" + timezoneAbbreviation + '\'' +
                ", elevation=" + elevation +
                ", hourly_units=" + hourlyUnits +
                ", hourly=" + hourly +
                '}';
    }

    public HourlyWeather getHourly() {
        return hourly;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setGenerationtime_ms(double generationtime_ms) {
        this.timeToGenerate = generationtime_ms;
    }

    public void setUtc_offset_seconds(int utc_offset_seconds) {
        this.utcOffetTime = utc_offset_seconds;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezoneAbbreviation = timezone_abbreviation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setHourly_units(HourlyUnits hourly_units) {
        this.hourlyUnits = hourly_units;
    }

    public void setHourly(HourlyWeather hourly) {
        this.hourly = hourly;
    }
}

