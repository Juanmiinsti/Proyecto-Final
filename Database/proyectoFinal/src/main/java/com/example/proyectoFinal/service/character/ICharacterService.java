package com.example.proyectoFinal.service.character;

import com.example.proyectoFinal.service.IEntity;

public interface ICharacterService extends IEntity<Character,Integer> {
    @Override
    Character save(Character character);

    @Override
    Character modify(Character save, Integer integer);

}
