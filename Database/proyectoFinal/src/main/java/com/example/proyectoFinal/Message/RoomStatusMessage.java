package com.example.proyectoFinal.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mensaje de estado de la sala
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomStatusMessage {
    private String type;    // Tipo de mensaje (player_joined, player_ready, etc.)
    private String userId;  // ID del jugador relacionado
    private String roomId;  // ID de la sala
    private long timestamp = System.currentTimeMillis();

    public RoomStatusMessage(String playerJoined, String userId, String roomId) {
        this.type = playerJoined;
        this.userId = userId;
        this.roomId = roomId;
    }
}