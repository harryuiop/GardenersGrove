package nz.ac.canterbury.seng302.gardenersgrove.weather;

import java.time.LocalDate;

public class ForecastWeatherData {
    private LocalDate date;
    private double maximumTemperature;
    private double minimumTemperature;
    private double humidity;
    private String weatherDescription;

    public ForecastWeatherData(
            LocalDate date,
            double maximumTemperature,
            double minimumTemperature,
            String weatherDescription
    ) {
        this.date = date;
        this.maximumTemperature = maximumTemperature;
        this.minimumTemperature = minimumTemperature;
        this.weatherDescription = weatherDescription;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getMaximumTemperature() {
        return maximumTemperature;
    }

    public double getMinimumTemperature() {
        return minimumTemperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
