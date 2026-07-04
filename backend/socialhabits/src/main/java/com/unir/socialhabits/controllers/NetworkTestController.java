package com.unir.socialhabits.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;
import java.net.Socket;

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
}