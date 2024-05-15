package nz.ac.canterbury.seng302.gardenersgrove.weather;

import java.util.List;

public interface WeatherService {
    List<ForecastWeatherData> getWeatherData(float latitude, float longitude) throws InterruptedException;
}
