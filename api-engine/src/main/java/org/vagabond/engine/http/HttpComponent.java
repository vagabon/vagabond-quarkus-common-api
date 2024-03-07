package org.vagabond.engine.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import jakarta.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.exeption.TechnicalException;

@ApplicationScoped
public class HttpComponent {

    public <T> T httpGet(String url, Class<T> payloadCLass) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Accept", "application/json").GET().build();
        return send(request, payloadCLass);
    }

    public <T> T httpPost(String url, String jsonBody, Class<T> payloadCLass) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).header("Accept", "application/json")
                .POST(BodyPublishers.ofString(jsonBody)).build();
        return send(request, payloadCLass);
    }

    public HttpResponse<String> send(HttpRequest request) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            return response;
        } catch (UnsupportedOperationException | IOException exception) {
            throw new TechnicalException(exception.getMessage(), exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new TechnicalException(exception.getMessage(), exception);
        } finally {
            client.close();
        }
    }

    public <T> T send(HttpRequest request, Class<T> payloadCLass) {
        var response = send(request);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructType(payloadCLass));
        } catch (JsonProcessingException exception) {
            throw new MetierException(exception.getMessage(), exception);
        }
    }
}
