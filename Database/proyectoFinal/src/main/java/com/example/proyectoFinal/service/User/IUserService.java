package com.example.proyectoFinal.service.User;

import com.example.proyectoFinal.Entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    List<User> getUsers();
    User getUserById(int id);
    User saveUser(User user);
    User modifyUser(int id, User user);
    boolean deleteUser(int id);
    Optional<User> findUserByName(String name);

}
