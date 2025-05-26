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

/**
 * WebSocket handler that manages a simple "guess the number" game.
 *
 * Each connected client can send guesses in the format "username:number".
 * The server responds with hints whether the guess is higher or lower
 * than the number to guess. When the correct number is guessed,
 * a new number is generated and broadcasted.
 */
@Component
public class WebsocketHandlerNumber extends TextWebSocketHandler {

    // List of active WebSocket sessions (connected clients)
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Random random = new Random();

    // Number to guess, randomly chosen between 0 and 99 inclusive
    private int numeroadivinar = random.nextInt(0, 100);

    /**
     * Called after a new WebSocket connection is established.
     * Adds the session to the active sessions list.
     *
     * @param session the WebSocket session
     * @throws IOException
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessions.add(session);

        // TODO: Extract username from token or query params if needed
        // Example: URI contains ?username=Juan
    }

    /**
     * Called after a WebSocket connection is closed.
     * Removes the session from the active sessions list.
     *
     * @param session the WebSocket session
     * @param status  the close status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("🧹 Sesión eliminada: " + session.getId());
    }

    /**
     * Handles incoming text messages from clients.
     *
     * Expected message format: "username:number" (e.g. "Juan:45").
     * Special command: "username:connected" sends a welcome message.
     *
     * Compares the guessed number with the number to guess and broadcasts hints.
     *
     * @param session the WebSocket session
     * @param message the received text message
     */
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
            } else {
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

                // Broadcast the response to all connected clients
                broadcast(response);
            }
        } catch (Exception e){
            System.out.println("Error handling message: " + e.getMessage());
        }
    }

    /**
     * Sends a message to a single WebSocket session if open.
     *
     * @param session the target WebSocket session
     * @param message the message to send
     */
    private void sendToSession(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                System.out.println("❌ Error al enviar a sesión individual: " + e.getMessage());
            }
        }
    }

    /**
     * Broadcasts a message to all open WebSocket sessions.
     * Cleans closed sessions before broadcasting.
     *
     * @param message the message to broadcast
     */
    private void broadcast(String message) {
        sessions.removeIf(s -> !s.isOpen()); // Remove closed sessions
        for (WebSocketSession s : sessions) {
            sendToSession(s, message);
        }
    }
}
