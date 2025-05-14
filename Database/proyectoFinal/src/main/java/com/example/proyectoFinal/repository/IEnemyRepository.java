package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEnemyRepository extends JpaRepository<Enemy,Integer> {
}
