package com.example.proyectoFinal.dto.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO implements Serializable {
    private int id;
    private String name;
    private int capacity;
}
