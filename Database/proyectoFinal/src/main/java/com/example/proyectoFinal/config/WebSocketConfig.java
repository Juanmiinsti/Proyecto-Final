package com.example.proyectoFinal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para enviar mensajes desde el cliente (Godot)
        config.setApplicationDestinationPrefixes("/app");

        // Prefijos para suscripciones desde Godot
        config.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint para conexión WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Permite conexiones desde cualquier origen (ajustar en producción)
                .withSockJS(); // Soporte para fallback

        // Endpoint alternativo sin SockJS para Godot
        registry.addEndpoint("/ws-godot")
                .setAllowedOriginPatterns("*");
    }
}