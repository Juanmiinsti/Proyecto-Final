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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles authentication-related endpoints such as user signup and login.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private IRoleRepository roleRepository;

    /**
     * Registers a new user in the system.
     *
     * <p>This method receives a {@link CreateUserDTO} object with user registration data,
     * maps it to a {@link User} entity, assigns a default role (ID 1), and calls the
     * {@link IAuthenticationService#signup(User)} method to complete the registration.</p>
     *
     * @param registerUsuarioDto The DTO object containing the user registration data.
     * @return A {@link ResponseEntity} with a success message and HTTP status 200.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody CreateUserDTO registerUsuarioDto) {
        User user = mapper.mapType(registerUsuarioDto, User.class);

        List<Role> roles = new ArrayList<>();
        Role aux = roleRepository.findById(1).orElse(null);
        System.out.println(aux.getName().name());

        roles.add(aux);
        user.setRoles(roles);

        authenticationService.signup(user);

        String message = "Usuario registrado correctamente";

        return new ResponseEntity<Response>(Response.ok(message), HttpStatus.OK);
    }

    /**
     * Authenticates a user using the provided login credentials.
     *
     * <p>This method maps the incoming {@link CreateUserDTO} to a {@link User} object and
     * delegates authentication to the {@link IAuthenticationService#authenticate(User)} method.
     * If successful, a JWT token is returned.</p>
     *
     * @param loginUserDto The DTO object containing the user's login credentials.
     * @return A {@link ResponseEntity} containing the generated JWT token as a {@link String}.
     */
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody CreateUserDTO loginUserDto) {
        User loginUser = mapper.mapType(loginUserDto, User.class);

        String jwtToken = authenticationService.authenticate(loginUser);

        return ResponseEntity.ok(jwtToken);
    }
}
