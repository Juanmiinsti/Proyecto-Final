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

/**
 * Controller that manages Character-related operations such as creation, retrieval,
 * updating, and deletion.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CharacterController {

    @Autowired
    private ICharacterService characterService;

    @Autowired
    private Mapper mapper;

    /**
     * Retrieves a list of all characters in the system.
     *
     * @return A {@link ResponseEntity} containing a list of {@link CharacterDTO} objects and HTTP status 200.
     */
    @GetMapping("/characters")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        List<CharacterDTO> characterDTOs = mapper.mapList(characterService.getCharacters(), CharacterDTO.class);
        return new ResponseEntity<>(characterDTOs, HttpStatus.OK);
    }

    /**
     * Retrieves a character by its unique ID.
     *
     * @param id The ID of the character to retrieve.
     * @return A {@link ResponseEntity} containing the requested {@link CharacterDTO} and HTTP status 200.
     */
    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable int id) {
        CharacterDTO characterDTO = mapper.mapType(characterService.getCharacterById(id), CharacterDTO.class);
        return new ResponseEntity<>(characterDTO, HttpStatus.OK);
    }

    /**
     * Creates a new character in the system.
     *
     * @param characterDTO The DTO object containing character creation data.
     * @return A {@link ResponseEntity} containing the saved {@link CharacterDTO} and HTTP status 201 (Created).
     */
    @PostMapping("/characters")
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CreateCharacterDTO characterDTO) {
        Character character = mapper.mapType(characterDTO, Character.class);
        Character saved = characterService.saveCharacter(character);
        return new ResponseEntity<>(mapper.mapType(saved, CharacterDTO.class), HttpStatus.CREATED);
    }

    /**
     * Updates an existing character identified by ID.
     *
     * @param id The ID of the character to update.
     * @param characterDetails The updated character data.
     * @return A {@link ResponseEntity} with the updated {@link CharacterDTO} and HTTP status 200,
     *         or an error response with HTTP status 500 if the update fails.
     */
    @PutMapping("/characters/{id}")
    public ResponseEntity<?> modifyCharacter(@PathVariable int id, @Valid @RequestBody CreateCharacterDTO characterDetails) {
        try {
            Character character = characterService.modifyCharacter(id, mapper.mapType(characterDetails, Character.class));
            return new ResponseEntity<>(mapper.mapType(character, CharacterDTO.class), HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a character by its ID.
     *
     * @param id The ID of the character to delete.
     * @return A {@link ResponseEntity} with a boolean true and HTTP status 200 if deletion is successful,
     *         or an error response with HTTP status 500 if deletion fails.
     */
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
