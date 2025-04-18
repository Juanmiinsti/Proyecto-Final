package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.name = ?1")
    Optional<User> findUserByName(String name);

}
