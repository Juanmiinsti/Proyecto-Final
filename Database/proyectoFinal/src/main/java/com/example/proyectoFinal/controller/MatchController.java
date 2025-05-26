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

/**
 * Controller responsible for handling Match-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting matches,
 * as well as filtering by player name.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private IMatchService matchService;

    @Autowired
    private Mapper mapper;

    /**
     * Retrieves all matches.
     *
     * @return A list of all matches in the system with HTTP status 200.
     */
    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.getMatches(), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }

    /**
     * Retrieves a match by its ID.
     *
     * @param id The ID of the match.
     * @return The match data if found, with HTTP status 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable int id) {
        MatchDTO matchDTO = mapper.mapType(matchService.getMatchById(id), MatchDTO.class);
        return new ResponseEntity<>(matchDTO, HttpStatus.OK);
    }

    /**
     * Retrieves all matches played by a user.
     *
     * @param name The username to filter by.
     * @return A list of matches played by the user with HTTP status 200.
     */
    @GetMapping("/total/{name}")
    public ResponseEntity<List<MatchDTO>> gettotalMathchesByusername(@PathVariable String name) {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.matchesbyuUsername(name), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }

    /**
     * Retrieves matches won by a specific user.
     *
     * @param name The username of the winner.
     * @return A list of won matches with HTTP status 200.
     */
    @GetMapping("/win/{name}")
    public ResponseEntity<List<MatchDTO>> getWinnedMathchesByusername(@PathVariable String name) {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.winnerMatchbyUsername(name), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }

    /**
     * Retrieves matches lost by a specific user.
     *
     * @param name The username of the player.
     * @return A list of lost matches with HTTP status 200.
     */
    @GetMapping("/lost/{name}")
    public ResponseEntity<List<MatchDTO>> getLoserMathchesByusername(@PathVariable String name) {
        List<MatchDTO> matchDTOList = mapper.mapList(matchService.lostmatchsbyName(name), MatchDTO.class);
        return new ResponseEntity<>(matchDTOList, HttpStatus.OK);
    }

    /**
     * Creates a new match.
     *
     * @param matchDTO The match data to create.
     * @return The created match with HTTP status 201, or an error with status 500.
     */
    @PostMapping
    public ResponseEntity<?> createMatch(@RequestBody CreateMatchDTO matchDTO) {
        try {
            MatchDTO createdMatch = mapper.mapType(matchService.saveMatch(matchDTO), MatchDTO.class);
            return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing match.
     *
     * @param id       The ID of the match to update.
     * @param matchDTO The new match data.
     * @return The updated match with HTTP status 200, or an error with status 500.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyMatch(@PathVariable int id, @RequestBody CreateMatchDTO matchDTO) {
        try {
            MatchDTO updatedMatch = mapper.mapType(matchService.modifyMatch(id, matchDTO), MatchDTO.class);
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a match by its ID.
     *
     * @param id The ID of the match to delete.
     * @return True with HTTP status 200 if successful, or an error with status 500.
     */
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
