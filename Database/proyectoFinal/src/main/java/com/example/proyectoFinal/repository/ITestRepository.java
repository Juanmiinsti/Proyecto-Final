package com.example.proyectoFinal.repository;

import com.example.proyectoFinal.Entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITestRepository extends JpaRepository<Test, Integer> {
}
