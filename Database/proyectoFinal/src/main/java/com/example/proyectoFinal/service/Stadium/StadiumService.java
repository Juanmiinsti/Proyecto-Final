package com.example.proyectoFinal.service.Stadium;

import com.example.proyectoFinal.Entity.Stadium;
import com.example.proyectoFinal.exceptions.exceptions.CreateEntityException;
import com.example.proyectoFinal.exceptions.exceptions.NotFoundEntityException;
import com.example.proyectoFinal.repository.IStadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StadiumService implements IStadiumService {

    @Autowired
    IStadiumRepository stadiumRepository;

    @Override
    public List<Stadium> getStadiums() {
        return stadiumRepository.findAll();
    }

    @Override
    public Stadium getStadiumById(int id) {
        return stadiumRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Stadium.class));
    }

    @Override
    public Stadium saveStadium(Stadium stadium) {
        try {
            return stadiumRepository.save(stadium);
        } catch (Exception e) {
            throw new CreateEntityException(stadium, e);
        }
    }

    @Override
    public Stadium modifyStadium(int id, Stadium stadium) {
        Stadium oldStadium = stadiumRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Stadium.class));
        stadium.setId(oldStadium.getId());
        return stadiumRepository.save(stadium);
    }

    @Override
    public boolean deleteStadium(int id) {
        Stadium stadium = stadiumRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(String.valueOf(id), Stadium.class));
        stadiumRepository.delete(stadium);
        return true;
    }
}
