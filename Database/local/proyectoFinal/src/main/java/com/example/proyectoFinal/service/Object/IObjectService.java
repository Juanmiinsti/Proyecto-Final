package com.example.proyectoFinal.service.Object;
import com.example.proyectoFinal.Entity.Object;

import java.util.List;

public interface IObjectService {
    List<Object> getObjects();
    Object getObjectById(int id);
    Object saveObject(Object object);
    void deleteObjectById(int id);
    Object updateObject(int id,Object object);
}
