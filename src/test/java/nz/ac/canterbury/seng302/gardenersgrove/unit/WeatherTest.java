package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherData;
import nz.ac.canterbury.seng302.gardenersgrove.weather.WeatherService;
import nz.ac.canterbury.seng302.gardenersgrove.weather.openmeteo.OpenMeteoWeather;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Import(OpenMeteoWeather.class)
public class WeatherTest {

    public WeatherService weatherService = new OpenMeteoWeather();

    static WeatherData sunny1 = new WeatherData(LocalDate.now(), 24.1, "Clear sky", 83.0, "bi bi-brightness-high");
    static WeatherData sunny2 = new WeatherData(LocalDate.now(), 24.1, "Mainly clear", 83.0, "bi bi-brightness-high");
    static WeatherData sunny3 = new WeatherData(LocalDate.now(), 24.1, "Partly cloudy", 83.0, "bi bi-brightness-high");
    static WeatherData rainy1 = new WeatherData(LocalDate.now(), 24.1, "Light drizzle", 83.0, "bi bi-brightness-high");
    static WeatherData rainy2 = new WeatherData(LocalDate.now(), 24.1, "Light freezing drizzle", 83.0, "bi bi-brightness-high");
    static WeatherData rainy3 = new WeatherData(LocalDate.now(), 24.1, "Heavy snow fall", 83.0, "bi bi-brightness-high");
    static WeatherData rainy4 = new WeatherData(LocalDate.now(), 24.1, "Moderate thunderstorm", 83.0, "bi bi-brightness-high");
    static WeatherData rainy5 = new WeatherData(LocalDate.now(), 24.1, "Thunderstorm with heavy hail", 83.0, "bi bi-brightness-high");
    static WeatherData other1 = new WeatherData(LocalDate.now(), 24.1, "Fog", 83.0, "bi bi-brightness-high");
    static WeatherData other2 = new WeatherData(LocalDate.now(), 24.1, "Overcast", 83.0, "bi bi-brightness-high");
    static WeatherData other3 = new WeatherData(LocalDate.now(), 24.1, "Depositing rine fog", 83.0, "bi bi-brightness-high");


    static Stream<List<WeatherData>> weatherDataProviderSunny() {
        List<WeatherData> sunnyAdvice1 = Arrays.asList(sunny1, sunny1);
        List<WeatherData> sunnyAdvice2 = Arrays.asList(sunny1, sunny2);
        List<WeatherData> sunnyAdvice3 = Arrays.asList(sunny1, sunny3);
        List<WeatherData> sunnyAdvice4 = Arrays.asList(sunny2, sunny2);
        List<WeatherData> sunnyAdvice5 = Arrays.asList(sunny2, sunny3);
        List<WeatherData> sunnyAdvice6 = Arrays.asList(sunny3, sunny3);
        return Stream.of(sunnyAdvice1, sunnyAdvice2, sunnyAdvice3, sunnyAdvice4, sunnyAdvice5, sunnyAdvice6);
    }

    static Stream<List<WeatherData>> weatherDataProviderRainy() {
        List<WeatherData> rainyAdvice1 = Arrays.asList(rainy1, rainy1);
        List<WeatherData> rainyAdvice2 = Arrays.asList(rainy1, rainy2);
        List<WeatherData> rainyAdvice3 = Arrays.asList(rainy1, rainy3);
        List<WeatherData> rainyAdvice4 = Arrays.asList(rainy1, rainy4);
        List<WeatherData> rainyAdvice5 = Arrays.asList(rainy1, rainy5);
        List<WeatherData> rainyAdvice6 = Arrays.asList(rainy2, rainy2);
        List<WeatherData> rainyAdvice7 = Arrays.asList(rainy2, rainy3);
        List<WeatherData> rainyAdvice8 = Arrays.asList(rainy2, rainy4);
        List<WeatherData> rainyAdvice9 = Arrays.asList(rainy2, rainy5);
        List<WeatherData> rainyAdvice10 = Arrays.asList(rainy3, rainy3);
        List<WeatherData> rainyAdvice11 = Arrays.asList(rainy3, rainy4);
        List<WeatherData> rainyAdvice12 = Arrays.asList(rainy3, rainy5);
        List<WeatherData> rainyAdvice13 = Arrays.asList(rainy4, rainy4);
        List<WeatherData> rainyAdvice14 = Arrays.asList(rainy4, rainy5);
        List<WeatherData> rainyAdvice15 = Arrays.asList(rainy5, rainy5);

        return Stream.of(rainyAdvice1, rainyAdvice2, rainyAdvice3, rainyAdvice4,
                rainyAdvice5, rainyAdvice6, rainyAdvice7, rainyAdvice8, rainyAdvice9,
                rainyAdvice10, rainyAdvice11, rainyAdvice12, rainyAdvice13, rainyAdvice14,
                rainyAdvice15);
    }

    static Stream<List<WeatherData>> weatherDataProviderOther() {
        List<WeatherData> otherAdvice1 = Arrays.asList(sunny1, rainy1);
        List<WeatherData> otherAdvice2 = Arrays.asList(sunny1, other1);
        List<WeatherData> otherAdvice3 = Arrays.asList(rainy1, other1);
        List<WeatherData> otherAdvice4 = Arrays.asList(other1, other1);
        List<WeatherData> otherAdvice5 = Arrays.asList(sunny2, rainy2);
        List<WeatherData> otherAdvice6 = Arrays.asList(sunny2, other2);
        List<WeatherData> otherAdvice7 = Arrays.asList(rainy2, other2);
        List<WeatherData> otherAdvice8 = Arrays.asList(other2, other2);
        List<WeatherData> otherAdvice9 = Arrays.asList(sunny3, rainy3);
        List<WeatherData> otherAdvice10 = Arrays.asList(sunny3, other3);
        List<WeatherData> otherAdvice11 = Arrays.asList(rainy3, other3);
        List<WeatherData> otherAdvice12 = Arrays.asList(other3, other3);
        List<WeatherData> otherAdvice13 = Arrays.asList(sunny1, other3);
        List<WeatherData> otherAdvice14 = Arrays.asList(rainy3, other3);


        return Stream.of(otherAdvice1, otherAdvice2, otherAdvice3, otherAdvice4,
                otherAdvice5, otherAdvice6, otherAdvice7, otherAdvice8, otherAdvice9,
                otherAdvice10, otherAdvice11, otherAdvice12, otherAdvice13, otherAdvice14);
    }

    @ParameterizedTest
    @MethodSource("weatherDataProviderSunny")
    void weatherAdviceSunny(List<WeatherData> weatherData) {
        Assertions.assertEquals(weatherService.getWeatherAdvice(weatherData), "There hasn’t been any rain recently, make sure to water your plants if they need it");
    }

    @ParameterizedTest
    @MethodSource("weatherDataProviderRainy")
    void weatherAdviceRainy(List<WeatherData> weatherData) {
        Assertions.assertEquals(weatherService.getWeatherAdvice(weatherData), "Outdoor plants don’t need any water today");
    }

    @ParameterizedTest
    @MethodSource("weatherDataProviderOther")
    void weatherAdviceOther(List<WeatherData> weatherData) {
        Assertions.assertEquals(weatherService.getWeatherAdvice(weatherData), "Have you checked on your garden today?");
    }
}
