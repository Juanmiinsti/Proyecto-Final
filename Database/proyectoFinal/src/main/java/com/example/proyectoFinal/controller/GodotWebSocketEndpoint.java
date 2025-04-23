package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.config.CustomSpringConfigurator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Component
@ServerEndpoint(value = "/ws-godot", configurator = CustomSpringConfigurator.class)
public class GodotWebSocketEndpoint {

    private static final Map<Session, String> userSessions = new ConcurrentHashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("✅ Godot conectado");
    }



    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("📩 Raw recibido: " + message);
        try {
            Map<String, String> parsed = mapper.readValue(message, Map.class);
            System.out.println("✅ Parsed: " + parsed);

            String type = parsed.get("type");
            // seguir con tu lógica...
        } catch (Exception e) {
            System.out.println("❌ Error parseando JSON: " + e.getMessage());
        }
    }


    @OnClose
    public void onClose(Session session) {
        System.out.println("🔌 Godot desconectado");
        userSessions.remove(session);
    }

    private void broadcastToAll(Map<String, String> msg) throws IOException {
        String json = mapper.writeValueAsString(msg);
        for (Session sess : userSessions.keySet()) {
            if (sess.isOpen()) {
                sess.getBasicRemote().sendText(json);
            }
        }
    }
}
