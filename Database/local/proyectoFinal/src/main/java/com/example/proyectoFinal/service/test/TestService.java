package com.example.proyectoFinal.service.test;

import com.example.proyectoFinal.Entity.Test;
import com.example.proyectoFinal.repository.ITestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TestService implements ITestService {
    @Autowired
    ITestRepository testRepository;

    @Override
    public List<Test> getTests() {
        return testRepository.findAll();
    }
}
