package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.service.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> findAll() {
        List<Match>matches=matchService.getMatches();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
    @PostMapping("/match")
    public ResponseEntity<Match> addMatch(@RequestBody Match match) {
        matchService.saveMatch(match);
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

}
