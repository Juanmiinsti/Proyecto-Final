package com.example.proyectoFinal.service.User;

import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService implements IUserService {
    @Autowired
    IUserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(int id, User user) {
        User u = userRepository.findById(id).orElse(null);
        user.setId(u.getId());
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(int id) {
        User u = userRepository.findById(id).orElse(null);
        userRepository.delete(u);
        return true;
    }

}
