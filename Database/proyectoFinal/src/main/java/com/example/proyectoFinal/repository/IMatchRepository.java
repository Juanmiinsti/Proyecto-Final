package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMatchRepository extends JpaRepository<Match, Integer> {
}
