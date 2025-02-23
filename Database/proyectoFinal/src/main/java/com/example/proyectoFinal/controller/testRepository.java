package com.example.proyectoFinal.controller;

import com.example.proyectoFinal.Entity.Test;
import com.example.proyectoFinal.service.test.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class testRepository {
    @Autowired private ITestService testService;


    @GetMapping("/prueba")
    public ResponseEntity<List<Test>> obtenerActores()
    {
        List<Test>tests=testService.getTests();
        return ResponseEntity.ok(tests);
    }

}
