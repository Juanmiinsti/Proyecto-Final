package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.dto.Object.CreateObjectDTO;
import com.example.proyectoFinal.dto.Object.ObjectDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.Object.IObjectService;
import com.example.proyectoFinal.Entity.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling operations related to game objects (items).
 * Provides endpoints for creating, retrieving, updating, and deleting items.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/items")
public class ObjectController {

    @Autowired
    private IObjectService objectService;

    @Autowired
    private Mapper mapper;

    /**
     * Retrieves all objects.
     *
     * @return A list of all objects with HTTP status 200.
     */
    @GetMapping
    public ResponseEntity<List<ObjectDTO>> getAllObjects() {
        List<ObjectDTO> objectDTOList = mapper.mapList(objectService.getObjects(), ObjectDTO.class);
        return new ResponseEntity<>(objectDTOList, HttpStatus.OK);
    }

    /**
     * Retrieves an object by its ID.
     *
     * @param id The ID of the object to retrieve.
     * @return The object data with HTTP status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ObjectDTO> getObjectById(@RequestParam int id) {
        ObjectDTO objectDTO = mapper.mapType(objectService.getObjectById(id), ObjectDTO.class);
        return new ResponseEntity<>(objectDTO, HttpStatus.OK);
    }

    /**
     * Creates a new object.
     *
     * @param objectDTO The object data to create.
     * @return The created object with HTTP status 201, or an error with status 500.
     */
    @PostMapping
    public ResponseEntity<?> createObject(@RequestBody CreateObjectDTO objectDTO) {
        try {
            Object aux = mapper.mapType(objectDTO, Object.class);
            ObjectDTO createdObject = mapper.mapType(objectService.saveObject(aux), ObjectDTO.class);
            return new ResponseEntity<>(createdObject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing object.
     *
     * @param id        The ID of the object to update.
     * @param objectDTO The new object data.
     * @return The updated object with HTTP status 200, or an error with status 500.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyObject(@PathVariable int id, @RequestBody CreateObjectDTO objectDTO) {
        try {
            Object aux = mapper.mapType(objectDTO, Object.class);
            ObjectDTO updatedObject = mapper.mapType(objectService.updateObject(id, aux), ObjectDTO.class);
            return new ResponseEntity<>(updatedObject, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an object by its ID.
     *
     * @param id The ID of the object to delete.
     * @return True with HTTP status 200 if successful, or an error with status 500.
     */
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
