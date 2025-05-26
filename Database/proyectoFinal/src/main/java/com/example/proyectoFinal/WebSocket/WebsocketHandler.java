package com.example.proyectoFinal.WebSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * WebsocketHandler is a basic WebSocket handler that manages WebSocket connections
 * and broadcasts received text messages to all connected clients.
 *
 * <p>Whenever a message is received from any client, it is forwarded to all open sessions.</p>
 */
@Component
public class WebsocketHandler extends TextWebSocketHandler {

    /**
     * List of currently active WebSocket sessions.
     * <p>
     * Note: Consider using a thread-safe list implementation like CopyOnWriteArrayList
     * to handle concurrent access in a multithreaded environment.
     * </p>
     */
    List<WebSocketSession> sessions = new ArrayList<>();

    /**
     * Called when a text message is received from a client.
     * Broadcasts the message payload to all open WebSocket sessions.
     *
     * @param session the session from which the message was received
     * @param message the received text message
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message.getPayload()));
                } catch (IOException e) {
                    System.out.println("Error sending message: " + e.getMessage());
                }
            } else {
                System.out.println("Closed session, cannot respond");
            }
        }
    }

    /**
     * Called when a new WebSocket connection is established.
     * Adds the new session to the list of active sessions.
     *
     * @param session the newly established WebSocket session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    /**
     * Called when a WebSocket connection is closed.
     * Removes the closed session from the list of active sessions.
     *
     * @param session the WebSocket session that was closed
     * @param status  the reason for the connection closure
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("Session removed: " + session.getId());
    }
}
