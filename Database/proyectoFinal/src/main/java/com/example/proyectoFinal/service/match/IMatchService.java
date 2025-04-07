package com.example.proyectoFinal.service.match;

import com.example.proyectoFinal.Entity.Match;

import java.util.List;

public interface IMatchService {
    List<Match> getMatches();
    Match getMatchById(int id);
    Match saveMatch(Match match);
    Match modifyMatch(int id,Match match);
    boolean deleteMatch(int id);
}
