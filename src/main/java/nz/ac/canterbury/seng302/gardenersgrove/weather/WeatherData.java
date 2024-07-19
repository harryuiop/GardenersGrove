package nz.ac.canterbury.seng302.gardenersgrove.weather;

import java.time.LocalDate;

/**
 * This class specifies what data items are needed
 * by the application and generalizes them.
 */
public class WeatherData {
    private final LocalDate date;
    private final double temperature;
    private final double humidity;
    private final String weatherDescription;
    private final String weatherIconName;

    public WeatherData(
            LocalDate date,
            double temperature,
            String weatherDescription,
            double humidity,
            String weatherIconName
    ) {
        this.date = date;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.humidity = humidity;
        this.weatherIconName = weatherIconName;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getWeatherIconName() {
        return weatherIconName;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", weatherDescription='" + weatherDescription + '\'' +
                '}';
    }
}
