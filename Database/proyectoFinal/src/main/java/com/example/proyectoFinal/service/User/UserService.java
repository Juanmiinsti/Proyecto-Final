package com.example.proyectoFinal.service.User;

import com.example.proyectoFinal.Entity.User;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), User.class));
    }

    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new CreateEntityException(user, e);
        }
    }

    @Override
    public User modifyUser(int id, User user) {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), User.class));
        user.setId(oldUser.getId());
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), User.class));
        userRepository.delete(user);
        return true;
    }
    @Override
    public Optional<User> findUserByName(String name) {
        return userRepository.findUserByName(name);
    }

}
