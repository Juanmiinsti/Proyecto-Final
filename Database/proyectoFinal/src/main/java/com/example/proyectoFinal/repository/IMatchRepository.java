package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Integer> {

}
