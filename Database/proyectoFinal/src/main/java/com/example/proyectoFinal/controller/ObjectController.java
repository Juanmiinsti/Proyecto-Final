package com.example.proyectoFinal.controller;


import com.example.proyectoFinal.dto.Match.MatchDTO;
import com.example.proyectoFinal.dto.Object.CreateObjectDTO;
import com.example.proyectoFinal.dto.Object.ObjectDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.Object.IObjectService;
import com.example.proyectoFinal.service.Object.ObjectService;
import com.example.proyectoFinal.Entity.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/items")
public class ObjectController {
    @Autowired private IObjectService objectService;

    @Autowired
    private Mapper mapper;

    @GetMapping()
    public ResponseEntity<List<ObjectDTO>> getAllObjects() {
        List<ObjectDTO> objectDTOList = mapper.mapList(objectService.getObjects(), ObjectDTO.class);
        return new ResponseEntity<>(objectDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectDTO> getObjectById(@RequestParam int id) {
        ObjectDTO objectDTOList = mapper.mapType(objectService.getObjectById(id), ObjectDTO.class);
        return new ResponseEntity<>(objectDTOList, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createObject(@RequestBody CreateObjectDTO objectDTO) {
        try {
            Object aux=mapper.mapType(objectDTO, Object.class);
            ObjectDTO createdObject = mapper.mapType(objectService.saveObject(aux), ObjectDTO.class);
            return new ResponseEntity<>(createdObject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Modificar un objeto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyObject(@PathVariable int id, @RequestBody CreateObjectDTO objectDTO) {
        try {
            Object aux=mapper.mapType(objectDTO, Object.class);
            ObjectDTO updatedObject = mapper.mapType(objectService.updateObject(id, aux), ObjectDTO.class);
            return new ResponseEntity<>(updatedObject, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un objeto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteObject(@PathVariable int id) {
        try {
            objectService.deleteObjectById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
