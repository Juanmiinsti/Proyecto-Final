package com.example.proyectoFinal.service.User;

import com.example.proyectoFinal.Entity.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();
    User getUser(int id);
    User saveUser(User user);
    User updateUser(int id,User user);
    boolean deleteUser(int id);
}
