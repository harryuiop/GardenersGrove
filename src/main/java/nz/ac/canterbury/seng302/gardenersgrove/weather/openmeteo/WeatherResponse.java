package nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private HourlyWeather hourlyWeather;
    @JsonProperty("daily")
    private DailyWeather dailyWeather;
    @JsonProperty("daily_units")
    private DailyUnits dailyUnits;
    @JsonProperty("current")
    private CurrentWeather currentWeather;
    @JsonProperty("current_units")
    private CurrentUnits currentUnits;

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
                ", hourly=" + hourlyWeather +
                ", daily=" + dailyWeather +
                ", daily_units=" + dailyUnits +
                ", current=" + currentWeather +
                ", current_units=" + currentUnits +
                '}';
    }

    public HourlyWeather getHourlyWeather() {
        return hourlyWeather;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setGenerationtime_ms(double timeToGenerate) {
        this.timeToGenerate = timeToGenerate;
    }

    public void setUtc_offset_seconds(int utcOffsetSeconds) {
        this.utcOffetTime = utcOffetTime;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setTimezone_abbreviation(String timezoneAbbreviation) {
        this.timezoneAbbreviation = timezoneAbbreviation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setHourly_units(HourlyUnits hourlyUnits) {
        this.hourlyUnits = hourlyUnits;
    }

    public void setHourlyWeather(HourlyWeather hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }
}

