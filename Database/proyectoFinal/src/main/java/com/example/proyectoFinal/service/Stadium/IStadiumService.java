package com.example.proyectoFinal.service.Stadium;

import com.example.proyectoFinal.Entity.Stadium;

import java.util.List;

public interface IStadiumService {
    List<Stadium> getStadiums();
    Stadium getStadiumById(int id);
    Stadium saveStadium(Stadium stadium);
    Stadium modifyStadium(int id, Stadium stadium);
    boolean deleteStadium(int id);
}
