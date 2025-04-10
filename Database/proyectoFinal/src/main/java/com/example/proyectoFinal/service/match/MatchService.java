package com.example.proyectoFinal.service.match;

import com.example.proyectoFinal.dto.Match.CreateMatchDTO;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.Entity.Character;
import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.repository.IMatchRepository;
import com.example.proyectoFinal.repository.ICharacterRepository;
import com.example.proyectoFinal.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService implements IMatchService {

    @Autowired
    private IMatchRepository matchRepository;

    @Autowired
    private ICharacterRepository characterRepository; // Repositorio para personajes

    @Autowired
    private IUserRepository userRepository; // Repositorio para usuarios

    @Override
    public List<Match> getMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(int id) {
        return matchRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));
    }


    @Override
    public Match saveMatch(CreateMatchDTO matchDTO) {
        // Buscando las entidades por ID
        Character charWinner = characterRepository.findById(matchDTO.getCharWinner()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getCharWinner()), Character.class));
        Character charLoser = characterRepository.findById(matchDTO.getCharLoser()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getCharLoser()), Character.class));
        User userWinner = userRepository.findById(matchDTO.getUserWinner()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getUserWinner()), User.class));
        User userLoser = userRepository.findById(matchDTO.getUserLoser()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getUserLoser()), User.class));

        // Creando el objeto Match
        Match match = new Match();
        match.setDate(matchDTO.getDate());
        match.setLength(matchDTO.getLength());
        match.setCharWinner(charWinner);
        match.setCharLoser(charLoser);
        match.setUserWinner(userWinner);
        match.setUserLoser(userLoser);

        try {
            return matchRepository.save(match);
        } catch (Exception e) {
            throw new CreateEntityException(match, e);
        }
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
    public Match modifyMatch(int id, CreateMatchDTO matchDTO) {
        Match oldMatch = matchRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));

        // Buscando las entidades por ID
        Character charWinner = characterRepository.findById(matchDTO.getCharWinner()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getCharWinner()), Character.class));
        Character charLoser = characterRepository.findById(matchDTO.getCharLoser()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getCharLoser()), Character.class));
        User userWinner = userRepository.findById(matchDTO.getUserWinner()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getUserWinner()), User.class));
        User userLoser = userRepository.findById(matchDTO.getUserLoser()).orElseThrow(() -> new NotFoundEntityException(String.valueOf(matchDTO.getUserLoser()), User.class));

        // Modificando el objeto Match con los nuevos datos
        oldMatch.setDate(matchDTO.getDate());
        oldMatch.setLength(matchDTO.getLength());
        oldMatch.setCharWinner(charWinner);
        oldMatch.setCharLoser(charLoser);
        oldMatch.setUserWinner(userWinner);
        oldMatch.setUserLoser(userLoser);

        try {
            return matchRepository.save(oldMatch);
        } catch (Exception e) {
            throw new UpdateEntityException(oldMatch, e);
        }
    }

    @Override
    public Match modifyMatch(int id, Match match) {
        Match existingMatch = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));

        // Aseguramos que el ID de la entidad coincida
        match.setId(id);

        try {
            return matchRepository.save(match);
        } catch (Exception e) {
            throw new UpdateEntityException(match, e);
        }
    }

    @Override
    public boolean deleteMatch(int id) {
        Match match = matchRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Match.class));
        matchRepository.delete(match);
        return true;
    }
}
