package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.dto.Tutorial.CreateTutorialDTO;
import com.example.proyectoFinal.dto.Tutorial.TutorialDTO;
import com.example.proyectoFinal.Entity.Tutorial;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.Tutorial.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/tutorials")
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;

    @Autowired
    private Mapper mapper;

    // Obtener todos los tutoriales
    @GetMapping
    public ResponseEntity<List<TutorialDTO>> getAllTutorials() {
        List<TutorialDTO> tutorialDTOs = mapper.mapList(tutorialService.getTutorials(), TutorialDTO.class);
        return new ResponseEntity<>(tutorialDTOs, HttpStatus.OK);
    }

    // Obtener tutorial por ID
    @GetMapping("/{id}")
    public ResponseEntity<TutorialDTO> getTutorialById(@PathVariable int id) {
        TutorialDTO tutorialDTO = mapper.mapType(tutorialService.getTutorialById(id), TutorialDTO.class);
        return new ResponseEntity<>(tutorialDTO, HttpStatus.OK);
    }

    // Crear nuevo tutorial
    @PostMapping
    public ResponseEntity<?> createTutorial(@RequestBody CreateTutorialDTO createDTO) {
        try {
            Tutorial tutorial = mapper.mapType(createDTO, Tutorial.class);
            TutorialDTO created = mapper.mapType(tutorialService.saveTutorial(tutorial), TutorialDTO.class);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Modificar tutorial existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTutorial(@PathVariable int id, @RequestBody CreateTutorialDTO createDTO) {
        try {
            Tutorial tutorial = mapper.mapType(createDTO, Tutorial.class);
            TutorialDTO updated = mapper.mapType(tutorialService.modifyTutorial(id, tutorial), TutorialDTO.class);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar tutorial
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTutorial(@PathVariable int id) {
        try {
            tutorialService.deleteTutorial(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
