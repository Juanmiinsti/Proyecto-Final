package com.example.proyectoFinal.controller;



import com.example.proyectoFinal.Entity.Role;
import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.dto.User.CreateUserDTO;
import com.example.proyectoFinal.exceptions.Response;
import com.example.proyectoFinal.mapper.Mapper;
import com.example.proyectoFinal.repository.IRoleRepository;
import com.example.proyectoFinal.service.authentication.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private IRoleRepository roleRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody CreateUserDTO registerUsuarioDto)
    {
        User user = mapper.mapType(registerUsuarioDto, User.class);

        List<Role> roles = new ArrayList<>();
        Role aux=roleRepository.findById(1).orElse(null);
        System.out.println(aux.getName().name());

        user.setRoles(roles);

        authenticationService.signup(user);

        String message = "Usuario registrado correctamente";

        return new ResponseEntity<Response>(Response.ok(message), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody CreateUserDTO loginUserDto)
    {
        //Usuario loginUser = mapper.mapType(loginUserDto, Usuario.class);

        //String jwtToken = authenticationService.authenticate(loginUser);

        User loginUser = mapper.mapType(loginUserDto, User.class);

        String jwtToken = authenticationService.authenticate(loginUser);

        return ResponseEntity.ok(jwtToken);
    }

}
