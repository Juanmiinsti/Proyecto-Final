package com.example.proyectoFinal.service.Room;

import com.example.proyectoFinal.Entity.Room;

import java.util.List;

public interface IRoomService {
    Room getRoom(int id);
    List<Room> getAllRooms();
    Room createRoom(Room room);
    Room updateRoom(int id,Room room);
    boolean deleteRoom(int id);
}
