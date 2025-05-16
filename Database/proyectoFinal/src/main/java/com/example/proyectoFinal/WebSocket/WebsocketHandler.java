package com.example.proyectoFinal.WebSocket;

import org.springframework.stereotype.Component;
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
        System.out.println("Mensaje recibido: " + message.getPayload());

        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("Recibido: " + message.getPayload()));
            } catch (IOException e) {
                System.out.println("Error al enviar respuesta: " + e.getMessage());
            }
        } else {
            System.out.println("Sesión cerrada, no se puede responder");
        }

        for (WebSocketSession s:sessions){
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage("Broadcast a sessiones: " + message.getPayload()));
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

}
