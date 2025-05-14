package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Message.ReadyMessage;
import com.example.proyectoFinal.Message.RoomJoinMessage;
import com.example.proyectoFinal.Message.RoomStatusMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
public class RoomWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public RoomWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Maneja la conexión de un jugador a una sala
     * @param message Datos de conexión
     * @param principal Información del usuario autenticado
     */
    @MessageMapping("/room/join/{roomId}")
    public void handleJoinRoom(
            @DestinationVariable String roomId,
            @Payload RoomJoinMessage message,
            Principal principal) {

        String userId = principal.getName();

        // Validar que la sala existe y tiene espacio
        // (Implementa esta lógica según tu aplicación)

        // Notificar a todos en la sala
        messagingTemplate.convertAndSend("/topic/room/" + roomId,
                new RoomStatusMessage("player_joined", userId, roomId));

        // Respuesta privada al usuario
        messagingTemplate.convertAndSendToUser(userId, "/queue/room/status",
                new RoomStatusMessage("you_joined", userId, roomId));
    }

    /**
     * Maneja el estado "listo" de un jugador
     */
    @MessageMapping("/room/ready")
    public void handleReadyStatus(@Payload ReadyMessage message, Principal principal) {
        String userId = principal.getName();
        String roomId = message.getRoomId();

        // Notificar a todos en la sala
        messagingTemplate.convertAndSend("/topic/room/" + roomId,
                new RoomStatusMessage("player_ready", userId, roomId));
    }
}