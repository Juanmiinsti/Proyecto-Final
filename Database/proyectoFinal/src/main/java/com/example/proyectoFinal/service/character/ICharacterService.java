package com.example.proyectoFinal.service.character;
import com.example.proyectoFinal.Entity.Character;

import java.util.List;

public interface ICharacterService  {
    List<Character> getallCharacters();
    Character getCharacterById(int id);
    Character saveCharacter(Character character);
    Character modifyCharacter(int id,Character character);
    boolean deleteCharacter(int id);


}
