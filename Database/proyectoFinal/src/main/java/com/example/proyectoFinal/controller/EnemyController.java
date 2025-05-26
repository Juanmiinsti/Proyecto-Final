package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Enemy;
import com.example.proyectoFinal.dto.Enemy.CreateEnemyDTO;
import com.example.proyectoFinal.dto.Enemy.EnemyDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.enemy.IEnemyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing Enemy entities.
 * Provides endpoints to perform CRUD operations on enemies.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/enemies")
public class EnemyController {

    @Autowired
    private IEnemyService enemyService;

    @Autowired
    private Mapper mapper;

    /**
     * Retrieves all enemies in the system.
     *
     * @return A {@link ResponseEntity} containing a list of {@link EnemyDTO} and HTTP status 200.
     */
    @GetMapping
    public ResponseEntity<List<EnemyDTO>> getAllEnemies() {
        List<EnemyDTO> enemies = mapper.mapList(enemyService.getEnemies(), EnemyDTO.class);
        return new ResponseEntity<>(enemies, HttpStatus.OK);
    }

    /**
     * Retrieves an enemy by its ID.
     *
     * @param id The ID of the enemy to retrieve.
     * @return A {@link ResponseEntity} containing the corresponding {@link EnemyDTO} and HTTP status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnemyDTO> getEnemyById(@PathVariable int id) {
        EnemyDTO dto = mapper.mapType(enemyService.getEnemyById(id), EnemyDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Creates a new enemy.
     *
     * @param createDTO The DTO containing the enemy data to be created.
     * @return A {@link ResponseEntity} with the created {@link EnemyDTO} and HTTP status 201,
     *         or an error response with status 500 in case of failure.
     */
    @PostMapping
    public ResponseEntity<?> createEnemy(@RequestBody CreateEnemyDTO createDTO) {
        try {
            Enemy entity = mapper.mapType(createDTO, Enemy.class);
            EnemyDTO created = mapper.mapType(enemyService.saveEnemy(entity), EnemyDTO.class);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing enemy identified by its ID.
     *
     * @param id The ID of the enemy to update.
     * @param updateDTO The updated enemy data.
     * @return A {@link ResponseEntity} with the updated {@link EnemyDTO} and HTTP status 200,
     *         or an error response with status 500 in case of failure.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnemy(@PathVariable int id, @RequestBody CreateEnemyDTO updateDTO) {
        try {
            Enemy entity = mapper.mapType(updateDTO, Enemy.class);
            EnemyDTO updated = mapper.mapType(enemyService.updateEnemy(id, entity), EnemyDTO.class);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an enemy by its ID.
     *
     * @param id The ID of the enemy to delete.
     * @return A {@link ResponseEntity} with `true` and HTTP status 200 if successful,
     *         or an error response with status 500 in case of failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnemy(@PathVariable int id) {
        try {
            enemyService.deleteEnemyById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
