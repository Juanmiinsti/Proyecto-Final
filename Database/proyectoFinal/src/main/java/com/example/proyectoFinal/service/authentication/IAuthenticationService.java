package com.example.proyectoFinal.service.authentication;


import com.example.proyectoFinal.Entity.User;

public interface IAuthenticationService
{
    public User signup(User newUser);

    public String authenticate(User user);
}
