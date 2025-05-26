package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.dto.Role.RoleDTO;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.service.Role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling operations related to roles.
 * Provides endpoints for retrieving role data.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private Mapper mapper;

    /**
     * Retrieves role IDs by the given username.
     *
     * @param name The name of the user.
     * @return A list of role IDs associated with the user, with HTTP status 200.
     */
    @GetMapping("/{name}")
    public ResponseEntity<List<Integer>> getRolesByName(@PathVariable String name) {
        List<Integer> roles = roleService.rolesbyname(name);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    /**
     * Retrieves all available roles.
     *
     * @return A list of role DTOs with HTTP status 200.
     */
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = mapper.mapList(roleService.getRoles(), RoleDTO.class);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
