package com.example.proyectoFinal.Message;

import lombok.Data;

/**
 * Mensaje para unirse a una sala
 */
@Data
public class RoomJoinMessage {
    private String roomId;  // ID de la sala a unirse
    private String userId;  // ID del jugador (opcional, se obtiene del token)
}