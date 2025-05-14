package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Character;
import com.example.proyectoFinal.dto.Character.CharacterDTO;
import com.example.proyectoFinal.dto.Character.CreateCharacterDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.character.ICharacterService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CharacterController {

    @Autowired
    private ICharacterService characterService;

    @Autowired
    private Mapper mapper;

    @GetMapping("/characters")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        List<CharacterDTO> characterDTOs = mapper.mapList(characterService.getCharacters(), CharacterDTO.class);
        return new ResponseEntity<>(characterDTOs, HttpStatus.OK);
    }

    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable int id) {
        CharacterDTO characterDTO = mapper.mapType(characterService.getCharacterById(id), CharacterDTO.class);
        return new ResponseEntity<>(characterDTO, HttpStatus.OK);
    }

    @PostMapping("/characters")
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CreateCharacterDTO characterDTO) {
        Character character = mapper.mapType(characterDTO, Character.class);
        Character saved = characterService.saveCharacter(character);
        return new ResponseEntity<>(mapper.mapType(saved, CharacterDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/characters/{id}")
    public ResponseEntity<?> modifyCharacter(@PathVariable int id, @Valid @RequestBody CreateCharacterDTO characterDetails) {
        try {
            Character character = characterService.modifyCharacter(id, mapper.mapType(characterDetails, Character.class));
            return new ResponseEntity<>(mapper.mapType(character, CharacterDTO.class), HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/characters/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable int id) {
        try {
            characterService.deleteCharacter(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
