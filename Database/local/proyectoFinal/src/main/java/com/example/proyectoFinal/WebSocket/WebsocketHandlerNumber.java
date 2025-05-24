package com.example.proyectoFinal.WebSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class WebsocketHandlerNumber extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Random random = new Random();
    private int numeroadivinar = random.nextInt(0, 100); // de 0 a 99 inclusive

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessions.add(session);

        // Extraer el username del token de conexión, o en este caso por la URI (ej: ?username=Juan)

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("🧹 Sesión eliminada: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {



        String payload = message.getPayload();

        if (!payload.contains(":")) {
            sendToSession(session, "❌ Formato inválido. Usa username:number");
            return;
        }

        String[] parts = payload.split(":", 2);
        String username = parts[0].trim();
        String numberStr = parts[1].trim();

        try {
            if (numberStr.equals("connected")){
                sendToSession(session,"welcome to guess the number "+username);
                System.out.println("welcome to guess the number "+username);
            }else {
                int guess;
                try {
                    guess = Integer.parseInt(numberStr);
                } catch (NumberFormatException e) {
                    sendToSession(session, "❌ Número inválido: " + numberStr);
                    return;
                }

                String response;
                if (guess < numeroadivinar) {
                    response = username + " ha dicho " + guess + ", pero el número es **mayor**.";
                } else if (guess > numeroadivinar) {
                    response = username + " ha dicho " + guess + ", pero el número es **menor**.";
                } else {
                    response = "🎯 ¡" + username + " ha adivinado el número " + guess + "! Se genera un nuevo número...";
                    numeroadivinar = random.nextInt(0, 100);
                }

                // Broadcast a todos los clientes
                broadcast(response);
            }
        }catch (Exception e){
            System.out.println("error in welcome message");
        }


    }

    private void sendToSession(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                System.out.println("❌ Error al enviar a sesión individual: " + e.getMessage());
            }
        }
    }

    private void broadcast(String message) {
        sessions.removeIf(s -> !s.isOpen()); // limpiar sesiones cerradas
        for (WebSocketSession s : sessions) {
            sendToSession(s, message);
        }
    }


}
