// CharacterService.java
package com.example.proyectoFinal.service.character;

import com.example.proyectoFinal.Entity.Character;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.ICharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService implements ICharacterService {
    @Autowired
    ICharacterRepository characterRepository;

    @Override
    public List<Character> getCharacters() {
        return characterRepository.findAll();
    }

    @Override
    public Character getCharacterById(int id) {
        return characterRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Character.class));
    }

    @Override
    public Character saveCharacter(Character character) {
        try {
            return characterRepository.save(character);
        } catch (Exception e) {
            throw new CreateEntityException(character, e);
        }
    }

    @Override
    public Character modifyCharacter(int id, Character character) {
        Character old = characterRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Character.class));
        character.setId(old.getId());
        return characterRepository.save(character);
    }

    @Override
    public boolean deleteCharacter(int id) {
        Character character = characterRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Character.class));
        characterRepository.delete(character);
        return true;
    }
}
