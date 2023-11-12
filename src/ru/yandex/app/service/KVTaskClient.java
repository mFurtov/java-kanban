package ru.yandex.app.service;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class KVTaskClient {
    private String apiToken;
    private HttpClient client;
    private String url;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        apiToken = register(url);
    }

    private String register(String url) {

        URI urlReg = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(urlReg).GET().build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RegisterClientException();
        }
    }

    public void put(String key, String json) {
        URI urlReg = URI.create(url + "/save/"+key+"?API_TOKEN="+apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlReg).POST(body).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        try {
           client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при регистрации на сервере KV");
        }
    }
    public String load(String key){
        URI urlReg = URI.create(url + "/load/"+key+"?API_TOKEN="+apiToken);
        HttpRequest request = HttpRequest.newBuilder().uri(urlReg).GET().build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при регистрации на сервере KV");
        }
        return response.body();
    }
}
