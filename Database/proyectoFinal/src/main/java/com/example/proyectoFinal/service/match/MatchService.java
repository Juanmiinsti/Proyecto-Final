package com.example.proyectoFinal.service.match;

import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.repository.IMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
        return matchRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));
    }

    @Override
    public Match saveMatch(Match match) {
        try {
            return matchRepository.save(match);
        } catch (Exception e) {
            throw new CreateEntityException(match, e);
        }

    }

    @Override
    public Match modifyMatch(int id, Match match) {
        Match oldMatch =
                matchRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));

        match.setId(oldMatch.getId());
        return matchRepository.save(match);
    }

    @Override
    public boolean deleteMatch(int id) {
        Match match =
                matchRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));
        matchRepository.delete(match);
        return true;
    }
}
