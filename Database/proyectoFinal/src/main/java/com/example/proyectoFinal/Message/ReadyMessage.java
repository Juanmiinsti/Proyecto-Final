package com.example.proyectoFinal.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadyMessage {
    private String roomId;  // ID de la sala
    private boolean isReady; // Estado de "listo"
}