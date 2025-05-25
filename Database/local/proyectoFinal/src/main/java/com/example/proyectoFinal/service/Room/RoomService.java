package com.example.proyectoFinal.service.Room;

import com.example.proyectoFinal.Entity.Room;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService implements IRoomService{
    @Autowired
    IRoomRepository roomRepository;

    @Override
    public Room getRoom(int id) {
        return roomRepository.findById(id).orElseThrow(()->new NotFoundEntityException(String.valueOf(id), Room.class));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(int id, Room room) {
        Room oldRoom = getRoom(id);
        room.setId(oldRoom.getId());

        return roomRepository.save(room);
    }

    @Override
    public boolean deleteRoom(int id) {
        Room room = getRoom(id);
        roomRepository.delete(room);
        return true;
    }
}
