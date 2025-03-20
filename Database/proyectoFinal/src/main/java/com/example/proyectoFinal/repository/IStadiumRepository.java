package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IStadiumRepository extends JpaRepository<Stadium, Integer> {
}
