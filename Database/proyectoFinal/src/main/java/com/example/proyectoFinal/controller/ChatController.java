package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.dto.Messages.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@Controller
public class ChatController {

    @MessageMapping("/chat")  // El cliente enviará a /app/chat.sendMessage
    @SendTo("androidUser")              // Todos los suscritos a /topic/public lo recibirán
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        return message;
    }




}
