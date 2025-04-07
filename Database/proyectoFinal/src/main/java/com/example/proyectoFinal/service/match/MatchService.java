package com.example.proyectoFinal.service.match;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.repository.IMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService implements IMatchService {
   @Autowired
   IMatchRepository matchRepository;

    @Override
    public List<Match> getMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(int id) {
        return matchRepository.findById(id).orElse(null);
    }

    @Override
    public Match saveMatch(Match match) {

        return matchRepository.save(match);
    }

    @Override
    public Match modifyMatch(int id, Match match) {
        Match oldMatch = matchRepository.findById(id).orElse(null);
        match.setId(oldMatch.getId());
        return matchRepository.save(match);
    }

    @Override
    public boolean deleteMatch(int id) {
        Match match = matchRepository.findById(id).orElse(null);
        matchRepository.delete(match);
        return true ;
    }
}
