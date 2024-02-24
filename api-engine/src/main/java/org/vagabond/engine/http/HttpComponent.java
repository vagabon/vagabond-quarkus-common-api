package org.vagabond.engine.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.vagabond.engine.exeption.TechnicalException;

@ApplicationScoped
public class HttpComponent {

    public <T> T httpGet(String url, Class<T> payloadCLass) throws InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Accept", "application/json").GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructType(payloadCLass));
        } catch (UnsupportedOperationException | IOException e) {
            throw new TechnicalException("HTTP GET", e);
        } finally {
            client.close();
        }
    }
}
