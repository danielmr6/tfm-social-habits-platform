package com.unir.socialhabits.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class NetworkTestController {

    @GetMapping("/smtp-test")
    public String smtpTest() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("smtp.gmail.com", 587), 5000);
            return "Conexión OK";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @GetMapping("/google")
    public String google() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return String.valueOf(response.statusCode());
    }
}