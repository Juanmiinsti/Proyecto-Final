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

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/enemies")
public class EnemyController {

    @Autowired
    private IEnemyService enemyService;

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<EnemyDTO>> getAllEnemies() {
        List<EnemyDTO> enemies = mapper.mapList(enemyService.getEnemies(), EnemyDTO.class);
        return new ResponseEntity<>(enemies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnemyDTO> getEnemyById(@PathVariable int id) {
        EnemyDTO dto = mapper.mapType(enemyService.getEnemyById(id), EnemyDTO.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

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
