package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Character;
import com.example.proyectoFinal.Entity.Test;
import com.example.proyectoFinal.service.character.ICharacterService;
import com.example.proyectoFinal.service.test.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CharacterController {
    @Autowired
    private ICharacterService characterService;


    @GetMapping("/char")
    public ResponseEntity<List<Character>> getAllCharacters()
    {
        List<Character>characters=characterService.getallCharacters();
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/char/{id}")
    public ResponseEntity<Character> getCharacterByID(@PathVariable int id)
    {
        Character character=characterService.getCharacterById(id);
        return ResponseEntity.ok(character);
    }
}
