package com.example.proyectoFinal.service.Tutorial;
import com.example.proyectoFinal.Entity.Tutorial;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.ITutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorialService implements ITutorialService {

    @Autowired
    ITutorialRepository tutorialRepository;

    @Override
    public List<Tutorial> getTutorials() {
        return tutorialRepository.findAll();
    }

    @Override
    public Tutorial getTutorialById(int id) {
        return tutorialRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Tutorial.class));
    }

    @Override
    public Tutorial saveTutorial(Tutorial tutorial) {
        try {
            return tutorialRepository.save(tutorial);
        } catch (Exception e) {
            throw new CreateEntityException(tutorial, e);
        }
    }

    @Override
    public Tutorial modifyTutorial(int id, Tutorial tutorial) {
        Tutorial oldTutorial = tutorialRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Tutorial.class));
        tutorial.setId(oldTutorial.getId());
        return tutorialRepository.save(tutorial);
    }

    @Override
    public boolean deleteTutorial(int id) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Tutorial.class));
        tutorialRepository.delete(tutorial);
        return true;
    }
}
