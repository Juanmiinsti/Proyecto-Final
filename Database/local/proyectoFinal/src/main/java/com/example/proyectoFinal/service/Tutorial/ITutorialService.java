package com.example.proyectoFinal.service.Tutorial;

import com.example.proyectoFinal.Entity.Tutorial;

import java.util.List;

public interface ITutorialService {
    List<Tutorial> getTutorials();
    Tutorial getTutorialById(int id);
    Tutorial saveTutorial(Tutorial tutorial);
    Tutorial modifyTutorial(int id, Tutorial tutorial);
    boolean deleteTutorial(int id);
}
