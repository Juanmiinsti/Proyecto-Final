package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.dto.Match.CreateMatchDTO;
import com.example.proyectoFinal.dto.Match.MatchDTO;
import com.example.proyectoFinal.service.match.IMatchService;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private IMatchService matchService;

    @Autowired
    private Mapper mapper;  // Si tienes un mapper para hacer la conversión entre DTO y entidades

    // Obtener todos los partidos
    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.getMatches(), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }

    // Obtener un partido por ID
    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable int id) {
        MatchDTO matchDTO = mapper.mapType(matchService.getMatchById(id), MatchDTO.class);
        return new ResponseEntity<>(matchDTO, HttpStatus.OK);
    }


    @GetMapping("/total/{name}")
    public ResponseEntity<List<MatchDTO>> gettotalMathchesByusername(@PathVariable String name) {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.matchesbyuUsername(name), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }

    @GetMapping("/win/{name}")
    public ResponseEntity<List<MatchDTO>> getWinnedMathchesByusername(@PathVariable String name) {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.winnerMatchbyUsername(name), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }
    @GetMapping("/lost/{name}")
    public ResponseEntity<List<MatchDTO>> getLoserMathchesByusername(@PathVariable String name) {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.lostmatchsbyName(name), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }


        // Crear un nuevo partido
    @PostMapping
    public ResponseEntity<?> createMatch(@RequestBody CreateMatchDTO matchDTO) {
        try {
            MatchDTO createdMatch = mapper.mapType(matchService.saveMatch(matchDTO), MatchDTO.class);
            return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Modificar un partido existente
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyMatch(@PathVariable int id, @RequestBody CreateMatchDTO matchDTO) {
        try {
            MatchDTO updatedMatch = mapper.mapType(matchService.modifyMatch(id, matchDTO), MatchDTO.class);
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un partido
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable int id) {
        try {
            matchService.deleteMatch(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
