package com.example.proyectoFinal.service.authentication;


import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.config.security.JwtTokenProvider;
import com.example.proyectoFinal.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService implements IAuthenticationService
{
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public User signup(User newUser)
    {
        if(userRepository.existsByName(newUser.getUsername()))
        {
            throw new IllegalArgumentException("Username is already in use");
        }

        //Comprobar si los roles existen

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        //newUser.setUsername(newUser.getEmail());

        return userRepository.save(newUser);
    }

    @Override
    public String authenticate(User user) {
        Authentication authResult = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authResult);

        return jwtTokenProvider.generateToken(authResult);
    }
}
