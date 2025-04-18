package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.dto.User.CreateUserDTO;
import com.example.proyectoFinal.dto.User.UserDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.exceptions.exceptions.DeleteEntityException;
import com.example.proyectoFinal.exceptions.exceptions.UpdateEntityException;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.User.IUserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private Mapper mapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = mapper.mapList(userService.getUsers(), UserDTO.class);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(mapper.mapType(user, UserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        User user = userService.saveUser(mapper.mapType(userDTO, User.class));
        return new ResponseEntity<>(mapper.mapType(user, UserDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> modifyUser(@PathVariable int id, @Valid @RequestBody CreateUserDTO userDTO) {
        try {
            User updatedUser = userService.modifyUser(id, mapper.mapType(userDTO, User.class));
            return new ResponseEntity<>(mapper.mapType(updatedUser, UserDTO.class), HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-name")
    public ResponseEntity<UserDTO> getUserByName(@RequestParam String name) {
        User user = userService.findUserByName(name)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con nombre: " + name));

        return new ResponseEntity<>(mapper.mapType(user, UserDTO.class), HttpStatus.OK);
    }


}
