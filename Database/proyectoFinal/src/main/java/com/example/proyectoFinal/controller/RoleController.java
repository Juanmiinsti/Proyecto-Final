package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.dto.Match.MatchDTO;
import com.example.proyectoFinal.service.Role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/{name}")

    public ResponseEntity<List<Integer>> getRolesByName(@PathVariable String name) {
        List<Integer> roles=roleService.rolesbyname(name);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
