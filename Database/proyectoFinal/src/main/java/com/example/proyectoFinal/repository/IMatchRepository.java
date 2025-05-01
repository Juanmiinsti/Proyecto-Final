package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Integer> {
    @Query("select m from  Match m join User u where u.name=?1")
    List<Match>matchsbyUsername(String username);

}
