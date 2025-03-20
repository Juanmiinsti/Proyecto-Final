package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICharacterRepository extends JpaRepository<Character, Integer> {

}
