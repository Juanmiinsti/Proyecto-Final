package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IObjectRepository extends JpaRepository<Object,Integer> {
}
