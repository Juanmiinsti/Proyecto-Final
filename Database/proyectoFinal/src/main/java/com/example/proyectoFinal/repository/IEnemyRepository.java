package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEnemyRepository extends JpaRepository<Enemy,Integer> {
}
