package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITutorialRepositoy extends JpaRepository<Tutorial, Integer> {
}
