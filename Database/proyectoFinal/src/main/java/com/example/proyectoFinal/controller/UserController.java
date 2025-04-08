package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Match;
import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.service.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class UserController {

@Autowired
    private IUserService userService;
    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
