package nz.ac.canterbury.seng302.gardenersgrove.utility;

import nz.ac.canterbury.seng302.gardenersgrove.exceptions.ProfanityCheckingException;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class ProfanityChecker {

    /**
     * Checks if a given string contains inappropriate language
     *
     * @param string is the test that needs checking
     * @return true if contains profanity, false otherwise
     */
    public static boolean checkProfanity(String string) throws ProfanityCheckingException, InterruptedException {
        URI uri = new DefaultUriBuilderFactory().builder()
                .scheme("https")
                .host("www.purgomalum.com")
                .path("service/containsprofanity")
                .queryParam("text", string.replace(" ", "%20"))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        String responseMessage;
        try (HttpClient client = HttpClient.newHttpClient()) {
            responseMessage = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException exception) {
            throw new ProfanityCheckingException("Failed to check for profanity", exception);
        }

        return Objects.equals(responseMessage, "true");
    }
}
