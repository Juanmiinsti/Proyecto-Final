package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.dto.User.CreateUserDTO;
import com.example.proyectoFinal.dto.User.UserDTO;
import com.example.proyectoFinal.dto.User.UserWithRoleDTO;
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

/**
 * REST controller for managing users.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private Mapper mapper;

    /**
     * Get all users.
     *
     * @return List of UserDTO and HTTP status 200.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = mapper.mapList(userService.getUsers(), UserDTO.class);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Get a user by their ID.
     *
     * @param id User ID.
     * @return UserDTO and HTTP status 200.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(mapper.mapType(user, UserDTO.class), HttpStatus.OK);
    }

    /**
     * Create a new user.
     *
     * @param userDTO DTO containing user data to create.
     * @return Created UserDTO and HTTP status 201.
     */
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        User user = userService.saveUser(mapper.mapType(userDTO, User.class));
        return new ResponseEntity<>(mapper.mapType(user, UserDTO.class), HttpStatus.CREATED);
    }

    /**
     * Modify an existing user by ID.
     *
     * @param id      User ID.
     * @param userDTO DTO containing updated user data including roles.
     * @return Updated UserDTO and HTTP status 200, or error response with HTTP status 500 if update fails.
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<?> modifyUser(@PathVariable int id, @Valid @RequestBody UserWithRoleDTO userDTO) {
        try {
            User updatedUser = userService.modifyUser(id, mapper.mapType(userDTO, User.class));
            return new ResponseEntity<>(mapper.mapType(updatedUser, UserDTO.class), HttpStatus.OK);
        } catch (UpdateEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a user by ID.
     *
     * @param id User ID.
     * @return True with HTTP status 200 if deleted, or error response with HTTP status 500.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (DeleteEntityException e) {
            return new ResponseEntity<>(Response.generalError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a user by their name.
     *
     * @param name Username.
     * @return UserDTO and HTTP status 200.
     * @throws RuntimeException if user not found.
     */
    @GetMapping("/userByName/{name}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String name) {
        User user = userService.findUserByName(name)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + name));

        return new ResponseEntity<>(mapper.mapType(user, UserDTO.class), HttpStatus.OK);
    }
}
