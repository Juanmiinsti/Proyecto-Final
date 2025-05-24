package com.example.proyectoFinal.WebSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebsocketHandler extends TextWebSocketHandler {
    List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        for (WebSocketSession s:sessions){
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message.getPayload()));
                } catch (IOException e) {
                    System.out.println("Error al enviar respuesta: " + e.getMessage());
                }
            } else {
                System.out.println("Sesión cerrada, no se puede responder");
            }
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        //the messages will be broadcasted to all users.
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)  {
        sessions.remove(session);
        System.out.println("Sesión eliminada: " + session.getId());

    }


}
