package nz.ac.canterbury.seng302.gardenersgrove.service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

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
                        .bodyToMono(String.class)
                        .onErrorResume(e -> Mono.error(
                                        new RuntimeException("Error while fetching weather data:" + e.getMessage(), e)
                        ));

    }

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("Start");
        weatherService.getWeather().subscribe(System.out::println, System.err::println);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
