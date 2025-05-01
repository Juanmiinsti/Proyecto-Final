package com.example.proyectoFinal.service.match;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.dto.Match.CreateMatchDTO;

import java.util.List;

public interface IMatchService {
    // Métodos para obtener los matches
    List<Match> getMatches();
    Match getMatchById(int id);

    // Métodos para guardar el match
    Match saveMatch(CreateMatchDTO matchDTO); // Recibe un DTO
    Match saveMatch(Match match); // Recibe una entidad (internamente)

    // Métodos para modificar el match
    Match modifyMatch(int id, CreateMatchDTO matchDTO); // Recibe un DTO
    Match modifyMatch(int id, Match match); // Recibe una entidad (internamente)

    List<Match>winnerMatchbyUsername(String name);
    List<Match>lostmatchsbyName(String name);


    // Método para eliminar el match
    boolean deleteMatch(int id);
}
