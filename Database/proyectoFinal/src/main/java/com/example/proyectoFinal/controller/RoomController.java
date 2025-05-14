package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Room;
import com.example.proyectoFinal.dto.Room.CreateRoomDTO;
import com.example.proyectoFinal.dto.Room.RoomDTO;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.service.Room.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/lobbies") // o "/api/rooms" si preferís
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @Autowired
    private Mapper mapper;

    // Obtener todas las salas
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> roomDTOList = mapper.mapList(roomService.getAllRooms(), RoomDTO.class);
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }

    // Obtener una sala por ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable int id) {
        RoomDTO roomDTO = mapper.mapType(roomService.getRoom(id), RoomDTO.class);
        return new ResponseEntity<>(roomDTO, HttpStatus.OK);
    }

    // Crear una nueva sala
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomDTO roomDTO) {
        try {
            Room roomaux=mapper.mapType(roomDTO, Room.class);
            RoomDTO createdRoom = mapper.mapType(roomService.createRoom(roomaux), RoomDTO.class);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Modificar sala existente
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyRoom(@PathVariable int id, @RequestBody CreateRoomDTO roomDTO) {
        try {
            Room roomaux=mapper.mapType(roomDTO, Room.class);
            RoomDTO updatedRoom = mapper.mapType(roomService.updateRoom(id, roomaux), RoomDTO.class);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar una sala
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id) {
        try {
            roomService.deleteRoom(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
