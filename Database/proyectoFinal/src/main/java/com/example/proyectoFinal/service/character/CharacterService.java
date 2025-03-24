package com.example.proyectoFinal.service.character;

import com.example.proyectoFinal.repository.ICharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.proyectoFinal.Entity.Character;

import java.util.List;

@Service
public class CharacterService implements ICharacterService {
    @Autowired
    ICharacterRepository characterRepository;


    @Override
    public List<Character> getallCharacters() {
        return characterRepository.findAll();
    }

    @Override
    public Character getCharacterById(int id) {
        return characterRepository.findById(id).orElse(null);
    }

    @Override
    public Character saveCharacter(Character character) {
        return characterRepository.save(character);
    }

    @Override
    public Character modifyCharacter(int id, Character character) {
        Character aux = characterRepository.findById(id).orElse(null);
        return characterRepository.save(aux);
    }

    @Override
    public boolean deleteCharacter(int id) {
        characterRepository.deleteById(id);
        return true;
    }
}
