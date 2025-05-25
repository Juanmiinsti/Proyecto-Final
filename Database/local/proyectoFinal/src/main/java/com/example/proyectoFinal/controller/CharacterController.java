package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Character;
import com.example.proyectoFinal.dto.Character.CharacterDTO;
import com.example.proyectoFinal.dto.Character.CreateCharacterDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.character.ICharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Obtiene la lista de todos los personajes")
    @ApiResponse(responseCode = "200", description = "Lista de personajes obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterDTO.class)))
    @GetMapping("/characters")
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        List<CharacterDTO> characterDTOs = mapper.mapList(characterService.getCharacters(), CharacterDTO.class);
        return new ResponseEntity<>(characterDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Obtiene un personaje por su ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Personaje encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterDTO.class))), @ApiResponse(responseCode = "404", description = "Personaje no encontrado", content = @Content)})
    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@Parameter(description = "ID del personaje a buscar", required = true) @PathVariable int id) {
        CharacterDTO characterDTO = mapper.mapType(characterService.getCharacterById(id), CharacterDTO.class);
        return new ResponseEntity<>(characterDTO, HttpStatus.OK);
    }

    @Operation(summary = "Crea un nuevo personaje")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Personaje creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterDTO.class))), @ApiResponse(responseCode = "400", description = "Datos inválidos en la petición", content = @Content)})
    @PostMapping("/characters")
    public ResponseEntity<CharacterDTO> createCharacter(@Parameter(description = "Datos para crear el personaje", required = true) @Valid @RequestBody CreateCharacterDTO characterDTO) {
        Character character = mapper.mapType(characterDTO, Character.class);
        Character saved = characterService.saveCharacter(character);
        return new ResponseEntity<>(mapper.mapType(saved, CharacterDTO.class), HttpStatus.CREATED);
    }

    @Operation(summary = "Modifica un personaje existente")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Personaje modificado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterDTO.class))), @ApiResponse(responseCode = "500", description = "Error al actualizar el personaje", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)))})
    @PutMapping("/characters/{id}")
    public ResponseEntity<?> modifyCharacter(@Parameter(description = "ID del personaje a modificar", required = true) @PathVariable int id, @Parameter(description = "Datos actualizados del personaje", required = true) @Valid @RequestBody CreateCharacterDTO characterDetails) {
        try {
            Character character = characterService.modifyCharacter(id, mapper.mapType(characterDetails, Character.class));
            return new ResponseEntity<>(mapper.mapType(character, CharacterDTO.class), HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Elimina un personaje por su ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Personaje eliminado exitosamente", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "500", description = "Error al eliminar el personaje", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)))})
    @DeleteMapping("/characters/{id}")
    public ResponseEntity<?> deleteCharacter(@Parameter(description = "ID del personaje a eliminar", required = true) @PathVariable int id) {
        try {
            characterService.deleteCharacter(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
