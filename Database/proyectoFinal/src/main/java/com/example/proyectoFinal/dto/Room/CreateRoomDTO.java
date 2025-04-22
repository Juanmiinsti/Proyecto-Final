package com.example.proyectoFinal.dto.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDTO implements Serializable {
    String name;
    int capacity;
}
