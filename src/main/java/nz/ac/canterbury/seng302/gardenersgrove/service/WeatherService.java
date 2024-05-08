package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class WeatherService {

    private final WebClient webClient;

    public WeatherService() {
        this.webClient = WebClient.create("https://api.open-meteo.com/v1");
    }

    public Mono<String> getWeather() {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", "52.52")
                        .queryParam("longitude", "13.41")
                        .queryParam("hourly", "temperature_2m")
                        .build())
                .retrieve()
                .bodyToMono(String.class);

    }

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        System.out.println("hello");
        weatherService.getWeather().subscribe(
                json -> System.out.println(json), error -> System.err.println("Error fetching weather: " + error.getMessage())
        );
        System.out.println("hi");
    }
}
