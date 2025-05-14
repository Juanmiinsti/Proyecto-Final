package com.example.proyectoFinal.config;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomEndpointConfig {

    @Bean
    public ServerEndpointConfig godotEndpointConfig() {
        return ServerEndpointConfig.Builder
                .create(com.example.proyectoFinal.controller.GodotWebSocketEndpoint.class, "/ws-godot")
                .build();
    }
}
