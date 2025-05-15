package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.dto.Messages.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")  // El cliente enviará a /app/chat.sendMessage
    @SendTo("/topic/public")              // Todos los suscritos a /topic/public lo recibirán
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage message,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Agrega el usuario a la sesión WebSocket
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        message.setContent("joined the chat");
        return message;
    }
}
